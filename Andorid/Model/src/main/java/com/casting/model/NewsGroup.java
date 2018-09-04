package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

import java.util.ArrayList;

public class NewsGroup extends BaseModel implements ICommonItem, ItemConstant {

    private ArrayList<News> NewsArrayList;

    public ArrayList<News> getNewsArrayList()
    {
        return NewsArrayList;
    }

    public void setNewsArrayList(ArrayList<News> newsArrayList) {
        NewsArrayList = newsArrayList;
    }

    @Override
    public int getItemType() {
        return TIME_LINE_GROUP;
    }
}
