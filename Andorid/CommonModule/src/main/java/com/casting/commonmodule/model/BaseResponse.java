package com.casting.commonmodule.model;

public abstract class BaseResponse<M extends BaseModel> {

    private int mResponseCode;

    private BaseRequest mSourceRequest;

    private M mResponseModel;

    public M getResponseModel() {
        return mResponseModel;
    }

    public void setResponseModel(M m) {
        mResponseModel = m;
    }

    public BaseRequest getSourceRequest() {
        return mSourceRequest;
    }

    public void setSourceRequest(BaseRequest sourceRequest) {
        mSourceRequest = sourceRequest;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int responseCode) {
        mResponseCode = responseCode;
    }
}
