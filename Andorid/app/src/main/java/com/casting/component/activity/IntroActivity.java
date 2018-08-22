package com.casting.component.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.casting.R;
import com.casting.component.fragment.LoginFragment;
import com.casting.component.fragment.RegisterFragment;

public class IntroActivity extends BaseFCActivity {

    private LoginFragment       mLoginFragment;
    private RegisterFragment    mRegisterFragment;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.layout_intro_activity);

        mLoginFragment = new LoginFragment();

        mRegisterFragment = new RegisterFragment();

        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                replaceFragment(R.id.intro_layoutFrame, mLoginFragment);
            }
        };
        post(runnable, 1000 * 3);
    }

    @Override
    protected void onClickEvent(View v)
    {
        switch (v.getId())
        {
            case R.id.registerFrameButton0:
            {
                replaceFragment(R.id.intro_layoutFrame, mRegisterFragment);
                break;
            }
        }
    }
}
