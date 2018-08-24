package com.casting.component.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.view.CircleImageView;
import com.casting.commonmodule.view.component.CommonActivity;

import java.util.Observable;
import java.util.Observer;

public class ProfileActivity extends CommonActivity implements Observer
{

    private CircleImageView mUserPicView;

    private TextView    mUserNickNameView;
    private TextView    mUserGradeView;
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

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.profile_activity);
    }

    @Override
    public void update(Observable o, Object arg)
    {

    }

    @Override
    protected void onClickEvent(View v)
    {

    }
}
