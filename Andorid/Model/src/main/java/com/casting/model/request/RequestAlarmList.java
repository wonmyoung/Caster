package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.Member;

public class RequestAlarmList extends NetworkRequest {

    private Member  mMember;

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
    public JSONParcelable getNetworkParcelable() {
        return null;
    }

    public Member getMember() {
        return mMember;
    }

    public void setmMember(Member m) {
        this.mMember = m;
    }
}
