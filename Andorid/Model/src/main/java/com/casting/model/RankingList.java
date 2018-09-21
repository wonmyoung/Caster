package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

import java.util.ArrayList;


public class RankingList extends BaseModel implements ICommonItem, ItemConstant {

    private String ChartItemListTitle;

    private ArrayList<Ranking> rankingList;

    @Override
    public int getItemType()
    {
        return RANKING_LIST;
    }

    public ArrayList<Ranking> getRankingList() {
        return rankingList;
    }

    public void setRankingList(ArrayList<Ranking> rankingList) {
        this.rankingList = rankingList;
    }

    public String getItemListTitle() {
        return ChartItemListTitle;
    }

    public void setItemListTitle(String chartItemListTitle) {
        ChartItemListTitle = chartItemListTitle;
    }

    public void addRanking(Ranking ranking)
    {
        if (rankingList == null)
        {
            rankingList = new ArrayList<>();
        }
        rankingList.add(ranking);
    }

    public int getListSize()
    {
        return (rankingList == null ? 0 : rankingList.size());
    }
}
