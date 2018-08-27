package com.casting.component.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casting.R;
import com.casting.commonmodule.view.component.CommonFragment;

public class MainRightSideMenu extends CommonFragment {

    public MainRightSideMenu()
    {
        super(R.layout.right_menu);
    }

    @Override
    protected void onClickEvent(View v)
    {

    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception
    {

    }

    @Override
    protected boolean onBackPressed()
    {
        return false;
    }
}
