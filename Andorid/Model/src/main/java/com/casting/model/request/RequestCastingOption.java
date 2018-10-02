package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.CastingOption;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestCastingOption extends NetworkRequest implements JSONParcelable<CastingOption> {

    private String SurveyInfoId;

    @Override
    public CastingOption parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> RequestCastingOption jsonObject = " + jsonObject.toString());

        CastingOption castingOption = null;

        JSONObject surveyInfo = UtilityData.convertJsonFromJson(jsonObject, "surveyInfo");

        JSONArray questionInfos = UtilityData.convertJsonArrayFromJson(surveyInfo, "question");

        if (questionInfos != null && questionInfos.length() > 0)
        {
            try
            {
                JSONObject questionInfo = questionInfos.getJSONObject(0);

                EasyLog.LogMessage(this, ">> RequestCastingOption surveyInfo = " + questionInfo.toString());

                String questionType = UtilityData.convertStringFromJSON(questionInfo, "questionType");
                String[] questions = UtilityData.convertStringArrayFromJSON(questionInfo, "question");

                EasyLog.LogMessage(this, ">> RequestCastingOption questionType = " + questionType);
                EasyLog.LogMessage(this, ">> RequestCastingOption questions len = " + (questions != null ? questions.length : 0));

                castingOption = new CastingOption();
                castingOption.setQuestionType(questionType);
                castingOption.setQuestions(questions);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return castingOption;
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

    public String getSurveyInfoId() {
        return SurveyInfoId;
    }

    public void setSurveyInfoId(String surveyInfoId) {
        SurveyInfoId = surveyInfoId;
    }
}
