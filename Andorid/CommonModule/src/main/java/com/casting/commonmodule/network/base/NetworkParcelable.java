package com.casting.commonmodule.network.base;

import com.casting.commonmodule.model.BaseModel;

public interface NetworkParcelable<R , M extends BaseModel> {

    M parse(R r);
}
