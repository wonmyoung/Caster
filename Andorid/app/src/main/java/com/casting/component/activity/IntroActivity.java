package com.casting.component.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.casting.R;

public class IntroActivity extends BaseFCActivity {

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_intro);

        if (isRuntimePermissionAllowed())
        {
            loadLoginPage();
        }
    }

    @Override
    protected void onPermissionsDenied(String deniedPermission)
    {
        super.onPermissionsDenied(deniedPermission);

        finish();
    }


    @Override
    protected void onPermissionsGranted()
    {
        super.onPermissionsGranted();

        loadLoginPage();
    }

    @Override
    protected void onClickEvent(View v)
    {

    }

    private void loadLoginPage()
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);

                startActivity(intent);
            }
        };
        post(runnable, 1000 * 3);
    }

}
