package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.model.AlarmList;
import com.casting.model.Member;

import org.json.JSONObject;

public class RequestAlarmList extends NetworkRequest implements JSONParcelable<AlarmList> {

    private Member  mMember;

    @Override
    public String getHttpMethod()
    {
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
    public JSONParcelable<AlarmList> getNetworkParcelable() {
        return this;
    }

    public Member getMember() {
        return mMember;
    }

    public void setmMember(Member m) {
        this.mMember = m;
    }

    @Override
    public AlarmList parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        return null;
    }
}
