package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.CastingStatus;

import org.json.JSONObject;

public class RequestCastingStatus extends NetworkRequest implements JSONParcelable<CastingStatus> {

    @Override
    public CastingStatus parse(JSONObject jsonObject) {
        return null;
    }

    @Override
    public String getHttpMethod() {
        return null;
    }

    @Override
    public ContentValues getHttpRequestHeader() {
        return null;
    }

    @Override
    public ContentValues getHttpRequestParameter() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable<CastingStatus>  getNetworkParcelable() {
        return this;
    }
}
