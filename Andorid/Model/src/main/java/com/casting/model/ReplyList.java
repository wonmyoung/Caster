package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

import java.util.ArrayList;

public class ReplyList extends BaseModel {

    private ArrayList<Reply> ReplyArrayList = new ArrayList<>();

    public ArrayList<Reply> getReplyList()
    {
        return ReplyArrayList;
    }

    public void setReplyList(ArrayList<Reply> replyArrayList)
    {
        ReplyArrayList = replyArrayList;
    }

    public void addReply(Reply reply)
    {
        if (ReplyArrayList == null)
        {
            ReplyArrayList = new ArrayList<>();
        }

        ReplyArrayList.add(reply);
    }

    public void addReply(int i, Reply reply)
    {
        if (ReplyArrayList == null)
        {
            ReplyArrayList = new ArrayList<>();
        }

        ReplyArrayList.add(i, reply);
    }

    public int getReplyListSize()
    {
        return (ReplyArrayList != null ? ReplyArrayList.size() : 0);
    }

}
