package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;

import java.util.ArrayList;

public class Alarm extends BaseModel implements ICommonItem {

    private int mItemType;

    private String SurveyId;

    private String EndDate;

    private Cast                TargetCast;

    private ArrayList<Member>   Friends;

    public void setItemType(int itemType)
    {
        mItemType = itemType;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }

    public ArrayList<Member> getFriends() {
        return Friends;
    }

    public void setFriends(ArrayList<Member> friends) {
        Friends = friends;
    }

    public Cast getTargetCast() {
        return TargetCast;
    }

    public void setTargetCast(Cast targetCast) {
        TargetCast = targetCast;
    }
}
