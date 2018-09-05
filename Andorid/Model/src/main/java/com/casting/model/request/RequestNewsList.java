package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.NewsList;

import org.json.JSONObject;

public class RequestNewsList extends NetworkRequest implements JSONParcelable<NewsList> {

    private String CastId;

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
    public JSONParcelable<NewsList> getNetworkParcelable()
    {
        return null;
    }

    @Override
    public NewsList parse(JSONObject jsonObject)
    {
        return null;
    }

    public String getCastId() {
        return CastId;
    }

    public void setCastId(String castId) {
        CastId = castId;
    }
}
