package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.ReplyList;
import com.casting.model.TimeLine;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestReplyList extends NetworkRequest implements JSONParcelable<ReplyList> {

    private String      CastId;

    private TimeLine    TargetTimeLine;

    @Override
    public String getHttpMethod() {
        return HttpGet;
    }

    @Override
    public ContentValues getHttpRequestHeader() {
        return null;
    }

    @Override
    public ContentValues getHttpRequestParameter() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable<ReplyList> getNetworkParcelable() {
        return this;
    }

    @Override
    public ReplyList parse(JSONObject jsonObject) {

        ReplyList replyList = new ReplyList();

        JSONArray jsonArray = UtilityData.convertJsonArrayFromJson(jsonObject, "commentInfo");

        int length = (jsonArray != null ? jsonArray.length() : 0);
        if (length > 0)
        {
            for (int i = 0 ; i < length ; i++)
            {
                try
                {
                    JSONObject o = jsonArray.getJSONObject(i);

                    String id = UtilityData.convertStringFromJSON(o, "_id");
                    String surveyId = UtilityData.convertStringFromJSON(o, "surveyId");
                    String userId = UtilityData.convertStringFromJSON(o, "userId");
                    String updated_at = UtilityData.convertStringFromJSON(o, "updated_at");
                    String created_at = UtilityData.convertStringFromJSON(o, "created_at");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return replyList;
    }

    public TimeLine getTargetTimeLine() {
        return TargetTimeLine;
    }

    public void setTargetTimeLine(TimeLine timeLine) {
        TargetTimeLine = timeLine;
    }

    public String getCastId() {
        return CastId;
    }

    public void setCastId(String castId) {
        CastId = castId;
    }
}
