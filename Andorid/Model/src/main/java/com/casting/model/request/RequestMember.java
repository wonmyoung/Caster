package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Member;

import org.json.JSONObject;

public class RequestMember extends NetworkRequest implements JSONParcelable<Member> {

    @Override
    public Member parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        String userName = UtilityData.convertStringFromJSON(jsonObject, "username");
        String userId = UtilityData.convertStringFromJSON(jsonObject, "userId");
        String email = UtilityData.convertStringFromJSON(jsonObject, "email");
        String avatar = UtilityData.convertStringFromJSON(jsonObject, "avatar");
        String level = UtilityData.convertStringFromJSON(jsonObject, "level");
        int point = UtilityData.convertIntegerFromJSON(jsonObject, "point");
        int followers = UtilityData.convertIntegerFromJSON(jsonObject, "followers");
        int following = UtilityData.convertIntegerFromJSON(jsonObject, "following");

        Member member = new Member();
        member.setUserName(userName);
        member.setUserId(userId);
        member.setEmail(email);
        member.setUserPicThumbnail(avatar);
        member.setUserLevel(level);
        member.setUserPoint(point);
        member.setFollowerNum(followers);
        member.setFollowingNum(following);

        return member;
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
}
