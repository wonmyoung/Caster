package com.casting.commonmodule.sns.kakao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.casting.commonmodule.WorkerThreadManager;
import com.casting.commonmodule.sns.SNSLogin;
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

public class KaKaoLoginSDK extends KakaoAdapter {

    private static class LazyHolder {
        private static KaKaoLoginSDK mInstance = new KaKaoLoginSDK();
    }

    private Application mApplication;

    private Activity mTopActivity;

    private SessionSyncCallback mSessionSyncCallback;

    public static KaKaoLoginSDK getInstance() {
        return LazyHolder.mInstance;
    }

    public void init(Application application) {

        mApplication = application;

        mSessionSyncCallback = new SessionSyncCallback();

        KakaoSDK.init(LazyHolder.mInstance);
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

    public void doSyncKaKaoAccount(final Activity activity , final SNSLogin SNSLogin) {
        EasyLog.LogMessage(">> KaKao doSyncKaKaoAccount");
        mTopActivity = activity;

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                EasyLog.LogMessage(">> KaKao onSessionClosed");

                ErrorCode errorCode = ErrorCode.valueOf(errorResult.getErrorCode());

                if (errorCode == ErrorCode.CLIENT_ERROR_CODE) {
                    EasyLog.LogMessage("-- KaKao onSessionClosed ErrorCode.CLIENT_ERROR_CODE");

                    if (SNSLogin != null) {
                        SNSLogin.onLoginError();
                    }
                } else {
                    EasyLog.LogMessage("-- KaKao onSessionClosed showLoginDialog");

                    doPrepareKaKaoAccountSession(activity , SNSLogin);
                }
            }

            @Override
            public void onNotSignedUp() {
                EasyLog.LogMessage(">> KaKao onNotSignedUp");

                if (SNSLogin != null) {
                    SNSLogin.onLoginError();
                }
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                EasyLog.LogMessage(">> KaKao onSuccess");
                Session.getCurrentSession().removeCallback(mSessionSyncCallback);

                if (SNSLogin != null) {
                    SNSLogin.setLoginResponseData(userProfile);

                    WorkerThreadManager.getInstance().runThread(SNSLogin);
                }
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                super.onFailure(errorResult);

                if (SNSLogin != null) {
                    SNSLogin.onLoginError();
                }
            }
        });
    }

    public void doCloseKaKaoAccountSession(final Runnable ... runnables) {
        EasyLog.LogMessage(">> KaKao doCloseKaKaoAccountSession");

        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                EasyLog.LogMessage(">> KaKao doCloseKaKaoAccountSession success");

                for (Runnable runnable : runnables) {
                    runnable.run();
                }
            }
        });
    }

    public void doPrepareKaKaoAccountSession(final Activity activity , final SNSLogin SNSLogin) {
        mTopActivity = activity;

        mSessionSyncCallback.setSyncLoginTask(SNSLogin);
        Session.getCurrentSession().addCallback(mSessionSyncCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private class SessionSyncCallback implements ISessionCallback {

        private SNSLogin mSNSLogin;

        @Override
        public void onSessionOpened() {
            EasyLog.LogMessage(">> sessionCallback .. onSessionOpened");

            if (mSNSLogin != null) {
                doSyncKaKaoAccount(mTopActivity , mSNSLogin);
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            EasyLog.LogMessage(">> sessionCallback .. onSessionOpenFailed");

            if (mSNSLogin != null) {
                mSNSLogin.onLoginError();
            }
        }

        public void setSyncLoginTask(SNSLogin syncLoginTask) {
            mSNSLogin = syncLoginTask;
        }
    }
}
