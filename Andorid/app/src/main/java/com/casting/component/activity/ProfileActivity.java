package com.casting.component.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.CircleImageView;
import com.casting.commonmodule.view.component.CommonActivity;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.Cast;
import com.casting.model.Member;
import com.casting.model.TimeLine;
import com.casting.model.global.ActiveMember;
import com.casting.model.global.ItemConstant;
import com.casting.model.request.RequestCastList;
import com.casting.model.request.RequestTimeLineList;
import com.casting.view.ItemViewAdapter;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ProfileActivity extends CommonActivity
        implements Observer, TabLayout.OnTabSelectedListener, ItemBindStrategy, IResponseListener {

    private CircleImageView mUserPicView;

    private TextView    mUserNickNameView;
    private TextView    mUserGradeView;
    private TextView    mUserDescription;
    private TextView    mUserIdView;
    private TextView    mInfoView1;
    private TextView    mInfoView2;
    private TextView    mInfoView3;
    private TextView    mInfoView4;
    private TextView    mInfoView5;
    private TextView    mInfoView6;

    private TabLayout       mTabLayout;
    private Button          mUserEditButton;
    private RecyclerView    mListView;
    private ItemViewAdapter     mListViewAdapter;
    private LinearLayoutManager mListViewManager;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_profile);

        mUserPicView = find(R.id.userImage);
        mUserNickNameView = find(R.id.userNickName);
        mUserGradeView = find(R.id.userGrade);
        mUserIdView = find(R.id.userIdView);
        mUserDescription = find(R.id.userDescription);
        mUserEditButton = find(R.id.userProfileEdit);
        mUserEditButton.setOnClickListener(this);

        mInfoView1 = find(R.id.userInfo1);
        mInfoView2 = find(R.id.userInfo2);
        mInfoView3 = find(R.id.userInfo3);
        mInfoView4 = find(R.id.userInfo4);
        mInfoView4.setOnClickListener(this);
        mInfoView5 = find(R.id.userInfo5);
        mInfoView5.setOnClickListener(this);
        mInfoView6 = find(R.id.userInfo6);
        mInfoView6.setVisibility(View.INVISIBLE);

        mTabLayout = find(R.id.userProfileTab);
        mTabLayout.addOnTabSelectedListener(this);
        TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setText("타임라인");

        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setText("참여중인 캐스트");

        TabLayout.Tab tab3 = mTabLayout.newTab();
        tab3.setText("종료된 캐스트");
        mTabLayout.addTab(tab1);
        mTabLayout.addTab(tab2);
        mTabLayout.addTab(tab3);

        mListViewAdapter = new ItemViewAdapter(this, this);
        mListView = find(R.id.userProfileListView);
        mListView.setAdapter(mListViewAdapter);
        mListViewManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mListViewManager);
        mListView.setHasFixedSize(true);

        ActiveMember activeMember = ActiveMember.getInstance();

        updateMemberData(activeMember.getMember());

        activeMember.addObserver(this);
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        mListViewAdapter.clear();
        mListViewAdapter.notifyDataSetChanged();
//
//        BaseModel m = response.getResponseModel();
//
//        if (m instanceof TimeLineList)
//        {
//            TimeLineList t = (TimeLineList) m;
//
//            mListViewAdapter.setItemList(t.getTimeLineList());
//            mListViewAdapter.notifyDataSetChanged();
//        }
//        else if (m instanceof CastList)
//        {
//
//            CastList c = (CastList) m;
//
//            mListViewAdapter.setItemList(c.getCastList());
//            mListViewAdapter.notifyDataSetChanged();
//        }
        Log.d("confirm" , ">> confirm onThreadResponseListen getResponseCode = " + response.getResponseCode());

        ArrayList<ICommonItem> list = new ArrayList<>();

        if (mTabLayout.getSelectedTabPosition() == 0)
        {
            loadDummyTimeLineList(list);
        }
        else
        {
            loadDummyCastList(list);
        }

        mListViewAdapter.setItemList(list);
        mListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (o instanceof ActiveMember && arg instanceof Member)
        {
            Member member = (Member) arg;

            updateMemberData(member);
        }
    }

    private void updateMemberData(Member member)
    {
        if (member != null)
        {
            ImageLoader.loadImage(this, mUserPicView, member.getUserPicThumbnail());

            mUserNickNameView.setText(member.getUserName());
            mUserIdView.setText(member.getUserId());
            mUserGradeView.setText(member.getUserLevel());
            mUserDescription.setText(member.getDescription());

            mInfoView1.setText(buildInfoString("CAP", member.getUserPoint()));
            mInfoView2.setText(buildInfoString("예측수", member.getCorrectCast()));
            mInfoView3.setText(buildInfoString("적중률", member.getCorrectRate()));
            mInfoView4.setText(buildInfoString("팔로잉", member.getFollowingNum()));
            mInfoView5.setText(buildInfoString("팔로워", member.getFollowerNum()));
        }
    }

    @Override
    protected void onClickEvent(View v)
    {
        if (v.equals(mUserEditButton))
        {
            Intent intent = new Intent(this, ProfileEditActivity.class);

            startActivity(intent);
        }
        else if (v.equals(mInfoView4))
        {
            Intent intent = new Intent(this, FollowingListActivity.class);

            startActivity(intent);
        }
        else if (v.equals(mInfoView5))
        {
            Intent intent = new Intent(this, FollowingListActivity.class);

            startActivity(intent);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
        switch (tab.getPosition())
        {
            case 0:
            {
                Member member = ActiveMember.getInstance().getMember();

                RequestTimeLineList requestTimeLineList = new RequestTimeLineList();
                requestTimeLineList.setMember(member);
                requestTimeLineList.setResponseListener(this);

                RequestHandler.getInstance().request(requestTimeLineList);
                break;
            }

            case 1:
            {
                RequestCastList requestCastList = new RequestCastList();
                requestCastList.setEmailAddress(ActiveMember.getInstance().getEmailAddress());
                requestCastList.setOrder(RequestCastList.Order.Available);
                requestCastList.setResponseListener(this);

                RequestHandler.getInstance().request(requestCastList);
                break;
            }

            case 2:
            {
                RequestCastList requestCastList = new RequestCastList();
                requestCastList.setEmailAddress(ActiveMember.getInstance().getEmailAddress());
                requestCastList.setOrder(RequestCastList.Order.Available);
                requestCastList.setResponseListener(this);

                RequestHandler.getInstance().request(requestCastList);
                break;
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab)
    {
        mListViewAdapter.clear();
        mListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab)
    {

    }

    @Override
    public void bindBodyItemView
            (CompositeViewHolder viewHolder, int position, int viewType, ICommonItem item) throws Exception
    {

        switch (viewType)
        {
            case CAST_CARD:
            {
                break;
            }

            case CAST_CARD_WIDE_THIN:
            {
                Cast cast = (Cast) item;

                ImageView imageView = viewHolder.find(R.id.castCardBack);

                int radius = (int) getResources().getDimension(R.dimen.dp25);

                ImageLoader.loadRoundImage(this, imageView, cast.getThumbnail(0), radius);
                break;
            }

            case CAST_CARD_WIDE:
            {
                break;
            }

            case TIME_LINE:
            {
                break;
            }
        }
    }

    private SpannableStringBuilder buildInfoString(String prefix, double data)
    {
        return buildInfoString(prefix, Double.toString(data));
    }

    private SpannableStringBuilder buildInfoString(String prefix, int data)
    {
        return buildInfoString(prefix, Integer.toString(data));
    }

    private SpannableStringBuilder buildInfoString(String prefix, String data)
    {

        int color1 = UtilityUI.getColor(this, R.color.color_f2f2f2);
        int color2 = Color.BLACK;

        SpannableString spannableString1 = UtilityUI.getColorSpannableString(prefix, color1);
        SpannableString spannableString2 = UtilityUI.getColorSpannableString(data, color2);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(spannableString1);
        builder.append("  ");
        builder.append(spannableString2);

        return builder;
    }

    private void loadDummyTimeLineList(ArrayList<ICommonItem> list)
    {
        if (list == null) {
            list = new ArrayList<>();
        }

        for (int i = 0 ; i < 100 ; i++)
        {
            TimeLine timeLine = new TimeLine();

            list.add(timeLine);
        }
    }

    private void loadDummyCastList(ArrayList<ICommonItem> list)
    {
        if (list == null) {
            list = new ArrayList<>();
        }

        for (int i = 0 ; i < 100 ; i++)
        {
            Cast cast = new Cast();
            cast.setItemType(ItemConstant.CAST_CARD_WIDE_THIN);
            cast.setRemainingTime(60 * 60 * 1000);
            cast.setTotalReward(20000);
            cast.setTitle("비트코인의 7월 25일 지정가격은 얼마일까요 ?");
            cast.setTags("비트코인", "더미데이터", "바톤컴퍼니", "가즈아!!");
            cast.setThumbnails(
                    "http://1.bp.blogspot.com/-suPZ9GdewYU/WjL2nodqGpI/AAAAAAABlZY/MgopnrYkJyQHGPnjnhp2ynzoz11h0PTHgCK4BGAYYCw/s1600/960x0.jpg");

            list.add(cast);
        }
    }
}
