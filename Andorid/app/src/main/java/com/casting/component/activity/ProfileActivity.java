package com.casting.component.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.CircleImageView;
import com.casting.commonmodule.view.component.CommonActivity;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.Cast;
import com.casting.model.CastList;
import com.casting.model.Member;
import com.casting.model.TimeLine;
import com.casting.model.global.ActiveMember;
import com.casting.model.global.ItemConstant;
import com.casting.model.request.RequestCastList;
import com.casting.model.request.RequestTimeLineList;
import com.casting.view.CustomTabLayout;
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

    private CustomTabLayout     mCustomTabLayout;
    private Button              mUserEditButton;
    private RecyclerView        mListView;
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

        mCustomTabLayout = find(R.id.userProfileTab);
        mCustomTabLayout.addOnTabSelectedListener(this);
        mCustomTabLayout.addTextTab("타임라인");
        mCustomTabLayout.addTextTab("참여중인 캐스트");
        mCustomTabLayout.addTextTab("종료된 캐스트");

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
        BaseRequest request = response.getSourceRequest();

        if (request.isRight(RequestTimeLineList.class))
        {

        }
        else if (request.isRight(CastList.class))
        {

        }
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

            String picThumbnail = member.getUserPicThumbnail();
            String userId = member.getUserId();
            String userName = member.getUserName();
            String userLevel = member.getUserLevel();
            String description = member.getDescription();

            EasyLog.LogMessage(this, "++ update userName =" + userName);
            EasyLog.LogMessage(this, "++ update userId =" + userId);
            EasyLog.LogMessage(this, "++ update picThumbnail = " + picThumbnail);
            EasyLog.LogMessage(this, "++ update userLevel  =" + userLevel);

            if (TextUtils.isEmpty(userId))
            {
                SpannableString spannableString = new SpannableString("(확인안됨)");
                spannableString.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#a0464646")), 0, spannableString.length(), 0);
                mUserIdView.setText(spannableString);
            }
            else
            {
                mUserIdView.setText(userId);
            }

            if (TextUtils.isEmpty(userName))
            {
                SpannableString spannableString = new SpannableString("(이름없음)");
                spannableString.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#666666")), 0, spannableString.length(), 0);
                mUserNickNameView.setText(spannableString);
            }
            else
            {
                mUserNickNameView.setText(userName);
            }

            if (TextUtils.isEmpty(userLevel))
            {
                mUserGradeView.setVisibility(View.GONE);
            }
            else
            {
                mUserGradeView.setVisibility(View.VISIBLE);
                mUserGradeView.setText(userLevel);
            }

            mUserDescription.setText(description);

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

        int color1 = Color.parseColor("#a0464646");
        int color2 = Color.BLACK;

        SpannableString spannableString1 = UtilityUI.getColorSpannableString(prefix, color1);
        SpannableString spannableString2 = UtilityUI.getColorSpannableString(data, color2);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(spannableString1);
        builder.append("  ");
        builder.append(spannableString2);

        return builder;
    }
}
