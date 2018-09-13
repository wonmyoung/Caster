package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.News;
import com.casting.model.NewsList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestNewsList extends NetworkRequest implements JSONParcelable<NewsList> {

    private String CastId;

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
    public JSONParcelable<NewsList> getNetworkParcelable()
    {
        return this;
    }

    @Override
    public NewsList parse(JSONObject jsonObject)
    {
        NewsList newsList = new NewsList();

        JSONArray jsonArray = UtilityData.convertJsonArrayFromJson(jsonObject, "articleInfo");

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
                    String title = UtilityData.convertStringFromJSON(o, "title");
                    String contents = UtilityData.convertStringFromJSON(o, "contents");
                    String link = UtilityData.convertStringFromJSON(o, "link");
                    String thumbnail = UtilityData.convertStringFromJSON(o, "thumbnail");
                    String updated_at = UtilityData.convertStringFromJSON(o, "updated_at");
                    String created_at = UtilityData.convertStringFromJSON(o, "created_at");

                    EasyLog.LogMessage(this, "++ parse id = " + id);
                    EasyLog.LogMessage(this, "++ parse surveyId = " + surveyId);
                    EasyLog.LogMessage(this, "++ parse title = " + title);
                    EasyLog.LogMessage(this, "++ parse contents = " + contents);
                    EasyLog.LogMessage(this, "++ parse link = " + link);
                    EasyLog.LogMessage(this, "++ parse thumbnail = " + thumbnail);
                    EasyLog.LogMessage(this, "++ parse updated_at = " + updated_at);
                    EasyLog.LogMessage(this, "++ parse created_at = " + created_at);

                    News news = new News();
                    news.setId(id);
                    news.setSurveyId(surveyId);
                    news.setNewsTitle(title);
                    news.setContents(contents);
                    news.setLink(link);
                    news.setThumbnail(thumbnail);
                    news.setCreated_at(created_at);
                    news.setUpdated_at(updated_at);

                    newsList.addNews(news);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return newsList;
    }

    public String getCastId() {
        return CastId;
    }

    public void setCastId(String castId) {
        CastId = castId;
    }
}
