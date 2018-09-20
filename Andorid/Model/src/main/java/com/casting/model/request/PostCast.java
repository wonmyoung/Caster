package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.model.Cast;

import org.json.JSONObject;

public class PostCast extends NetworkRequest implements JSONParcelable<Cast> {

    private String      Predict;
    private String      SurveyId;
    private String      Comment;
    private String      Bet;
    private String      SurveyTitle;

    @Override
    public String getHttpMethod() {
        return HttpPost;
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
        ContentValues contentValues = new ContentValues();
        contentValues.put("predict", Predict);
        contentValues.put("comment", Comment);
        contentValues.put("bet", Bet);
        contentValues.put("id", SurveyId);
        contentValues.put("title", SurveyTitle);

        return contentValues;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable<Cast> getNetworkParcelable() {
        return this;
    }

    @Override
    public Cast parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        return null;
    }

    public String getPredict() {
        return Predict;
    }

    public void setPredict(String predict) {
        Predict = predict;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getBet() {
        return Bet;
    }

    public void setBet(String bet) {
        Bet = bet;
    }

    public String getSurveyTitle() {
        return SurveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        SurveyTitle = surveyTitle;
    }
}
