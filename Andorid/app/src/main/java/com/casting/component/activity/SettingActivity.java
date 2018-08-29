package com.casting.component.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.view.component.CommonActivity;

public class SettingActivity extends BaseFCActivity implements CompoundButton.OnCheckedChangeListener, IResponseListener {

    private ToggleButton    mToggleButton1;
    private ToggleButton    mToggleButton2;
    private ToggleButton    mToggleButton3;
    private ToggleButton    mToggleButton4;

    private View    mInformationView1;
    private View    mInformationView2;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_setting);

        mToggleButton1 = find(R.id.settingButton1);
        mToggleButton1.setOnCheckedChangeListener(this);
        mToggleButton2 = find(R.id.settingButton2);
        mToggleButton3 = find(R.id.settingButton3);
        mToggleButton4 = find(R.id.settingButton4);

        mInformationView1 = find(R.id.informationView1);
        mInformationView1.setOnClickListener(this);
        mInformationView2 = find(R.id.informationView2);
        mInformationView2.setOnClickListener(this);
    }

    @Override
    protected void onClickEvent(View v)
    {
        if (v.equals(mInformationView1))
        {

        }
        else if (v.equals(mInformationView2))
        {

        }
    }

    @Override
    protected void onCheckedChangedEvent(CompoundButton v, boolean b)
    {
        super.onCheckedChangedEvent(v, b);

        if (v.equals(mToggleButton1))
        {

        }
        else if (v.equals(mToggleButton2))
        {

        }
        else if (v.equals(mToggleButton3))
        {

        }
        else if (v.equals(mToggleButton4))
        {

        }
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {

    }
}
