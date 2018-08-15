package com.casting.commonmodule.network.base;

import org.json.JSONObject;

public interface JSONParcelable extends ResponseParcelable<JSONObject> {

    @Override
    void parse(JSONObject jsonObject);
}
