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
import com.casting.model.CastingStatus;
import com.casting.model.News;
import com.casting.model.NewsList;
import com.casting.model.Reply;
import com.casting.model.ReplyList;
import com.casting.model.TimeLine;
import com.casting.model.TimeLineList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class RequestCast extends NetworkRequest implements JSONParcelable<Cast> {

    private Cast    mCast;

    private String  ErrorMessage;

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

        if (surveyInfo != null)
        {
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

            CastingStatus castingStatus = parseCastingStatus(
                    UtilityData.convertJsonFromJson(jsonObject, "userPredictInfo"),
                    UtilityData.convertIntegerFromJSON(jsonObject,"countPredict"));

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
            mCast.setCurrentCastingStatus(castingStatus);
        }
        else
        {
            ErrorMessage = UtilityData.convertStringFromJSON(jsonObject, "message");
        }

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

                    EasyLog.LogMessage(this, "++ parseNewsList o = " + o.toString());

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

    private CastingStatus parseCastingStatus(JSONObject userPredictInfo, int countPredict)
    {
        CastingStatus castingStatus = null;

        if (userPredictInfo != null)
        {
            EasyLog.LogMessage(this, ">> parseCastingStatus userPredictInfo = " + userPredictInfo.toString());
            EasyLog.LogMessage(this, ">> parseCastingStatus countPredict = " + countPredict);

            castingStatus = new CastingStatus();

            Iterator<String> iterator = userPredictInfo.keys();

            while (iterator.hasNext())
            {
                String key = iterator.next();

                int value = UtilityData.convertIntegerFromJSON(userPredictInfo, key);
                if (value > 0 && countPredict > 0)
                {
                    int percentage = (value * 100) / countPredict;

                    castingStatus.addStatus(key, percentage);
                }
            }

            // castingStatus.sort();
        }

        return castingStatus;
    }

    private TimeLineList parseTimeLineList(JSONArray jsonArray)
    {
        TimeLineList timeLineList = null;

        int commentInfoSize = (jsonArray != null ? jsonArray.length() : 0);
        if (commentInfoSize > 0)
        {
            EasyLog.LogMessage(this, ">> parseTimeLineList jsonArray = " + jsonArray.toString());

            timeLineList = new TimeLineList();

            for (int i = 0 ; i < commentInfoSize ; i++)
            {
                try
                {
                    JSONObject o = jsonArray.getJSONObject(i);

                    EasyLog.LogMessage(this, ">> parseTimeLineList o = " + o.toString());

                    String id = UtilityData.convertStringFromJSON(o, "_id");
                    String surveyId = UtilityData.convertStringFromJSON(o, "surveyId");
                    JSONObject userObject = UtilityData.convertJsonFromJson(o, "userId");
                    String userId = UtilityData.convertStringFromJSON(userObject, "_id");
                    String userName = UtilityData.convertStringFromJSON(userObject, "username");
                    String avatar = UtilityData.convertStringFromJSON(userObject, "avatar");
                    String updated_at = UtilityData.convertStringFromJSON(o, "updated_at");
                    String created_at = UtilityData.convertStringFromJSON(o, "created_at");
                    String comment = UtilityData.convertStringFromJSON(o, "comment");

                    EasyLog.LogMessage(this, ">> parseTimeLineList id = " + id);
                    EasyLog.LogMessage(this, ">> parseTimeLineList surveyId = " + surveyId);
                    EasyLog.LogMessage(this, ">> parseTimeLineList userId = " + userId);
                    EasyLog.LogMessage(this, ">> parseTimeLineList userName = " + userName);
                    EasyLog.LogMessage(this, ">> parseTimeLineList avatar = " + avatar);
                    EasyLog.LogMessage(this, ">> parseTimeLineList updated_at = " + updated_at);
                    EasyLog.LogMessage(this, ">> parseTimeLineList created_at = " + created_at);
                    EasyLog.LogMessage(this, ">> parseTimeLineList comment = " + comment);

                    JSONArray jsonArray1 = UtilityData.convertJsonArrayFromJson(o, "reply");

                    ReplyList replyList = parseReplyList(jsonArray1);

                    TimeLine timeLine = new TimeLine();
                    timeLine.setId(id);
                    timeLine.setSurveyId(surveyId);
                    timeLine.setUserId(userId);
                    timeLine.setUserName(userName);
                    timeLine.setUserAvatar(avatar);
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
        EasyLog.LogMessage(this, ">> parseReplyList ");

        ReplyList replyList = null;

        int repliesSize = (jsonArray == null ? 0 : jsonArray.length());
        if (repliesSize > 0)
        {
            EasyLog.LogMessage(this, ">> parseReplyList jsonArray = " + jsonArray.toString());

            replyList = new ReplyList();

            for (int j = 0 ; j < repliesSize; j ++)
            {
                try
                {
                    JSONObject replyJson = jsonArray.getJSONObject(j);

                    EasyLog.LogMessage(this, ">> parseReplyList replyJson = " + replyJson.toString());

                    String userName = UtilityData.convertStringFromJSON(replyJson, "userName");
                    String avatar = UtilityData.convertStringFromJSON(replyJson, "avatar");
                    String id = UtilityData.convertStringFromJSON(replyJson, "_id");
                    String updated_at = UtilityData.convertStringFromJSON(replyJson, "updated_at");
                    String created_at = UtilityData.convertStringFromJSON(replyJson, "created_at");
                    String content = UtilityData.convertStringFromJSON(replyJson, "content");

                    EasyLog.LogMessage(this, ">> parseReplyList userName = " + userName);
                    EasyLog.LogMessage(this, ">> parseReplyList avatar = " + avatar);
                    EasyLog.LogMessage(this, ">> parseReplyList id = " + id);
                    EasyLog.LogMessage(this, ">> parseReplyList updated_at = " + updated_at);
                    EasyLog.LogMessage(this, ">> parseReplyList created_at = " + created_at);
                    EasyLog.LogMessage(this, ">> parseReplyList content = " + content);

                    Reply reply = new Reply();
                    reply.setUserName(userName);
                    reply.setUserAvatar(avatar);
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

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
