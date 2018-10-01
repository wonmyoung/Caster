package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Member;

import org.json.JSONObject;


public class RequestAccountsModify extends NetworkRequest implements JSONParcelable<Member> {

    private Member  TargetMember;

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
    public JSONParcelable<Member> getNetworkParcelable()
    {
        return this;
    }

    @Override
    public Member parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        JSONObject userInfo = UtilityData.convertJsonFromJson(jsonObject, "userInfo");

        if (TargetMember != null && userInfo != null)
        {
            String email = UtilityData.convertStringFromJSON(userInfo, "email");
            String userName = UtilityData.convertStringFromJSON(userInfo, "username");
            String avatar = UtilityData.convertStringFromJSON(userInfo, "avatar");
            String gender = UtilityData.convertStringFromJSON(userInfo, "gender");
            String birth = UtilityData.convertStringFromJSON(userInfo, "birth");
            String residence = UtilityData.convertStringFromJSON(userInfo, "residence");
            String job = UtilityData.convertStringFromJSON(userInfo, "job");
            String phone = UtilityData.convertStringFromJSON(userInfo, "phone");

            EasyLog.LogMessage(this, "++ parse email = " + email);
            EasyLog.LogMessage(this, "++ parse userName = " + userName);
            EasyLog.LogMessage(this, "++ parse avatar = " + avatar);
            EasyLog.LogMessage(this, "++ parse gender = " + gender);
            EasyLog.LogMessage(this, "++ parse birth = " + birth);
            EasyLog.LogMessage(this, "++ parse residence = " + residence);
            EasyLog.LogMessage(this, "++ parse job = " + job);
            EasyLog.LogMessage(this, "++ parse phone = " + phone);

            TargetMember.setEmail(email);
            TargetMember.setUserName(userName);
            TargetMember.setUserAvatar(avatar);
            TargetMember.setUserGender(gender);
            TargetMember.setUserBirthTime(birth);
            TargetMember.setUserResidence(residence);
            TargetMember.setUserOccupation(job);

        }

        return TargetMember;
    }

    public Member getTargetMember() {
        return TargetMember;
    }

    public void setTargetMember(Member targetMember) {
        TargetMember = targetMember;
    }
}
