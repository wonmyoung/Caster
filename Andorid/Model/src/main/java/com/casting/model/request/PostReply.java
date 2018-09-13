package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.Reply;

import org.json.JSONObject;

public class PostReply extends NetworkRequest implements JSONParcelable<Reply> {

    @Override
    public Reply parse(JSONObject jsonObject) {
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
    public JSONParcelable<Reply> getNetworkParcelable() {
        return this;
    }
}
