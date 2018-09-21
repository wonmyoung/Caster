package com.casting.commonmodule.session;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.casting.commonmodule.IRequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.session.facebook.FacebookSessionSDK;
import com.casting.commonmodule.session.google.GoogleSessionSDK;
import com.casting.commonmodule.session.kakao.KaKaoSessionSDK;
import com.casting.commonmodule.utility.EasyLog;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.util.LinkedList;

public class SessionRequestHandler implements IRequestHandler {

    private LinkedList<String> HashKeyList = new LinkedList<>();

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

        AppEventsLogger.activateApp(application);

        try
        {
            PackageManager packageManager = application.getPackageManager();

            String packageName = application.getPackageName();

            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());

                String hashKey = Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT);

                EasyLog.LogMessage(this, "++ init hashKey = " + hashKey);

                HashKeyList.add(hashKey);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
