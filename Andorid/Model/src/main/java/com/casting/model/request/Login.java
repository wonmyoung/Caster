package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.session.SessionType;
import com.casting.model.Member;

import org.json.JSONObject;

public class Login extends NetworkRequest<Member> implements JSONParcelable<Member> {

    private SessionType mSessionType;

    @Override
    public String getHttpMethod() {
        return HttpGet;
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
    public Member parse(JSONObject jsonObject) {
        return null;
    }

    public SessionType getSessionType() {
        return mSessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.mSessionType = sessionType;
    }
}
