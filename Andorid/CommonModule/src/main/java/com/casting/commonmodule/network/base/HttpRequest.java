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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class HttpRequest<M extends BaseModel> implements NetworkConstant {

    private final static int DEFAULT_TIMEOUT = 5 * 1000;

    private final static String TWO_HYPENS  = "--";
    private final static String BOUNDARY    = "*************************";
    private final static String LINE_END    = "\r\n";
    private final static int MAX_BUFFER_SIZE = 1024 * 1024;


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

            if (HttpGet.equalsIgnoreCase(mHttpMethod))
            {
                EasyLog.LogMessage(this, "++ Http Get network execute ");

                strUrlBuilder.append(buildParameter());
            }

            URL url = new URL(strUrlBuilder.toString());
            EasyLog.LogMessage(this, "++ Http method = " + mHttpMethod);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setRequestMethod(mHttpMethod);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            if (mRequestHeader != null)
            {
                for (String key : mRequestHeader.keySet())
                {
                    String v = mRequestHeader.getAsString(key);

                    connection.setRequestProperty(key , v);
                }
            }
            connection.setDoOutput(mHttpMethod.equalsIgnoreCase(HttpPost));
            connection.setDoInput(true);
            connection.connect();

            if (HttpPost.equalsIgnoreCase(mHttpMethod))
            {
                EasyLog.LogMessage(this, "++ Http Post network execute ");

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                if (mNetworkRequest instanceof IFileUpLoader)
                {
                    IFileUpLoader fileUpLoader = (IFileUpLoader) mNetworkRequest;

                    String filePath = fileUpLoader.getFilePath();
                    String fileMimeType = fileUpLoader.getFileMimeType();
                    String fileField = fileUpLoader.getFileField();

                    String[] q = filePath.split("/");
                    int id = q.length - 1;

                    connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

                    outputStream.writeBytes(TWO_HYPENS + BOUNDARY + LINE_END);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileField + "\"; filename=\"" + q[id] + "\"" + LINE_END);
                    outputStream.writeBytes("Content-Type: " + fileMimeType + LINE_END);
                    outputStream.writeBytes("Content-Transfer-Encoding: binary" + LINE_END);
                    outputStream.writeBytes(LINE_END);

                    File file = new File(filePath);

                    FileInputStream fileInputStream = new FileInputStream(file);

                    int bytesAvailable = fileInputStream.available();
                    int bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);

                    byte[] buffer = new byte[bufferSize];

                    int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0)
                    {
                        outputStream.write(buffer, 0, bufferSize);

                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    outputStream.writeBytes(LINE_END);

                    fileInputStream.close();
                }

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(buildParameter());
                bufferedWriter.flush();
                bufferedWriter.close();

                outputStream.close();
            }

            if (isValidHttpConnection(connection.getResponseCode()))
            {

                List<String> cookies = (connection.getHeaderFields() != null ?
                                        connection.getHeaderFields().get("Set-Cookie") : null);
                int cookiesSize = (cookies == null ? 0 : cookies.size());
                if (cookiesSize > 0)
                {
                    for (String s : cookies)
                    {
                        EasyLog.LogMessage(this, "++ cookie = " + s);
                    }
                }

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

                printServerLog(responseBuilder.toString());

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

    private String buildParameter() throws UnsupportedEncodingException
    {
        StringBuilder stringBuilder = new StringBuilder();

        if (mParameterValues != null)
        {

            for (Map.Entry<String , Object> entry : mParameterValues.valueSet())
            {

                String parameterName = URLEncoder.encode(entry.getKey() , "UTF-8");

                Object o = entry.getValue();

                if (o != null)
                {
                    String parameterValue = URLEncoder.encode(o.toString() , "UTF-8");

                    if (!TextUtils.isEmpty(stringBuilder.toString()))
                    {
                        stringBuilder.append("&");
                    }

                    stringBuilder.append(parameterName);
                    stringBuilder.append("=");
                    stringBuilder.append(parameterValue);
                }
            }

            EasyLog.LogMessage("*********************************************************************");
            EasyLog.LogMessage(">> URL :" ,mUrlData);
            EasyLog.LogMessage(">> parameter :", stringBuilder.toString());
            EasyLog.LogMessage("*********************************************************************");
        }

        return stringBuilder.toString();
    }

    private void printServerLog(String responseData) {
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
