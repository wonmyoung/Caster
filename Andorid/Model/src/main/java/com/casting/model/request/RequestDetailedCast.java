package com.casting.model.request;

import android.content.ContentValues;
import android.text.TextUtils;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Cast;
import com.casting.model.News;
import com.casting.model.NewsList;
import com.casting.model.Reply;
import com.casting.model.ReplyList;
import com.casting.model.TimeLine;
import com.casting.model.TimeLineList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestDetailedCast extends NetworkRequest implements JSONParcelable<Cast> {

    private Cast    mCast;

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
    public JSONParcelable<Cast> getNetworkParcelable()
    {
        return this;
    }

    @Override
    public Cast parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        JSONObject surveyInfo = UtilityData.convertJsonFromJson(jsonObject, "surveyInfo");

        String title = UtilityData.convertStringFromJSON(surveyInfo, "title");
        String surveyId = UtilityData.convertStringFromJSON(surveyInfo, "surveyId");
        String[] thumbNails = UtilityData.convertStringArrayFromJSON(surveyInfo, "thumbnail");
        String[] tag = UtilityData.convertStringArrayFromJSON(surveyInfo, "tag");
        String description = UtilityData.convertStringFromJSON(surveyInfo, "description");
        String startData = UtilityData.convertStringFromJSON(surveyInfo, "startData");
        String endDate = UtilityData.convertStringFromJSON(surveyInfo, "endDate");
        String reference = UtilityData.convertStringFromJSON(surveyInfo, "reference");
        String status = UtilityData.convertStringFromJSON(surveyInfo, "status");
        int totalReward = UtilityData.convertIntegerFromJSON(surveyInfo, "totalReward");
        int casterNum = UtilityData.convertIntegerFromJSON(surveyInfo, "casterNum");
        int participants = UtilityData.convertIntegerFromJSON(surveyInfo, "participants");

        NewsList newsList = parseNewsList(
                UtilityData.convertJsonArrayFromJson(jsonObject, "articleInfo"));

        TimeLineList timeLineList = parseTimeLineList(
                UtilityData.convertJsonArrayFromJson(jsonObject, "commentInfo"));

        EasyLog.LogMessage(this, "++ parse totalReward = " + totalReward);
        EasyLog.LogMessage(this, "++ parse casterNum = " + casterNum);
        EasyLog.LogMessage(this, "++ parse title = " + title);
        EasyLog.LogMessage(this, "++ parse surveyId = " + surveyId);

        mCast.setSurveyId(surveyId);
        mCast.setTotalReward(totalReward);
        mCast.setCasterNum(casterNum);
        mCast.setTitle(title);
        mCast.setTags(tag);
        mCast.setEndDate(endDate);
        mCast.setReference(reference);
        mCast.setStartDate(startData);
        mCast.setStatus(status);
        mCast.setThumbnails(thumbNails);
        mCast.setDescription(description);
        mCast.setParticipants(participants);
        mCast.setTimeLineList(timeLineList);
        mCast.setNewsList(newsList);

        return mCast;
    }

    private NewsList parseNewsList(JSONArray jsonArray)
    {
        NewsList newsList = null;

        int articleInfoSize = (jsonArray != null ? jsonArray.length() : 0);
        if (articleInfoSize > 0)
        {
            newsList = new NewsList();

            for (int i = 0 ; i < articleInfoSize ; i++)
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
                    String[] tag = UtilityData.convertStringArrayFromJSON(o, "tag");

                    EasyLog.LogMessage(this, "++ parseNewsList id = " + id);
                    EasyLog.LogMessage(this, "++ parseNewsList surveyId = " + surveyId);
                    EasyLog.LogMessage(this, "++ parseNewsList title = " + title);
                    EasyLog.LogMessage(this, "++ parseNewsList contents = " + contents);
                    News news = new News();
                    news.setId(id);
                    news.setSurveyId(surveyId);
                    news.setNewsTitle(title);
                    news.setContents(contents);
                    news.setLink(link);
                    news.setThumbnail(thumbnail);
                    news.setUpdated_at(updated_at);
                    news.setCreated_at(created_at);
                    news.setTags(tag);

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

    private TimeLineList parseTimeLineList(JSONArray jsonArray)
    {
        TimeLineList timeLineList = null;

        int commentInfoSize = (jsonArray != null ? jsonArray.length() : 0);
        if (commentInfoSize > 0)
        {
            timeLineList = new TimeLineList();

            for (int i = 0 ; i < commentInfoSize ; i++)
            {
                try
                {
                    JSONObject o = jsonArray.getJSONObject(i);


                    String id = UtilityData.convertStringFromJSON(o, "_id");
                    String surveyId = UtilityData.convertStringFromJSON(o, "surveyId");
                    String userId = UtilityData.convertStringFromJSON(o, "userId");
                    String updated_at = UtilityData.convertStringFromJSON(o, "updated_at");
                    String created_at = UtilityData.convertStringFromJSON(o, "created_at");
                    String comment = UtilityData.convertStringFromJSON(o, "comment");

                    JSONArray jsonArray1 = UtilityData.convertJsonArrayFromJson(o, "reply");

                    ReplyList replyList = parseReplyList(jsonArray1);

                    TimeLine timeLine = new TimeLine();
                    timeLine.setId(id);
                    timeLine.setSurveyId(surveyId);
                    timeLine.setUserId(userId);
                    timeLine.setUpdated_at(updated_at);
                    timeLine.setCreated_at(created_at);
                    timeLine.setComments(comment);
                    timeLine.setReplyList(replyList);

                    timeLineList.addTimeLine(timeLine);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return timeLineList;
    }

    private ReplyList parseReplyList(JSONArray jsonArray)
    {
        ReplyList replyList = null;

        int repliesSize = (jsonArray == null ? 0 : jsonArray.length());
        if (repliesSize > 0)
        {
            replyList = new ReplyList();

            for (int j = 0 ; j < repliesSize; j ++)
            {
                try
                {
                    JSONObject replyJson = jsonArray.getJSONObject(j);

                    String userName = UtilityData.convertStringFromJSON(replyJson, "userName");
                    String id = UtilityData.convertStringFromJSON(replyJson, "_id");
                    String updated_at = UtilityData.convertStringFromJSON(replyJson, "updated_at");
                    String created_at = UtilityData.convertStringFromJSON(replyJson, "created_at");
                    String content = UtilityData.convertStringFromJSON(replyJson, "content");

                    Reply reply = new Reply();
                    reply.setUserName(userName);
                    reply.setId(id);
                    reply.setUpdated_at(updated_at);
                    reply.setCreated_at(created_at);
                    reply.setContent(content);

                    replyList.addReply(reply);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return replyList;
    }



    public Cast getCast()
    {
        return mCast;
    }

    public void setCast(Cast c)
    {
        this.mCast = c;
    }

    public String getSurveyInfoId()
    {
        return (mCast != null ? mCast.getCastId() : null);
    }
}
