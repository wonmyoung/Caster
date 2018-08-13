package com.casting.commonmodule.network.base;

import com.casting.commonmodule.model.BaseRequest;

public interface URLGeneratorStrategy {

    String create(BaseRequest request);
}
