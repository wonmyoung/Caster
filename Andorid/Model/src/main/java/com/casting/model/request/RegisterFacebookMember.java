package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.session.SessionType;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Member;

import org.json.JSONObject;

public class RegisterFacebookMember extends NetworkRequest implements JSONParcelable<Member> {

    private Member  FacebookMember;

    private boolean Response = false;
    private String  ResponseMessage;

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
        contentValues.put("email", FacebookMember.getEmail());
        contentValues.put("userId", FacebookMember.getUserId());
        contentValues.put("password", FacebookMember.getPassWord());
        contentValues.put("password2", FacebookMember.getPassWord());

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

        if (FacebookMember != null && Response) {
            FacebookMember.setAuthToken(token);
        }

        EasyLog.LogMessage(this, "++ parse jsonObject = " + jsonObject.toString());
        EasyLog.LogMessage(this, "++ parse Response = " + Response);
        EasyLog.LogMessage(this, "++ parse ResponseMessage = " + ResponseMessage);
        EasyLog.LogMessage(this, "++ parse error = " + error);
        EasyLog.LogMessage(this, "++ parse token = " + token);

        return FacebookMember;
    }

    public boolean isResponse() {
        return Response;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public Member getFacebookMember() {
        return FacebookMember;
    }

    public void setFacebookMember(Member facebookMember) {
        FacebookMember = facebookMember;
    }
}
