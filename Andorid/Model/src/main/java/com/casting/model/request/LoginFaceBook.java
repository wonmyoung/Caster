package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.session.SessionType;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Member;

import org.json.JSONObject;

public class LoginFaceBook extends NetworkRequest implements JSONParcelable<Member> {

    private Member      FaceBookMember;

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
        contentValues.put("email", FaceBookMember.getEmail());
        contentValues.put("password", FaceBookMember.getPassWord());

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

            FaceBookMember.setAuthToken(token);
        }

        return FaceBookMember;
    }

    public boolean isSuccessResponse() {
        return SuccessResponse;
    }

    public Member getFaceBookMember() {
        return FaceBookMember;
    }

    public void setFaceBookMember(Member faceBookMember) {
        FaceBookMember = faceBookMember;
    }
}
