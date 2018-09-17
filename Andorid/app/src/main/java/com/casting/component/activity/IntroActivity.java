package com.casting.component.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.model.Member;
import com.casting.model.global.ActiveMember;
import com.casting.model.request.Login;

public class IntroActivity extends BaseFCActivity implements IResponseListener {

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_intro);

        if (isRuntimePermissionAllowed())
        {
            load();
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

        load();
    }

    private void load()
    {
        CommonPreference commonPreference = CommonPreference.getInstance();

        String memberEmail = commonPreference.getSharedValueByString(MEMBER_EMAIL, "");
        String memberPw = commonPreference.getSharedValueByString(MEMBER_PW, "");

        EasyLog.LogMessage(this, "++ load memberEmail = ", memberEmail);
        EasyLog.LogMessage(this, "++ load memberPw = ", memberPw);

        if (!TextUtils.isEmpty(memberEmail) && !TextUtils.isEmpty(memberPw))
        {
            Login login = new Login();
            login.setEmailAddress(memberEmail);
            login.setPassword(memberPw);
            login.setResponseListener(this);

            RequestHandler.getInstance().request(login);
        }
        else
        {
            loadLoginPage();
        }
    }

    private void loadLoginPage()
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);

                startActivity(intent);

                finish();
            }
        };
        post(runnable, 1000 * 3);
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        BaseRequest request = response.getSourceRequest();

        if (request.isRight(Login.class))
        {
            Login login = (Login) request;

            if (login.isSuccessResponse())
            {
                Member member = (Member) response.getResponseModel();

                CommonPreference commonPreference = CommonPreference.getInstance();
                commonPreference.setSharedValueByString(MEMBER_EMAIL, member.getEmail());
                commonPreference.setSharedValueByString(MEMBER_PW, member.getPassWord());
                commonPreference.setSharedValueByString(AUTH_TOKEN, member.getAuthToken());

                ActiveMember.getInstance().setMember(member);

                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);

                finish();
            }
            else
            {
                loadLoginPage();
            }
        }
    }
}
