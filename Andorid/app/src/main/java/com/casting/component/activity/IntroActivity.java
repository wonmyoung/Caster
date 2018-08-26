package com.casting.component.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.casting.R;
import com.casting.component.fragment.LoginFragment;

public class IntroActivity extends BaseFCActivity {

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_intro);

        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                LoginFragment loginFragment = new LoginFragment();

                replaceFragment(R.id.intro_layoutFrame, loginFragment);
            }
        };
        post(runnable, 1000 * 3);
    }

    @Override
    protected void onClickEvent(View v)
    {

    }

    @Override
    protected void onFragmentVisible(Fragment fragment)
    {
        super.onFragmentVisible(fragment);

        if (fragment == null)
        {
            finish();
        }
    }
}
