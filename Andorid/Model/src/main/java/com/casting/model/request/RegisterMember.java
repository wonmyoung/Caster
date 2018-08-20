package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.session.SessionType;
import com.casting.model.Member;

import org.json.JSONObject;

public class RegisterMember extends NetworkRequest implements JSONParcelable<Member> {

    private String NickName;
    private String EmailAddress;
    private String Password;

    private SessionType     mSessionType;

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
    public JSONParcelable<Member> getNetworkParcelable() {
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

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
