package com.casting.commonmodule;

import com.casting.commonmodule.model.BaseRequest;

public interface IRequestHandler<R extends BaseRequest> {

    void request(R r);
}
