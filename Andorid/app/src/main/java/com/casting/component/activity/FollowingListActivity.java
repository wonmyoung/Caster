package com.casting.component.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.view.component.CommonActivity;
import com.casting.commonmodule.view.list.CommonRecyclerView;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.Constants;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.FollowingVector;
import com.casting.model.global.ItemConstant;
import com.casting.model.request.RequestFollowingList;
import com.casting.view.ItemViewAdapter;

import java.util.ArrayList;

import static com.casting.model.request.RequestFollowingList.FollowingVector.FOLLOWER;
import static com.casting.model.request.RequestFollowingList.FollowingVector.FOLLOWING;

public class FollowingListActivity extends CommonActivity implements ItemBindStrategy, IResponseListener, Constants {

    private TextView                mListViewTitle;
    private CommonRecyclerView      mListView;
    private ItemViewAdapter         mListViewAdapter;
    private LinearLayoutManager     mListViewManager;

    private int mListViewMode;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_following);

        mListViewTitle = find(R.id.followingListViewTitle);
        mListViewAdapter = new ItemViewAdapter(this, this);
        mListView = find(R.id.followingListView);
        mListView.setAdapter(mListViewAdapter);
        mListViewManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mListViewManager);
        mListView.setHasFixedSize(true);

        mListViewMode = getIntent().getIntExtra(FOLLOWING_LIST_MODE, 0);

        if (mListViewMode > 0)
        {
            RequestFollowingList requestFollowingList = new RequestFollowingList();
            requestFollowingList.setResponseListener(this);
            requestFollowingList.setFollowingVector(
                    (mListViewMode == followingList) ? FOLLOWING : FOLLOWER);

            mListViewTitle.setText(
                    (mListViewMode == followingList) ? "팔로잉 리스트" : "팔로워 리스트");

            RequestHandler.getInstance().request(requestFollowingList);
        }
    }

    @Override
    public void bindBodyItemView
            (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
    {
        FollowingVector followingVector = (FollowingVector) item;
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        EasyLog.LogMessage(this, ">> onThreadResponseListen ");

        ArrayList<ICommonItem> list = new ArrayList<>();

        loadDummyItemList(list);

        mListViewAdapter.setItemList(list);
        mListViewAdapter.notifyDataSetChanged();
    }

    private void loadDummyItemList(ArrayList<ICommonItem> list)
    {
        for (int i = 0 ; i < 100 ; i ++)
        {
            FollowingVector followingVector = new FollowingVector();
            followingVector.setItemType((mListViewMode == followingList) ?
                    FOLLOWING_INFO_ITEM : FOLLOWER_INFO_ITEM);

            list.add(followingVector);
        }
    }
}
