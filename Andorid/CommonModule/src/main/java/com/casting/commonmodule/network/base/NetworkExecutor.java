package com.casting.commonmodule.network.base;

import android.os.AsyncTask;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.network.NetworkState;
import com.casting.commonmodule.network.exception.NetworkException;
import com.casting.commonmodule.network.exception.NetworkExceptionEnum;
import com.casting.commonmodule.network.NetworkRequestHandler;


public class NetworkExecutor<M extends BaseModel> extends AsyncTask<Object , Integer , M> {

    private static final String NETWORK_ERROR_MESSAGE = "네트워크가 상태 확인 필요";

    private HttpRequest<M>      mNetworkRequester;
    private NetworkException    mException = null;

    private INetworkProgressView mProgressView;

    @Override
    protected M doInBackground(Object ... os) {
        try {

            if (NetworkState.getInstance().isNetworkAvailable())
            {
                publishProgress();

                return mNetworkRequester.execute();
            }
            else
            {
                NetworkException networkException = new NetworkException();
                networkException.setExceptionEnum(NetworkExceptionEnum.NETWORK_NOT_AVAILABLE);
                networkException.setErrorMessage(NETWORK_ERROR_MESSAGE);

                throw networkException;
            }

        }
        catch (NetworkException e)
        {
            setWorkerException(e);

            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        int updateProgress = (values.length == 0 ? 1 : values[0]);

        if (mProgressView != null) {
            mProgressView.updateProgress(updateProgress);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(M m) {

        NetworkResponse networkResponse = new NetworkResponse();
        networkResponse.setSourceRequest(mNetworkRequester.getNetworkRequest());

        if (mException == null)
        {
            networkResponse.setResponseCode(1);
            networkResponse.setResponseModel(m);
        }
        else
        {
            networkResponse.setResponseCode(mException.getErrorCode());
            networkResponse.setNetworkException(mException);
        }

        NetworkRequestHandler.getInstance().receiveResponse(networkResponse);
    }

    public HttpRequest getNetworkRequestor() {
        return mNetworkRequester;
    }

    public void setNetworkRequester(HttpRequest<M> r) {
        this.mNetworkRequester = r;
    }

    public NetworkException getWorkerException() {
        return mException;
    }

    public void setWorkerException(NetworkException e) {
        this.mException = e;
    }

    public void setProgressView(INetworkProgressView progressView) {
        this.mProgressView = progressView;
    }
}
