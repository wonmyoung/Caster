package com.casting.commonmodule;

import com.casting.commonmodule.model.BaseResponse;

public interface IResponseListener {

    void onThreadResponseListen(BaseResponse response);
}
