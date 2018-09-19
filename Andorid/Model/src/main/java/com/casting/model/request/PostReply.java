package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Reply;

import org.json.JSONObject;

public class PostReply extends NetworkRequest implements JSONParcelable<Reply> {

    private String  Id;
    private String  Content;

    @Override
    public Reply parse(JSONObject jsonObject)
    {
        EasyLog.LogMessage(this, ">> parse jsonObject = " + jsonObject.toString());

        Reply reply = null;

        JSONObject replyInfo = UtilityData.convertJsonFromJson(jsonObject, "reply");

        if (replyInfo != null)
        {
            String content = UtilityData.convertStringFromJSON(replyInfo, "content");
            String username = UtilityData.convertStringFromJSON(replyInfo, "username");
            String userId = UtilityData.convertStringFromJSON(replyInfo, "userId");
            String avatar = UtilityData.convertStringFromJSON(replyInfo, "avatar");
            String date = UtilityData.convertStringFromJSON(replyInfo, "date");

            EasyLog.LogMessage(this, "++ parse content = " + content);
            EasyLog.LogMessage(this, "++ parse username = " + username);
            EasyLog.LogMessage(this, "++ parse userId = " + userId);
            EasyLog.LogMessage(this, "++ parse avatar = " + avatar);
            EasyLog.LogMessage(this, "++ parse date = " + date);

            reply = new Reply();
            reply.setContent(content);
            reply.setUserName(username);
            reply.setUserId(userId);
            reply.setUserAvatar(avatar);
            reply.setCreated_at(date);
        }

        return reply;
    }

    @Override
    public String getHttpMethod()
    {
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
        contentValues.put("id", Id);
        contentValues.put("content", Content);

        return contentValues;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable<Reply> getNetworkParcelable()
    {
        return this;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
