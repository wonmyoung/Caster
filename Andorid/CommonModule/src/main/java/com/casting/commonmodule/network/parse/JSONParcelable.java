package com.casting.commonmodule.network.parse;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.network.base.NetworkParcelable;

import org.json.JSONObject;

public interface JSONParcelable<M extends BaseModel> extends NetworkParcelable<JSONObject, M> {

    @Override
    M parse(JSONObject jsonObject);
}
