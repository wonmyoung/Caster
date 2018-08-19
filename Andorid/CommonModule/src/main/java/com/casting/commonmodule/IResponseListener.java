package com.casting.commonmodule;

import com.casting.commonmodule.model.BaseResponse;

public interface IResponseListener<R extends BaseResponse> {

    void onThreadResponseListen(R response);
}
