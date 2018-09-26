package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.FollowingVector;
import com.casting.model.FollowingVectorList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestFollowingList extends NetworkRequest implements JSONParcelable<FollowingVectorList> {

    private int RequestItemType;

    @Override
    public FollowingVectorList parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        JSONArray followerInfo = UtilityData.convertJsonArrayFromJson(jsonObject, "followerInfo");

        FollowingVectorList followingVectorList = new FollowingVectorList();

        if (followerInfo != null)
        {
            int size = followerInfo.length();

            for (int i = 0 ; i < size ; i++)
            {
                try
                {
                    JSONObject info = followerInfo.getJSONObject(i);

                    String userName = UtilityData.convertStringFromJSON(info, "username");
                    String userId = UtilityData.convertStringFromJSON(info, "userId");
                    String avatar = UtilityData.convertStringFromJSON(info, "avatar");

                    FollowingVector followingVector = new FollowingVector();
                    followingVector.setItemType(RequestItemType);
                    followingVector.setUserId(userId);
                    followingVector.setUserName(userName);
                    followingVector.setAvatar(avatar);

                    followingVectorList.addFollowingVector(followingVector);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return followingVectorList;
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
    public JSONParcelable getNetworkParcelable()
    {
        return this;
    }

    public int getRequestItemType() {
        return RequestItemType;
    }

    public void setRequestItemType(int requestItemType) {
        RequestItemType = requestItemType;
    }
}
