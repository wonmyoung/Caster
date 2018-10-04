package com.casting.model.request;

import android.content.ContentValues;
import android.text.TextUtils;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Member;

import org.json.JSONObject;

public class RequestMemberLatest extends NetworkRequest implements JSONParcelable<Member> {

    private Member  CurrentMember;

    @Override
    public Member parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        JSONObject userInfo = UtilityData.convertJsonFromJson(jsonObject, "userinfo");

        if (userInfo != null && CurrentMember != null)
        {
            String userName = UtilityData.convertStringFromJSON(userInfo, "username");
            String userId = UtilityData.convertStringFromJSON(userInfo, "userId");
            String email = UtilityData.convertStringFromJSON(userInfo, "email");
            String avatar = UtilityData.convertStringFromJSON(userInfo, "avatar");
            String level = UtilityData.convertStringFromJSON(userInfo, "level");
            int point = UtilityData.convertIntegerFromJSON(userInfo, "point");
            int followers = UtilityData.convertIntegerFromJSON(userInfo, "followers");
            int following = UtilityData.convertIntegerFromJSON(userInfo, "following");
            int numOfPredict = UtilityData.convertIntegerFromJSON(jsonObject, "numOfPredict");

            EasyLog.LogMessage(this, "++ parse userName = " + userName);
            EasyLog.LogMessage(this, "++ parse userId = " + userId);
            EasyLog.LogMessage(this, "++ parse email = " + email);
            EasyLog.LogMessage(this, "++ parse avatar = " + avatar);
            EasyLog.LogMessage(this, "++ parse level = " + level);
            EasyLog.LogMessage(this, "++ parse point = " + point);
            EasyLog.LogMessage(this, "++ parse followers = " + followers);
            EasyLog.LogMessage(this, "++ parse following = " + following);
            EasyLog.LogMessage(this, "++ parse numOfPredict = " + numOfPredict);

            if (!TextUtils.isEmpty(userName)) CurrentMember.setUserName(userName);
            if (!TextUtils.isEmpty(userId)) CurrentMember.setUserId(userId);
            if (!TextUtils.isEmpty(email)) CurrentMember.setEmail(email);
            if (!TextUtils.isEmpty(avatar)) CurrentMember.setUserAvatar(avatar);
            if (!TextUtils.isEmpty(level)) CurrentMember.setUserLevel(level);
            if (point > -1) CurrentMember.setUserPoint(point);
            if (followers > -1) CurrentMember.setFollowerNum(followers);
            if (following > -1) CurrentMember.setFollowingNum(following);
            if (numOfPredict > -1) CurrentMember.setCorrectCast(numOfPredict);
        }

        return CurrentMember;
    }

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

    public Member getCurrentMember() {
        return CurrentMember;
    }

    public void setCurrentMember(Member currentMember) {
        CurrentMember = currentMember;
    }
}
