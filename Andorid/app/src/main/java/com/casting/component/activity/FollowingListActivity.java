package com.casting.component.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.view.CircleImageView;
import com.casting.commonmodule.view.component.CommonActivity;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.commonmodule.view.list.CommonRecyclerView;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.Constants;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.FollowingVector;
import com.casting.model.FollowingVectorList;
import com.casting.model.request.RequestFollowingList;
import com.casting.view.ItemViewAdapter;

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
            mListViewTitle.setText(
                    (mListViewMode == followingList) ? "팔로잉 리스트" : "팔로워 리스트");

            RequestFollowingList requestFollowingList = new RequestFollowingList();
            requestFollowingList.setResponseListener(this);
            requestFollowingList.setRequestItemType((mListViewMode == followingList) ?
                    FOLLOWING_INFO_ITEM : FOLLOWER_INFO_ITEM);

            RequestHandler.getInstance().request(requestFollowingList);
        }
    }

    @Override
    public void bindBodyItemView
            (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
    {
        FollowingVector followingVector = (FollowingVector) item;

        String userName = followingVector.getUserName();
        String userId = followingVector.getUserId();
        String avatar = followingVector.getAvatar();

        CircleImageView circleImageView = holder.find(R.id.userImage);

        int radius = (int) getResources().getDimension(R.dimen.dp25);

        ImageLoader.loadRoundImage(this, circleImageView, avatar, radius);

        TextView textView1 = holder.find(R.id.userNameView);
        textView1.setText(userName);

        TextView textView2 = holder.find(R.id.userIdView);
        textView2.setText(userId);

        Button button = holder.find(R.id.followingButton);
        button.setTag(position);
        button.setText(followingVector.getItemType() == FOLLOWING_INFO_ITEM ? "팔로잉" : "팔로잉하기");
        button.setOnClickListener(this);
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        BaseRequest request = response.getSourceRequest();

        EasyLog.LogMessage(this, ">> onThreadResponseListen ");
        EasyLog.LogMessage(this, ">> onThreadResponseListen request = " + request.getClass().getSimpleName());

        if (request.isRight(RequestFollowingList.class))
        {
            onInfoListResponse(response);
        }
    }

    private void onInfoListResponse(BaseResponse response)
    {
        FollowingVectorList followingVectorList = (FollowingVectorList) response.getResponseModel();

        if (followingVectorList.size() > 0)
        {
            mListViewAdapter.setItemList(followingVectorList.getVectorArrayList());
            mListViewAdapter.notifyDataSetChanged();
        }
    }

}
