package com.casting.commonmodule.model;

import com.casting.commonmodule.IResponseListener;

public abstract class BaseRequest<R extends BaseResponse> {

    private IResponseListener<R> mResponseListener;

    public IResponseListener<R> getResponseListener() {
        return mResponseListener;
    }

    public void setResponseListener(IResponseListener<R> responseListener) {
        this.mResponseListener = responseListener;
    }

    public <B extends BaseRequest> boolean isRight(Class<B> rClass) {
        return getClass().equals(rClass);
    }
}
