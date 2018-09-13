package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

import java.util.ArrayList;

public class NewsList extends BaseModel implements ICommonItem, ItemConstant {

    private ArrayList<News> NewsArrayList = new ArrayList<>();

    public ArrayList<News> getNewsArrayList() {
        return NewsArrayList;
    }

    public void setNewsArrayList(ArrayList<News> newsArrayList) {
        NewsArrayList = newsArrayList;
    }

    public void addNews(News news)
    {
        if (NewsArrayList != null)
        {
            NewsArrayList.add(news);
        }
    }

    public int getNewsSize()
    {
        return (NewsArrayList != null ? NewsArrayList.size() : 0);
    }

    @Override
    public int getItemType()
    {
        return NEWS_GROUP;
    }
}
