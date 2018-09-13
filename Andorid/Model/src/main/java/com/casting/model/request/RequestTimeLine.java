package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.TimeLine;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestTimeLine extends NetworkRequest implements JSONParcelable<TimeLine> {

    @Override
    public String getHttpMethod()
    {
        return null;
    }

    @Override
    public ContentValues getHttpRequestHeader()
    {
        return null;
    }

    @Override
    public ContentValues getHttpRequestParameter()
    {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable<TimeLine> getNetworkParcelable()
    {
        return this;
    }

    @Override
    public TimeLine parse(JSONObject jsonObject)
    {
        TimeLine timeLine = new TimeLine();

        String id = UtilityData.convertStringFromJSON(jsonObject, "_id");
        String surveyId = UtilityData.convertStringFromJSON(jsonObject, "surveyId");
        String userId = UtilityData.convertStringFromJSON(jsonObject, "userId");
        String updated_at = UtilityData.convertStringFromJSON(jsonObject, "updated_at");
        String created_at = UtilityData.convertStringFromJSON(jsonObject, "created_at");
        String comment = UtilityData.convertStringFromJSON(jsonObject, "comment");

        JSONArray replies = UtilityData.convertJsonArrayFromJson(jsonObject, "reply");

        timeLine.setId(id);
        timeLine.setSurveyId(surveyId);
        timeLine.setUserId(userId);
        timeLine.setUpdated_at(updated_at);
        timeLine.setCreated_at(created_at);
        timeLine.setComments(comment);

        int repliesSize = (replies == null ? 0 : replies.length());
        if (repliesSize > 0)
        {
            for (int i = 0 ; i < repliesSize; i ++)
            {
                try
                {
                    JSONObject o = replies.getJSONObject(i);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return timeLine;
    }
}
