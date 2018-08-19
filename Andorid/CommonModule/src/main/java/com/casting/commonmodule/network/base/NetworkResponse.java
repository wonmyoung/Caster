package com.casting.commonmodule.network.base;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.network.exception.NetworkException;

public class NetworkResponse<M extends BaseModel> extends BaseResponse<M> {

    private NetworkException    mNetworkException;

    public NetworkException getNetworkException() {
        return mNetworkException;
    }

    public void setNetworkException(NetworkException e) {
        this.mNetworkException = e;
    }

}
