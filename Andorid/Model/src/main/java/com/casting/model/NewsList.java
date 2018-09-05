package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

import java.util.ArrayList;

public class NewsList extends BaseModel {

    private ArrayList<News> NewsArrayList;

    public ArrayList<News> getNewsArrayList() {
        return NewsArrayList;
    }

    public void setNewsArrayList(ArrayList<News> newsArrayList) {
        NewsArrayList = newsArrayList;
    }
}
