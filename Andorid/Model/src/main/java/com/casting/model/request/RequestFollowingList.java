package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.model.FollowingVectorList;
import com.casting.model.Member;

import org.json.JSONObject;

public class RequestFollowingList extends NetworkRequest implements JSONParcelable<FollowingVectorList> {

    @Override
    public FollowingVectorList parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        FollowingVectorList followingVectorList = null;

        return followingVectorList;
    }

    public enum FollowingVector{FOLLOWING, FOLLOWER}

    private FollowingVector mFollowingVector;

    @Override
    public String getHttpMethod() {
        return HttpGet;
    }

    @Override
    public ContentValues getHttpRequestHeader()
    {
        String token = CommonPreference.getInstance().getSharedValueByString("AUTH_TOKEN", "");

        ContentValues contentValues = new ContentValues();
        contentValues.put("authorization", token);

        return contentValues;
    }

    @Override
    public ContentValues getHttpRequestParameter() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable getNetworkParcelable() {
        return this;
    }

    public RequestFollowingList.FollowingVector getFollowingVector() {
        return mFollowingVector;
    }

    public void setFollowingVector(RequestFollowingList.FollowingVector vector) {
        mFollowingVector = vector;
    }
}
