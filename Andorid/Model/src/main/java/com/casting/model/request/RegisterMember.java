package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.session.SessionType;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.model.Member;

import org.json.JSONObject;

public class RegisterMember extends NetworkRequest implements JSONParcelable<Member> {

    private String NickName;
    private String UserId;
    private String EmailAddress;
    private String Password;

    private boolean Response = false;
    private String  ResponseMessage;

    private SessionType     mSessionType;

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
        contentValues.put("userId", UserId);
        contentValues.put("password", Password);
        contentValues.put("password2", Password);

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

        Response = UtilityData.convertBooleanFromJSON(jsonObject, "success");
        ResponseMessage = UtilityData.convertStringFromJSON(jsonObject, "message");

        String error = UtilityData.convertStringFromJSON(jsonObject, "error");
        String token = UtilityData.convertStringFromJSON(jsonObject, "token");

        EasyLog.LogMessage(this, "++ parse jsonObject = " + jsonObject.toString());
        EasyLog.LogMessage(this, "++ parse Response = " + Response);
        EasyLog.LogMessage(this, "++ parse ResponseMessage = " + ResponseMessage);
        EasyLog.LogMessage(this, "++ parse error = " + error);
        EasyLog.LogMessage(this, "++ parse token = " + token);

        Member member = null;

        if (Response)
        {
            member = new Member();
            member.setUserId(UserId);
            member.setPassWord(Password);
            member.setEmail(EmailAddress);
            member.setAuthToken(token);
        }

        return member;
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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public boolean isResponse() {
        return Response;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }
}
