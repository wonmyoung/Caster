package com.casting.commonmodule.session.kakao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.session.ISessionLoginListener;
import com.casting.commonmodule.session.ISessionLogoutListener;
import com.casting.commonmodule.session.ISessionSDK;
import com.casting.commonmodule.utility.EasyLog;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

public class KaKaoSessionSDK extends KakaoAdapter implements ISessionSDK<UserProfile> {

    private static class LazyHolder {
        private static KaKaoSessionSDK mInstance = new KaKaoSessionSDK();
    }

    private Application             mApplication;
    private AppCompatActivity       mTopActivity;

    private SessionSyncCallback     mSessionSyncCallback;

    public void init(Application application) {

        mApplication = application;

        mSessionSyncCallback = new SessionSyncCallback();

        KakaoSDK.init(LazyHolder.mInstance);
    }

    public static KaKaoSessionSDK getInstance() {
        return LazyHolder.mInstance;
    }

    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {

            @Override
            public Activity getTopActivity() {
                return mTopActivity;
            }

            @Override
            public Context getApplicationContext() {
                return mApplication;
            }
        };
    }

    @Override
    public ISessionConfig getSessionConfig() {
        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes() {
                return new AuthType[]{AuthType.KAKAO_TALK};
            }

            @Override
            public boolean isUsingWebviewTimer() {
                return false;
            }

            @Override
            public ApprovalType getApprovalType() {
                return ApprovalType.INDIVIDUAL;
            }

            @Override
            public boolean isSaveFormData() {
                return true;
            }
        };
    }

    @Override
    public void login(final AppCompatActivity activity , final ISessionLoginListener<UserProfile> loginListener)
    {
        EasyLog.LogMessage(">> KaKao login");

        mTopActivity = activity;

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult)
            {
                EasyLog.LogMessage(">> KaKao onSessionClosed");

                ErrorCode errorCode = ErrorCode.valueOf(errorResult.getErrorCode());

                if (errorCode == ErrorCode.CLIENT_ERROR_CODE)
                {
                    EasyLog.LogMessage("-- KaKao onSessionClosed ErrorCode.CLIENT_ERROR_CODE");

                    if (loginListener != null) {
                        loginListener.onError();
                    }
                }
                else
                {
                    EasyLog.LogMessage("-- KaKao onSessionClosed showLoginDialog");

                    waitSession(activity , loginListener);
                }
            }

            @Override
            public void onNotSignedUp()
            {
                EasyLog.LogMessage(">> KaKao onNotSignedUp");

                if (loginListener != null) {
                    loginListener.onError();
                }
            }

            @Override
            public void onSuccess(UserProfile userProfile)
            {
                EasyLog.LogMessage(">> KaKao onSuccess");

                Session.getCurrentSession().removeCallback(mSessionSyncCallback);

                if (loginListener != null) {
                    loginListener.onLogin(userProfile);
                }
            }

            @Override
            public void onFailure(ErrorResult errorResult)
            {
                super.onFailure(errorResult);

                if (loginListener != null) {
                    loginListener.onError();
                }
            }
        });
    }

    @Override
    public void logout(AppCompatActivity a, ISessionLogoutListener listener)
    {
        EasyLog.LogMessage(">> KaKao logout");

        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout()
            {
                EasyLog.LogMessage(">> KaKao logout success");

            }
        });
    }

    @Override
    public void waitSession
            (final AppCompatActivity activity ,
             final ISessionLoginListener<UserProfile> loginListener)
    {
        mTopActivity = activity;

        mSessionSyncCallback.setSyncLoginTask(loginListener);

        Session.getCurrentSession().addCallback(mSessionSyncCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private class SessionSyncCallback implements ISessionCallback {

        private ISessionLoginListener<UserProfile> mLoginListener;

        @Override
        public void onSessionOpened()
        {
            EasyLog.LogMessage(">> sessionCallback .. onSessionOpened");

            if (mLoginListener != null) {
                login(mTopActivity , mLoginListener);
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception)
        {
            EasyLog.LogMessage(">> sessionCallback .. onSessionOpenFailed");

            if (mLoginListener != null) {
                mLoginListener.onError();
            }
        }

        private void setSyncLoginTask(ISessionLoginListener<UserProfile> loginListener) {
            mLoginListener = loginListener;
        }
    }
}
