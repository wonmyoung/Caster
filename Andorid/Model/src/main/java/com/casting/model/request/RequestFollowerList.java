package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.Member;

public class RequestFollowerList extends NetworkRequest {

    public enum FollowingVector
    {FOLLOWING, FOLLOWER}

    private FollowingVector mFollowingVector;

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

    public void setMember(Member member) {
        this.mMember = member;
    }

    public RequestFollowerList.FollowingVector getFollowingVector() {
        return mFollowingVector;
    }

    public void setFollowingVector(RequestFollowerList.FollowingVector vector) {
        mFollowingVector = vector;
    }
}
