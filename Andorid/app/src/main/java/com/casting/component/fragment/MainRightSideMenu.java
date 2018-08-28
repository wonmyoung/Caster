package com.casting.component.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.view.component.CommonFragment;
import com.casting.commonmodule.view.list.CommonRecyclerView;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.Alarm;
import com.casting.model.FollowingVector;
import com.casting.model.global.ItemConstant;
import com.casting.model.request.RequestAlarmList;
import com.casting.view.ItemViewAdapter;

import java.util.ArrayList;

public class MainRightSideMenu extends CommonFragment implements IResponseListener, ItemBindStrategy {

    private CommonRecyclerView  mListView;
    private ItemViewAdapter     mListViewAdapter;
    private LinearLayoutManager mListViewManager;

    public MainRightSideMenu()
    {
        super(R.layout.right_menu);
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception
    {
        mListViewAdapter = new ItemViewAdapter(getContext(), this);
        mListView = find(R.id.alarmListView);
        mListView.setAdapter(mListViewAdapter);
        mListViewManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mListViewManager);
        mListView.setHasFixedSize(true);

        RequestAlarmList requestAlarmList = new RequestAlarmList();
        requestAlarmList.setResponseListener(this);

        RequestHandler.getInstance().request(requestAlarmList);
    }

    @Override
    protected void onClickEvent(View v)
    {

    }

    @Override
    protected boolean onBackPressed()
    {
        return false;
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
            Alarm alarm  = new Alarm();
            alarm.setItemType(ItemConstant.ALARM);

            list.add(alarm);
        }
    }

    @Override
    public void bindBodyItemView
            (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
    {

    }
}
