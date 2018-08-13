package com.casting.commonmodule.network.base;

import android.content.ContentValues;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.exceptions.ParsingException;
import com.casting.commonmodule.network.exceptions.ServerResponseError;
import com.casting.commonmodule.utility.EasyLog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class BaseHttpRequester<M extends BaseModel> implements NetworkProtocol {

    private final static int DEFAULT_TIMEOUT = 5 * 1000;

    private BaseRequest mRequestCommand;

    private String      mUrlData;
    private String      mHttpMethod;
    private String      mDownloadPath;
    private ContentValues mRequestHeader;
    private ContentValues mParameterValues;

    @SuppressWarnings("unchecked")
    @WorkerThread
    public M execute() throws ParsingException, ServerResponseError, NullPointerException
    {

        M instance = null;

        HttpURLConnection connection = null;

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(mUrlData);
            if (HttpGet.equalsIgnoreCase(mHttpMethod)) urlBuilder.append(convertParameter());

            URL url = new URL(urlBuilder.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setRequestMethod(mHttpMethod);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            if (getRequestHttpHeader() != null) {

                for (String key : getRequestHttpHeader().keySet()) {
                    String value = getRequestHttpHeader().getAsString(key);
                    connection.setRequestProperty(key , value);
                }
            }
            connection.setDoOutput(mHttpMethod.equalsIgnoreCase(HttpPost));
            connection.setDoInput(true);
            connection.connect();

            if (HttpPost.equalsIgnoreCase(mHttpMethod)) {

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(convertParameter());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
            }

            if (isValidHttpConnection(connection.getResponseCode())){

                if (TextUtils.isEmpty(mDownloadPath)) {

                    Class c = mRequestCommand.getTargetClass();

                    Object o = (c != null ? c.newInstance() : null);

                    if (o != null && o instanceof BaseModel)
                    {
                        instance = (M) o;

                        StringBuilder stringBuilder = new StringBuilder();

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        while (true) {

                            String stringLine = bufferedReader.readLine();

                            if (stringLine == null) break;

                            stringBuilder.append(stringLine).append('\n');
                        }

                        bufferedReader.close();
                        connection.getInputStream().close();

                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                        INetworkJSONParcelable iNetworkJSONParcelable = (INetworkJSONParcelable) instance;
                        iNetworkJSONParcelable.parseJSON2Data(jsonObject);
                    }

                }
                else
                {
                    int read = 0;

                    byte[] bytes = new byte[1024];

                    File file = new File(mDownloadPath);

                    if (file.createNewFile())
                    {
                        OutputStream outputStream = new FileOutputStream(file);

                        while ((read = connection.getInputStream().read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                        connection.getInputStream().close();
                    }
                }

            }
            else
            {

                EasyLog.LogMessage("-- ServerResponseError [http error code] " + connection.getResponseCode());
                EasyLog.LogMessage("-- ServerResponseError [http error Message] " + connection.getResponseMessage());

                String responseData = "";
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream()));

                while (true) {
                    String stringLine = bufferedReader.readLine();
                    if (stringLine == null) break;

                    responseData += (stringLine + '\n');
                }

                convertRawServerData2Log(responseData);

                bufferedReader.close();
                connection.getErrorStream().close();

                EasyLog.LogMessage("-- ServerResponseError [http error Message] " + responseData);

                ServerResponseError serverResponseError = new ServerResponseError();
                serverResponseError.setErrorMessage(HttpConnectionError);

                throw serverResponseError;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();

            ParsingException parsingException = new ParsingException();
            parsingException.setErrorMessage(ParsingError);

            throw parsingException;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return instance;
    }

    public String getUrlData() {
        return mUrlData;
    }

    public void setUrlData(String urlData) {
        this.mUrlData = urlData;
    }

    public String getHttpMethod() {
        return mHttpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.mHttpMethod = httpMethod;
    }

    public ContentValues getRequestHttpHeader() {
        return mRequestHeader;
    }

    public void setRequestHttpHeader(ContentValues requestHeader) {
        this.mRequestHeader = requestHeader;
    }

    public ContentValues getParameterValues() {
        return mParameterValues;
    }

    public void setParameterValues(ContentValues parameterValues) {
        this.mParameterValues = parameterValues;
    }

    public BaseRequest getRequestCommand() {
        return mRequestCommand;
    }

    public void setRequestCommandTag(BaseRequest request) {
        this.mRequestCommand = request;
    }

    public String getDownloadPath() {
        return mDownloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.mDownloadPath = downloadPath;
    }

    private String convertParameter() throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();

        if (mParameterValues != null) {
            for (Map.Entry<String , Object> entry : mParameterValues.valueSet()) {
                String parameterName = URLEncoder.encode(entry.getKey() , "UTF-8");

                if (entry.getValue() != null) {
                    String parameterValue = URLEncoder.encode(entry.getValue().toString() , "UTF-8");
                    String parameter = parameterName + "=" + parameterValue;

                    if (TextUtils.isEmpty(stringBuilder.toString())) {
                        stringBuilder.append(parameter);
                    } else {
                        stringBuilder.append("&"+parameter);
                    }
                }
            }
            EasyLog.LogMessage("*********************************************************************");
            EasyLog.LogMessage(">> URL :" + mUrlData);
            EasyLog.LogMessage(">> parameter :"+stringBuilder.toString());
            EasyLog.LogMessage("*********************************************************************");
        }
        return stringBuilder.toString();
    }

    private void convertRawServerData2Log(String responseData) {
        EasyLog.LogMessage("*********************************************************************");
        EasyLog.LogMessage(">> URL :"+mUrlData);
        EasyLog.LogMessage(">> responseData :"+responseData);
        EasyLog.LogMessage("*********************************************************************");
    }

    private String convertStringFromJSON(JSONObject jsonObject , String jsonField) {
        try {
            return jsonObject.getString(jsonField);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isValidHttpConnection(int httpResponseCode) {
        switch (httpResponseCode) {
            case HttpURLConnection.HTTP_OK:
            case HttpURLConnection.HTTP_CREATED:
                return true;

            default:
                return false;
        }
    }
}
