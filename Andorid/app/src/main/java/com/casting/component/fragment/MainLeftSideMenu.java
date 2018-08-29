package com.casting.component.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.view.CircleImageView;
import com.casting.commonmodule.view.component.CommonFragment;
import com.casting.component.activity.CastChartActivity;
import com.casting.component.activity.ProfileActivity;
import com.casting.component.activity.SettingActivity;
import com.casting.model.global.ActiveMember;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.Observable;
import java.util.Observer;

public class MainLeftSideMenu extends CommonFragment implements Observer {

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
                Intent intent = new Intent(getContext(), CastChartActivity.class);

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
        if (o instanceof ActiveMember)
        {

        }
    }
}
