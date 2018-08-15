package com.casting.commonmodule.model;

import com.casting.commonmodule.IResponseListener;

public abstract class BaseRequest<M extends BaseModel> {

    private IResponseListener mResponseListener;

    private boolean mNetworkRequest;

    public abstract Class<M> getTargetClass();

    public IResponseListener getResponseListener() {
        return mResponseListener;
    }

    public void setResponseListener(IResponseListener responseListener) {
        this.mResponseListener = responseListener;
    }

    public <R extends BaseRequest> boolean isRight(Class<R> rClass) {
        return getClass().equals(rClass);
    }

    public boolean isNetworkRequest() {
        return mNetworkRequest;
    }

    public void setNetworkRequest(boolean networkRequest) {
        mNetworkRequest = networkRequest;
    }
}
