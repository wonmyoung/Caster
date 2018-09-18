package com.casting.component.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.casting.model.request.RequestMember;

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

        RequestMember requestMember = new RequestMember();
        requestMember.setResponseListener(this);

        RequestHandler.getInstance().request(requestMember);
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
        return false;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        EasyLog.LogMessage(this, ">> update ");

        if (o instanceof ActiveMember)
        {
            EasyLog.LogMessage(this, ">> update ActiveMember ");

            Member member = (Member) arg;

            String picThumbnail = member.getUserPicThumbnail();

            EasyLog.LogMessage(this, ">> update ActiveMember picThumbnail =" + picThumbnail);

            Context c = getContext();

            UtilityUI.setThumbNailRoundedImageView(c, mProfileUserPic, picThumbnail, R.dimen.dp25);
        }
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        EasyLog.LogMessage(this, ">> onThreadResponseListen ");

        BaseRequest request = response.getSourceRequest();

        EasyLog.LogMessage(this, ">> onThreadResponseListen request = " + request.getClass().getSimpleName());

        if (request.isRight(RequestMember.class))
        {
            Member member = (Member) response.getResponseModel();

            ActiveMember.getInstance().setMember(member);
        }
    }
}
