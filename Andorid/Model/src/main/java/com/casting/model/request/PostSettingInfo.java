package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;

public class PostSettingInfo extends NetworkRequest {

    @Override
    public String getHttpMethod() {
        return HttpPut;
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
    public <P extends NetworkParcelable> P getNetworkParcelable() {
        return null;
    }
}
