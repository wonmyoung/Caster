package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.model.Cast;
import com.casting.model.Member;
import com.casting.model.TimeLineList;

import org.json.JSONObject;

public class RequestTimeLineList extends NetworkRequest implements JSONParcelable<TimeLineList> {

    private Member  mMember;

    private Cast    mCast;

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
    public ContentValues getHttpRequestParameter()
    {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable getNetworkParcelable()
    {
        return this;
    }

    public Member getMember() {
        return mMember;
    }

    public void setMember(Member member) {
        this.mMember = member;
    }

    @Override
    public TimeLineList parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        TimeLineList timeLineList = new TimeLineList();


        return timeLineList;
    }

    public Cast getCast() {
        return mCast;
    }

    public void setCast(Cast c) {
        this.mCast = c;
    }
}
