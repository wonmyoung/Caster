package com.casting.component.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.view.component.CommonActivity;
import com.casting.commonmodule.view.list.CommonRecyclerView;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.Ranking;
import com.casting.model.global.ItemConstant;
import com.casting.model.request.RequestRankingList;
import com.casting.view.CustomTabLayout;
import com.casting.view.ItemViewAdapter;

import java.util.ArrayList;

public class RankingListActivity extends CommonActivity implements ItemBindStrategy, IResponseListener {

    private CustomTabLayout     mCustomTabLayout;

    private CommonRecyclerView  mListView;
    private ItemViewAdapter     mListViewAdapter;
    private LinearLayoutManager mListViewManager;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_cast_chart);

        mListViewAdapter = new ItemViewAdapter(this, this);
        mListView = find(R.id.castChartListView);
        mListView.setAdapter(mListViewAdapter);
        mListViewManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mListViewManager);
        mListView.setHasFixedSize(true);

        RequestRankingList requestRankingList = new RequestRankingList();
        requestRankingList.setResponseListener(this);

        RequestHandler.getInstance().request(requestRankingList);
    }

    @Override
    protected void onClickEvent(View v)
    {

    }

    @Override
    public void bindBodyItemView
            (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
    {

    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        ArrayList<ICommonItem> list = new ArrayList<>();

        loadDummyItemList(list);

        mListViewAdapter.clear();
        mListViewAdapter.notifyDataSetChanged();

        mListViewAdapter.setItemList(list);
        mListViewAdapter.notifyDataSetChanged();
    }

    private void loadDummyItemList(ArrayList<ICommonItem> list)
    {
        for (int i = 0 ; i < 100 ; i ++)
        {
            Ranking ranking = new Ranking();
            ranking.setItemType(ItemConstant.RANKING);

            list.add(ranking);
        }
    }
}
