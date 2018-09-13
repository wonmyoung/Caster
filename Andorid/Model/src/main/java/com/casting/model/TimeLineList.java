package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

import java.util.ArrayList;

public class TimeLineList extends BaseModel implements ICommonItem, ItemConstant {

    private ArrayList<TimeLine> TimeLineList;

    public ArrayList<TimeLine> getTimeLineList() {
        return TimeLineList;
    }

    public void setTimeLineList(ArrayList<TimeLine> timeLineList) {
        TimeLineList = timeLineList;
    }

    public void addTimeLine(TimeLine timeLine)
    {
        if (TimeLineList == null)
        {
            TimeLineList = new ArrayList<>();
        }

        TimeLineList.add(timeLine);
    }

    public int getTimeLineListSize()
    {
        return (TimeLineList == null ? 0 : TimeLineList.size());
    }

    public TimeLine getTimeLine(int i)
    {
        return (TimeLineList != null && TimeLineList.size() > i ?
                TimeLineList.get(i) : null);
    }

    @Override
    public int getItemType()
    {
        return TIME_LINE_GROUP;
    }
}
