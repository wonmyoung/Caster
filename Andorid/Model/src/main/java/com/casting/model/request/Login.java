package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.session.SessionType;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Member;

import org.json.JSONObject;

public class Login extends NetworkRequest implements JSONParcelable<Member> {

    private String EmailAddress;
    private String Password;

    private SessionType mSessionType;

    private boolean SuccessResponse = false;

    @Override
    public String getHttpMethod()
    {
        return HttpPost;
    }

    @Override
    public ContentValues getHttpRequestHeader()
    {
        return null;
    }

    @Override
    public ContentValues getHttpRequestParameter()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", EmailAddress);
        contentValues.put("password", Password);

        return contentValues;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable<Member> getNetworkParcelable()
    {
        return this;
    }

    @Override
    public Member parse(JSONObject jsonObject)
    {
        SuccessResponse = UtilityData.convertBooleanFromJSON(jsonObject, "success");

        EasyLog.LogMessage(this, "++ parse jsonObject = " + jsonObject.toString());
        EasyLog.LogMessage(this, "++ parse SuccessResponse = " + SuccessResponse);

        if (SuccessResponse)
        {
            String token = UtilityData.convertStringFromJSON(jsonObject, "token");

            Member member = new Member();
            member.setEmail(EmailAddress);
            member.setPassWord(Password);
            member.setAuthToken(token);

            return member;
        }

        return null;
    }

    public SessionType getSessionType() {
        return mSessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.mSessionType = sessionType;
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

    public boolean isSuccessResponse() {
        return SuccessResponse;
    }
}
