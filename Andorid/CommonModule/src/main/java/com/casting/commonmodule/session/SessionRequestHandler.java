package com.casting.commonmodule.session;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.IRequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.session.facebook.FacebookSessionSDK;
import com.casting.commonmodule.session.google.GoogleSessionSDK;
import com.casting.commonmodule.session.kakao.KaKaoSessionSDK;
import com.facebook.FacebookSdk;

public class SessionRequestHandler implements IRequestHandler {

    private static class LazyHolder {
        private static SessionRequestHandler mInstance = new SessionRequestHandler();
    }

    public static SessionRequestHandler getInstance() {
        return LazyHolder.mInstance;
    }

    public void init(Application application)
    {
        // TODO
        // KaKaoSessionSDK.getInstance().init(application);

        FacebookSdk.sdkInitialize(application);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void request(BaseRequest r)
    {
        if (r instanceof SessionLogin)
        {
            SessionLogin s = (SessionLogin) r;

            SessionType sessionType = s.getTargetSessionType();

            ISessionSDK iSessionSDK = sessionType.getSessionLoginSDK();
            iSessionSDK.login(s.getAppCompatActivity(), s.getSessionLoginListener());
        }
        else if (r instanceof SessionLogout)
        {
            SessionLogout s = (SessionLogout) r;

            SessionType sessionType = s.getTargetSessionType();

            ISessionSDK iSessionSDK = sessionType.getSessionLoginSDK();
            iSessionSDK.logout(s.getAppCompatActivity(), s.getSessionLogoutListener());
        }
        else if (r instanceof SessionWait)
        {
            SessionWait s = (SessionWait) r;

            AppCompatActivity a = s.getAppCompatActivity();

            ISessionLoginListener listener = s.getSessionLoginListener();

            KaKaoSessionSDK.getInstance().waitSession(a, listener);

            FacebookSessionSDK.getInstance().waitSession(a, listener);

            GoogleSessionSDK.getInstance().waitSession(a, listener);
        }
    }
}
