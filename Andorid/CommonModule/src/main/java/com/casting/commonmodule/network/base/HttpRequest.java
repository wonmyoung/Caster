package com.casting.commonmodule.network.base;

import android.content.ContentValues;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.exception.NetworkException;
import com.casting.commonmodule.network.exception.NetworkExceptionEnum;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.network.parse.XMLParcelable;
import com.casting.commonmodule.utility.EasyLog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpRequest<M extends BaseModel> implements NetworkConstant {

    private final static int DEFAULT_TIMEOUT = 5 * 1000;

    private NetworkRequest  mNetworkRequest;

    private String          mUrlData;
    private String          mHttpMethod;
    private ContentValues   mRequestHeader;
    private ContentValues   mParameterValues;

    @SuppressWarnings("unchecked")
    @WorkerThread
    public M execute() throws NetworkException, NullPointerException
    {

        M instance = null;

        HttpURLConnection connection = null;

        try
        {
            StringBuilder strUrlBuilder = new StringBuilder();
            strUrlBuilder.append(mUrlData);

            if (HttpGet.equalsIgnoreCase(mHttpMethod)) {
                strUrlBuilder.append(convertParameter());
            }

            URL url = new URL(strUrlBuilder.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setRequestMethod(mHttpMethod);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            if (getRequestHttpHeader() != null)
            {
                for (String key : getRequestHttpHeader().keySet())
                {
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

            if (isValidHttpConnection(connection.getResponseCode()))
            {

                InputStream inputStream = connection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();

                while (true) {

                    String stringLine = bufferedReader.readLine();

                    if (stringLine == null) break;

                    stringBuilder.append(stringLine).append('\n');
                }

                bufferedReader.close();

                inputStream.close();

                NetworkParcelable np = mNetworkRequest.getNetworkParcelable();

                if (np instanceof JSONParcelable)
                {
                    JSONParcelable<M> jsonParcelable = (JSONParcelable) np;

                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                    instance = jsonParcelable.parse(jsonObject);
                }
                else if (np instanceof XMLParcelable)
                {
                    XMLParcelable<M> xmlParcelable = (XMLParcelable) np;

                    instance = xmlParcelable.parse(stringBuilder.toString());
                }
            }
            else
            {

                EasyLog.LogMessage("-- ServerResponseError [http error code] " + connection.getResponseCode());
                EasyLog.LogMessage("-- ServerResponseError [http error Message] " + connection.getResponseMessage());

                StringBuilder responseBuilder = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                while (true) {

                    String stringLine = bufferedReader.readLine();

                    if (stringLine == null) break;

                    responseBuilder.append(stringLine).append('\n');
                }

                convertRawServerData2Log(responseBuilder.toString());

                bufferedReader.close();
                connection.getErrorStream().close();

                EasyLog.LogMessage("-- ServerResponseError [http error Message] " + responseBuilder.toString());

                NetworkException networkException = new NetworkException();
                networkException.setExceptionEnum(NetworkExceptionEnum.NOFOUND_PARAMETER);
                networkException.setErrorMessage(responseBuilder.toString());

                throw networkException;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();

            NetworkException networkException = new NetworkException();
            networkException.setExceptionEnum(NetworkExceptionEnum.PARSING_ERROR);
            networkException.setErrorMessage(ParsingError);

            throw networkException;

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

    public NetworkRequest getNetworkRequest() {
        return mNetworkRequest;
    }

    public void setNetworkRequest(NetworkRequest networkRequest) {
        mNetworkRequest = networkRequest;
    }

    private String convertParameter() throws UnsupportedEncodingException {

        StringBuilder stringBuilder = new StringBuilder();

        if (mParameterValues != null)
        {

            for (Map.Entry<String , Object> entry : mParameterValues.valueSet())
            {

                String parameterName = URLEncoder.encode(entry.getKey() , "UTF-8");

                if (entry.getValue() != null)
                {
                    String parameterValue = URLEncoder.encode(entry.getValue().toString() , "UTF-8");
                    String parameter = parameterName + "=" + parameterValue;

                    if (TextUtils.isEmpty(stringBuilder.toString()))
                    {
                        stringBuilder.append(parameter);
                    }
                    else
                    {
                        stringBuilder.append("&");
                        stringBuilder.append(parameter);
                    }
                }
            }

            EasyLog.LogMessage("*********************************************************************");
            EasyLog.LogMessage(">> URL :" ,mUrlData);
            EasyLog.LogMessage(">> parameter :", stringBuilder.toString());
            EasyLog.LogMessage("*********************************************************************");
        }
        return stringBuilder.toString();
    }

    private void convertRawServerData2Log(String responseData) {
        EasyLog.LogMessage("*********************************************************************");
        EasyLog.LogMessage(">> URL :", mUrlData);
        EasyLog.LogMessage(">> responseData :", responseData);
        EasyLog.LogMessage("*********************************************************************");
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
