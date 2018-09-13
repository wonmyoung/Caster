package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

import java.util.ArrayList;

public class ReplyList extends BaseModel {

    private ArrayList<Reply> ReplyArrayList = new ArrayList<>();

    public ArrayList<Reply> getReplyArrayList()
    {
        return ReplyArrayList;
    }

    public void setReplyArrayList(ArrayList<Reply> replyArrayList)
    {
        ReplyArrayList = replyArrayList;
    }

    public void addReply(Reply reply)
    {
        if (ReplyArrayList != null)
        {
            ReplyArrayList.add(reply);
        }
    }

    public int getReplyListSize()
    {
        return (ReplyArrayList != null ? ReplyArrayList.size() : 0);
    }

}
