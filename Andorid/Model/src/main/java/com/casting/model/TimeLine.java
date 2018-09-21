package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

import java.util.ArrayList;

public class TimeLine extends BaseModel implements ICommonItem, ItemConstant
{
    private Member  mMember;

    private String  Id;
    private String  SurveyId;
    private String  UserId;
    private String  Updated_at;
    private String  Created_at;
    private String  Comments;

    private ReplyList ReplyList;

    public Member getMember() {
        return mMember;
    }

    public void setMember(Member member) {
        mMember = member;
    }

    @Override
    public int getItemType() {
        return TIME_LINE;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUpdated_at() {
        return Updated_at;
    }

    public void setUpdated_at(String updated_at) {
        Updated_at = updated_at;
    }

    public String getCreated_at() {
        return Created_at;
    }

    public void setCreated_at(String created_at) {
        Created_at = created_at;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public ReplyList getReplyList() {
        return ReplyList;
    }

    public void setReplyList(ReplyList replyList) {
        ReplyList = replyList;
    }

    public void addReply(Reply reply)
    {
        if (ReplyList == null)
        {
            ReplyList = new ReplyList();
        }

        ReplyList.addReply(reply);
    }
}
