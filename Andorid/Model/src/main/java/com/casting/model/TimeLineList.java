package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

import java.util.ArrayList;

public class TimeLineList extends BaseModel {

    private ArrayList<TimeLine> TimeLineList;

    public ArrayList<TimeLine> getTimeLineList() {
        return TimeLineList;
    }

    public void setTimeLineList(ArrayList<TimeLine> timeLineList) {
        TimeLineList = timeLineList;
    }
}
