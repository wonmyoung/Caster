package com.casting.component.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casting.R;
import com.casting.commonmodule.view.component.CommonFragment;

import java.util.Observable;
import java.util.Observer;

public class ProfileEditFragment extends CommonFragment implements Observer
{

    public ProfileEditFragment() {
        super(R.layout.fragment_profile_edit);
    }

    @Override
    protected void onClickEvent(View v) {

    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception
    {

    }

    @Override
    public void update(Observable o, Object arg)
    {

    }

    @Override
    protected boolean onBackPressed() {
        return true;
    }
}
