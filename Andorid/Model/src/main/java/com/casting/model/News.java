package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

public class News extends BaseModel implements ICommonItem, ItemConstant {

    private String NewsTitle;
    private String News;

    private String NewsTime;

    @Override
    public int getItemType() {
        return NEWS;
    }

    public String getNewsTitle() {
        return NewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        NewsTitle = newsTitle;
    }

    public String getNews() {
        return News;
    }

    public void setNews(String news) {
        News = news;
    }

    public String getNewsTime() {
        return NewsTime;
    }

    public void setNewsTime(String newsTime) {
        NewsTime = newsTime;
    }
}
