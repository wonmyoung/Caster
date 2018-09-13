package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.News;

import org.json.JSONObject;

public class RequestNews extends NetworkRequest implements JSONParcelable<News> {

    private String SurveyId;

    @Override
    public String getHttpMethod() {
        return null;
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
    public JSONParcelable<News> getNetworkParcelable() {
        return this;
    }

    @Override
    public News parse(JSONObject jsonObject)
    {
        News news = new News();



        return news;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }
}
