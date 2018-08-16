package com.casting.commonmodule.network.parse;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.network.base.NetworkParcelable;

public interface XMLParcelable<M extends BaseModel> extends NetworkParcelable<String, M> {

    @Override
    M parse(String s);
}
