package com.casting.commonmodule.network.base;

import org.json.JSONObject;

public interface INetworkJSONParcelable {

    void parseJSON2Data(JSONObject jsonObject);
}
