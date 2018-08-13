package com.casting.commonmodule.network.base;

import com.casting.commonmodule.model.BaseResponse;

public class NetworkResponse extends BaseResponse {

    private String mResponseMessage;

    public String getResponseMessage() {
        return mResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        mResponseMessage = responseMessage;
    }
}
