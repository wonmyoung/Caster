package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.Cast;

import org.json.JSONObject;

public class RequestDetailedCast extends NetworkRequest implements JSONParcelable<Cast> {

    private Cast mCast;

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
    public Cast parse(JSONObject jsonObject) {
        return null;
    }

    public Cast getCast() {
        return mCast;
    }

    public void setCast(Cast c) {
        this.mCast = c;
    }
}
