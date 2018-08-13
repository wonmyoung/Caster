package com.casting.commonmodule.model;

import com.casting.commonmodule.thread.ThreadResponseListener;

public abstract class BaseRequest<M extends BaseModel> {

    private ThreadResponseListener mResponseListener;

    public abstract Class<M> getTargetClass();

    public ThreadResponseListener getResponseListener() {
        return mResponseListener;
    }

    public void setResponseListener(ThreadResponseListener responseListener) {
        this.mResponseListener = responseListener;
    }
}
