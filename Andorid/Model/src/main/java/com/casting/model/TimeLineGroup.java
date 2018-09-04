package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

import java.util.ArrayList;

public class TimeLineGroup extends BaseModel implements ICommonItem, ItemConstant
{
    private Member  mMember;

    private ArrayList<TimeLine> TimeLineArrayList;

    public Member getMember() {
        return mMember;
    }

    public void setMember(Member member) {
        mMember = member;
    }

    public ArrayList<TimeLine> getTimeLineArrayList() {
        return TimeLineArrayList;
    }

    public void setTimeLineArrayList(ArrayList<TimeLine> timeLines) {
        TimeLineArrayList = timeLines;
    }

    @Override
    public int getItemType() {
        return TIME_LINE_GROUP;
    }
}
