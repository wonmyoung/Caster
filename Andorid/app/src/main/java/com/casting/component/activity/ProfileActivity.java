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

import com.casting.FutureCasting;
import com.casting.FutureCastingUtil;
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
import com.casting.interfaces.Constants;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.Cast;
import com.casting.model.CastList;
import com.casting.model.Member;
import com.casting.model.TimeLine;
import com.casting.model.TimeLineList;
import com.casting.model.global.ActiveMember;
import com.casting.model.global.ItemConstant;
import com.casting.model.request.RequestCastList;
import com.casting.model.request.RequestTimeLineList;
import com.casting.view.CustomTabLayout;
import com.casting.view.ItemViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ProfileActivity extends CommonActivity
        implements Observer, TabLayout.OnTabSelectedListener, ItemBindStrategy, IResponseListener, Constants {

    private Member      TargetMember;

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

        Serializable s = getIntent().getSerializableExtra(DEFINE_MEMBER);

        TargetMember = (s == null ? null : (Member) s);

        if (TargetMember == null)
        {
            ActiveMember activeMember = ActiveMember.getInstance();
            activeMember.addObserver(this);

            TargetMember = activeMember.getMember();
        }
        else
        {
            mUserEditButton.setVisibility(View.GONE);
        }

        updateMemberData(TargetMember);
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        BaseRequest request = response.getSourceRequest();

        if (request.isRight(RequestTimeLineList.class))
        {
            onResponseTimeLineList(response);
        }
        else if (request.isRight(CastList.class))
        {
            onResponseCastList(response);
        }
    }

    private void onResponseTimeLineList(BaseResponse response)
    {
        TimeLineList timeLineList = (TimeLineList) response.getResponseModel();

        if (timeLineList != null)
        {
            mListViewAdapter.setItemList(timeLineList.getTimeLineList());
            mListViewAdapter.notifyDataSetChanged();
        }
    }

    private void onResponseCastList(BaseResponse response)
    {
        int responseCode = response.getResponseCode();
        if (responseCode > 0)
        {
            CastList castList = (CastList) response.getResponseModel();

            int size = castList.getSize();
            if (size > 0)
            {
                mListViewAdapter.setItemList(castList.getCastList());
                mListViewAdapter.notifyDataSetChanged();
            }
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
            intent.putExtra(FOLLOWING_LIST_MODE, followingList);
            startActivity(intent);
        }
        else if (v.equals(mInfoView5))
        {
            Intent intent = new Intent(this, FollowingListActivity.class);
            intent.putExtra(FOLLOWING_LIST_MODE, followerList);
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
                RequestTimeLineList requestTimeLineList = new RequestTimeLineList();
                requestTimeLineList.setMember(TargetMember);
                requestTimeLineList.setResponseListener(this);

                RequestHandler.getInstance().request(requestTimeLineList);
                break;
            }

            case 1:
            {
                RequestCastList requestCastList = new RequestCastList();
                requestCastList.setOrder(RequestCastList.Order.Applied);
                requestCastList.setResponseListener(this);

                RequestHandler.getInstance().request(requestCastList);
                break;
            }

            case 2:
            {
                RequestCastList requestCastList = new RequestCastList();
                requestCastList.setOrder(RequestCastList.Order.Done);
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
            (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
    {

        switch (viewType)
        {
            case CAST_CARD_WIDE:
            {
                Cast cast = (Cast) item;

                String thumbNailPath = cast.getThumbnail(0);
                String[] tags = cast.getTags();
                String tag1 = (tags != null && tags.length > 0 ? tags[0] : null);
                String tag2 = (tags != null && tags.length > 1 ? tags[1] : null);
                String endDate = cast.getEndDate();
                String formattedEndDate = FutureCastingUtil.getTimeFormattedString(endDate);
                String castingNumberString = null;

                int castingNumber = cast.getCasterNum();
                if (castingNumber > 0)
                {
                    castingNumberString = Integer.toString(cast.getCasterNum());
                    castingNumberString += " 명 참여";
                }

                EasyLog.LogMessage(this, "++ bindBodyItemView CAST_CARD_WIDE getCastId = ", cast.getCastId());
                EasyLog.LogMessage(this, "++ bindBodyItemView CAST_CARD_WIDE thumbNailPath = ", thumbNailPath);
                EasyLog.LogMessage(this, "++ bindBodyItemView CAST_CARD_WIDE isCastingDone = " + cast.isCastingDone());
                EasyLog.LogMessage(this, "++ bindBodyItemView CAST_CARD_WIDE tag1 = " + tag1);
                EasyLog.LogMessage(this, "++ bindBodyItemView CAST_CARD_WIDE tag2 = " + tag2);
                EasyLog.LogMessage(this, "++ bindBodyItemView CAST_CARD_WIDE endDate = " + endDate);
                EasyLog.LogMessage(this, "++ bindBodyItemView CAST_CARD_WIDE formattedEndDate = " + formattedEndDate);

                TextView tagView1 = holder.find(R.id.castTag1);

                if (TextUtils.isEmpty(tag1))
                {
                    tagView1.setVisibility(View.GONE);
                }
                else
                {
                    tagView1.setText(tag1);
                    tagView1.setVisibility(View.VISIBLE);
                }

                TextView tagView2 = holder.find(R.id.castTag2);

                if (TextUtils.isEmpty(tag2))
                {
                    tagView2.setVisibility(View.GONE);
                }
                else
                {
                    tagView2.setText(tag2);
                    tagView2.setVisibility(View.VISIBLE);
                }

                CircleImageView circleImageView = holder.find(R.id.castCardBack);

                if (!TextUtils.isEmpty(thumbNailPath))
                {
                    int radius = (int) getResources().getDimension(R.dimen.dp25);

                    ImageLoader.loadRoundImage(this, circleImageView, thumbNailPath, radius);
                }
                else
                {
                    UtilityUI.setBackGroundDrawable(circleImageView, R.drawable.shape_gray_color_alpha50_round10);
                }

                TextView textView1 = find(R.id.castTopText1);
                TextView textView2 = find(R.id.castTopText2);
                TextView textView3 = find(R.id.castCardTitle);
                TextView textView4 = find(R.id.castCardDescription);

                textView1.setText(castingNumberString);

                if (FutureCastingUtil.isPast(endDate))
                {
                    textView2.setText("종료");
                    textView2.setPadding(20,5,20,5);

                    UtilityUI.setBackGroundDrawable(textView2, R.drawable.shape_pink_color_round5_alpha80);
                }
                else if (cast.isCastingDone())
                {
                    textView2.setText("참여중");
                    textView2.setPadding(20,5,20,5);

                    UtilityUI.setBackGroundDrawable(textView2, R.drawable.shape_pink_color_round5_alpha80);
                }
                else
                {
                    formattedEndDate += " 전";

                    textView2.setText(formattedEndDate);
                }

                textView3.setText(cast.getTitle());
                textView4.setVisibility(View.GONE);
                break;
            }

            case TIME_LINE:
            {
                TimeLine timeLine = (TimeLine) item;

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(FutureCasting.HTTP_PROTOCOL);
                stringBuilder.append(FutureCasting.SERVER_DOMAIN);
                stringBuilder.append(FutureCasting.SERVER_PORT);
                stringBuilder.append("/uploads/account/");
                stringBuilder.append(timeLine.getUserId());
                CircleImageView circleImageView = holder.find(R.id.userImage);

                UtilityUI.setThumbNailRoundedImageView(this, circleImageView, stringBuilder.toString(), R.dimen.dp25);

                String createdDate = timeLine.getCreated_at();
                String comments = timeLine.getComments();
                String userId = timeLine.getUserId();
                String userName = timeLine.getUserName();

                TextView textView1 = holder.find(R.id.userNickName);
                textView1.setText(userName);

                TextView textView2 = holder.find(R.id.userId);
                textView2.setText(userId);

                TextView textView3 = holder.find(R.id.userTimeLine);
                textView3.setText(comments);

                StringBuilder createdDateBuilder = new StringBuilder();
                createdDateBuilder.append(FutureCastingUtil.getTimeFormattedString(createdDate));
                createdDateBuilder.append(" 전");
                TextView textView4 = holder.find(R.id.userTimeLineTime);
                textView4.setText(createdDateBuilder);

                holder.find(R.id.replyThisTimeLine).setVisibility(View.GONE);
                holder.find(R.id.shareTimeLineButton).setVisibility(View.GONE);
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
