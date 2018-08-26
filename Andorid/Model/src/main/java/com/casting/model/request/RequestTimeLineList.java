package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.Member;
import com.casting.model.TimeLineList;

import org.json.JSONObject;

public class RequestTimeLineList extends NetworkRequest implements JSONParcelable<TimeLineList> {

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

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable getNetworkParcelable() {
        return null;
    }

    public Member getMember() {
        return mMember;
    }

    public void setMember(Member member) {
        this.mMember = member;
    }

    @Override
    public TimeLineList parse(JSONObject jsonObject) {
        return null;
    }
}
