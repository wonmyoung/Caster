package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Ranking;
import com.casting.model.RankingList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestRankingList extends NetworkRequest implements JSONParcelable<RankingList> {

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
    public ContentValues getHttpRequestParameter()
    {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable<RankingList> getNetworkParcelable()
    {
        return this;
    }

    @Override
    public RankingList parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject " + jsonObject.toString());

        RankingList rankingList = new RankingList();

        JSONArray userInfoList = UtilityData.convertJsonArrayFromJson(jsonObject, "userInfo");

        int length = (userInfoList != null ? userInfoList.length() : 0);
        if (length > 0)
        {

            for (int i = 0 ; i < length ; i++)
            {
                try
                {
                    JSONObject userInfo = userInfoList.getJSONObject(i);

                    EasyLog.LogMessage(this, ">> parse userInfo = " + userInfo.toString());

                    String level = UtilityData.convertStringFromJSON(userInfo, "level");
                    String userId = UtilityData.convertStringFromJSON(userInfo, "userId");
                    String username = UtilityData.convertStringFromJSON(userInfo, "username");
                    String avatar = UtilityData.convertStringFromJSON(userInfo, "avatar");
                    int reward = UtilityData.convertIntegerFromJSON(userInfo, "reward");
                    int point = UtilityData.convertIntegerFromJSON(userInfo, "point");
                    int hitRatio = UtilityData.convertIntegerFromJSON(userInfo, "hitRatio");

                    EasyLog.LogMessage(this, ">> parse level " + level);
                    EasyLog.LogMessage(this, ">> parse userId " + userId);
                    EasyLog.LogMessage(this, ">> parse username " + username);
                    EasyLog.LogMessage(this, ">> parse avatar " + avatar);
                    EasyLog.LogMessage(this, ">> parse reward " + reward);
                    EasyLog.LogMessage(this, ">> parse point " + point);
                    EasyLog.LogMessage(this, ">> parse hitRatio " + hitRatio);

                    Ranking ranking = new Ranking();
                    ranking.setLevel(level);
                    ranking.setUserId(userId);
                    ranking.setUserName(username);
                    ranking.setAvatar(avatar);
                    ranking.setReward(reward);
                    ranking.setPoint(point);
                    ranking.setHitRatio(hitRatio);

                    rankingList.addRanking(ranking);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return rankingList;
    }
}
