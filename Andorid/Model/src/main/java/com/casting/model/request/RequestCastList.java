package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.CastList;

import org.json.JSONObject;

public class RequestCastList extends NetworkRequest implements JSONParcelable<CastList> {

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

    @Override
    public NetworkParcelable getNetworkParcelable() {
        return this;
    }

    @Override
    public CastList parse(JSONObject jsonObject) {
        return null;
    }
}
