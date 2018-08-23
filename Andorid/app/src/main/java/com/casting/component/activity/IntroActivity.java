package com.casting.component.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.casting.FutureCasting;
import com.casting.R;
import com.casting.commonmodule.network.NetworkRequestHandler;
import com.casting.component.fragment.LoginFragment;
import com.casting.component.fragment.RegisterFragment;

public class IntroActivity extends BaseFCActivity {

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.layout_intro_activity);

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
}
