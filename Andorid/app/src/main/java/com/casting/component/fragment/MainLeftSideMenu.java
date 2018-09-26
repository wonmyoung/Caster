package com.casting.component.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.CircleImageView;
import com.casting.commonmodule.view.component.CommonFragment;
import com.casting.component.activity.RankingListActivity;
import com.casting.component.activity.ProfileActivity;
import com.casting.component.activity.SettingActivity;
import com.casting.model.Member;
import com.casting.model.global.ActiveMember;
import com.casting.model.request.RequestMemberLatest;

import java.util.Observable;
import java.util.Observer;

public class MainLeftSideMenu extends CommonFragment implements Observer, IResponseListener {

    private CircleImageView mProfileUserPic;
    private TextView        mProfileUserGrade;
    private TextView        mProfileUserName;
    private TextView        mProfileUserId;

    public MainLeftSideMenu()
    {
        super(R.layout.left_menu);
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception
    {
        find(R.id.leftMenuButton1).setOnClickListener(this);
        find(R.id.leftMenuButton2).setOnClickListener(this);
        find(R.id.leftMenuButton3).setOnClickListener(this);

        mProfileUserPic = find(R.id.userImage);
        mProfileUserGrade = find(R.id.userGrade);
        mProfileUserName = find(R.id.userNickName);
        mProfileUserId = find(R.id.userIdView);

        ActiveMember activeMember = ActiveMember.getInstance();
        activeMember.addObserver(this);
        Member member = activeMember.getMember();

        RequestMemberLatest requestMemberLatest = new RequestMemberLatest();
        requestMemberLatest.setCurrentMember(member);
        requestMemberLatest.setResponseListener(this);

        RequestHandler.getInstance().request(requestMemberLatest);
    }

    @Override
    protected void onClickEvent(View v)
    {
        switch (v.getId())
        {
            case R.id.leftMenuButton1:
            {
                Intent intent = new Intent(getContext(), ProfileActivity.class);

                startActivity(intent);
                break;
            }

            case R.id.leftMenuButton2:
            {
                Intent intent = new Intent(getContext(), RankingListActivity.class);

                startActivity(intent);
                break;
            }

            case R.id.leftMenuButton3:
            {
                Intent intent = new Intent(getContext(), SettingActivity.class);

                startActivity(intent);
                break;
            }
        }
    }


    @Override
    protected boolean onBackPressed()
    {
        return true;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        EasyLog.LogMessage(this, ">> update ");

        if (o instanceof ActiveMember)
        {
            Member member = (Member) arg;

            if (member != null)
            {
                String picThumbnail = member.getUserPicThumbnail();
                String userId = member.getUserId();
                String userName = member.getUserName();
                String userLevel = member.getUserLevel();

                EasyLog.LogMessage(this, "++ update userName =" + userName);
                EasyLog.LogMessage(this, "++ update userId =" + userId);
                EasyLog.LogMessage(this, "++ update picThumbnail = " + picThumbnail);
                EasyLog.LogMessage(this, "++ update userLevel  =" + userLevel);

                if (TextUtils.isEmpty(userId))
                {
                    SpannableString spannableString = new SpannableString("(확인안됨)");
                    spannableString.setSpan(
                            new ForegroundColorSpan(Color.parseColor("#a0464646")), 0, spannableString.length(), 0);
                    mProfileUserId.setText(spannableString);
                }
                else
                {
                    mProfileUserId.setText(userId);
                }

                if (TextUtils.isEmpty(userName))
                {
                    SpannableString spannableString = new SpannableString("(이름없음)");
                    spannableString.setSpan(
                            new ForegroundColorSpan(Color.parseColor("#666666")), 0, spannableString.length(), 0);
                    mProfileUserName.setText(spannableString);
                }
                else
                {
                    mProfileUserName.setText(userName);
                }

                if (TextUtils.isEmpty(userLevel))
                {
                    mProfileUserGrade.setVisibility(View.GONE);
                }
                else
                {
                    mProfileUserGrade.setVisibility(View.VISIBLE);
                    mProfileUserGrade.setText(userLevel);
                }

                Context c = getContext();

                UtilityUI.setThumbNailRoundedImageView(c, mProfileUserPic, picThumbnail, R.dimen.dp25);
            }
        }
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        BaseRequest request = response.getSourceRequest();

        EasyLog.LogMessage(this, ">> onThreadResponseListen ");
        EasyLog.LogMessage(this, ">> onThreadResponseListen request = " + request.getClass().getSimpleName());

        if (request.isRight(RequestMemberLatest.class))
        {
            Member member = (Member) response.getResponseModel();

            ActiveMember.getInstance().setMember(member);
        }
    }
}
