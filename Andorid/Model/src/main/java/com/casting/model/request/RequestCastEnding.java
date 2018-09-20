package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Alarm;
import com.casting.model.Cast;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestCastEnding extends NetworkRequest implements JSONParcelable<Alarm> {

    private String SurveyId;

    @Override
    public Alarm parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse JSONObject = " + jsonObject.toString());

        String surveyId = UtilityData.convertStringFromJSON(jsonObject, "surveyId");
        String endDate = UtilityData.convertStringFromJSON(jsonObject, "endDate");
        JSONArray friends = UtilityData.convertJsonArrayFromJson(jsonObject, "friends");

        Alarm alarm = new Alarm();
        alarm.setSurveyId(surveyId);
        alarm.setEndDate(endDate);

        return alarm;
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
    public JSONParcelable<Alarm> getNetworkParcelable() {
        return this;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }
}
