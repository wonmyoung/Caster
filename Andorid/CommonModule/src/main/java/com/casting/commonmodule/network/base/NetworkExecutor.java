package com.casting.commonmodule.network.base;

import android.os.AsyncTask;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.network.NetworkStatus;
import com.casting.commonmodule.network.exceptions.NetworkException;
import com.casting.commonmodule.network.exceptions.NetworkExceptionEnum;
import com.casting.commonmodule.network.exceptions.NetworkNotAvailable;
import com.casting.commonmodule.network.exceptions.ParsingException;
import com.casting.commonmodule.network.exceptions.ServerResponseError;


public class NetworkExecutor<M extends BaseModel> extends AsyncTask<Object , Integer , M> {

    private static final String NETWORK_ERROR_MESSAGE = "네트워크가 상태 확인 필요";

    private BaseHttpRequester<M>    mNetworkRequester;
    private NetworkException mWorkerException = null;

    private INetworkProgressView mProgressView;
    private INetworkResponseListener mNetworkResponseListener;

    @Override
    protected M doInBackground(Object ... os) {
        try {

            if (NetworkStatus.getInstance().isNetworkAvailable())
            {
                publishProgress();

                return mNetworkRequester.execute();
            }
            else
            {
                NetworkNotAvailable networkNotAvailable = new NetworkNotAvailable();
                networkNotAvailable.setErrorMessage(NETWORK_ERROR_MESSAGE);

                throw networkNotAvailable;
            }

        }
        catch (NetworkNotAvailable | ServerResponseError | ParsingException e)
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

        if (mWorkerException == null)
        {
            NetworkResponse networkResponse = new NetworkResponse();
            networkResponse.setSourceRequest(mNetworkRequester.getRequestCommand());
            networkResponse.setResponseCode(1);
            networkResponse.setResponseModel(m);

            if (mNetworkResponseListener != null) {
                mNetworkResponseListener.onNetworkResponse(networkResponse);
            }
        }
        else
        {
            onResponseError(mWorkerException);
        }
    }

    private void onResponseError(NetworkException workerException) {

        NetworkExceptionEnum networkExceptions = workerException.getNetworkExceptions();

        NetworkResponse networkResponse = new NetworkResponse();
        networkResponse.setSourceRequest(mNetworkRequester.getRequestCommand());
        networkResponse.setResponseCode(networkExceptions.getErrorCode());
        networkResponse.setResponseMessage(networkExceptions.getErrorMessage());

        if (mNetworkResponseListener != null) {
            mNetworkResponseListener.onNetworkResponse(networkResponse);
        }
    }

    public BaseHttpRequester getNetworkRequestor() {
        return mNetworkRequester;
    }

    public void setNetworkRequestor(BaseHttpRequester httpRequester) {
        this.mNetworkRequester = httpRequester;
    }

    public INetworkResponseListener getNetworkResponseListener() {
        return mNetworkResponseListener;
    }

    public void setNetworkResponseListener(INetworkResponseListener mNetworkResponseListener) {
        this.mNetworkResponseListener = mNetworkResponseListener;
    }

    public NetworkException getWorkerException() {
        return mWorkerException;
    }

    public void setWorkerException(NetworkException workerException) {
        this.mWorkerException = workerException;
    }

    public void setProgressView(INetworkProgressView progressView) {
        this.mProgressView = progressView;
    }
}
