package com.casting.commonmodule.sns.google;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.casting.commonmodule.sns.SNSLogin;
import com.casting.commonmodule.sns.facebook.FacebookLoginSDK;
import com.casting.commonmodule.utility.EasyLog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class GoogleLoginSDK implements ResultCallback<GoogleSignInResult> {

    public static final int GOOGLE_ACCOUNT_REQUEST = 9001;

    private GoogleSignInOptions mGoogleSignInOptions;
    private GoogleApiClient mGoogleApiClient;

    private SNSLogin mSNSLogin;

    private static class LazyHolder {
        private static GoogleLoginSDK mInstance = new GoogleLoginSDK();
    }

    public static GoogleLoginSDK getInstance() {
        return GoogleLoginSDK.LazyHolder.mInstance;
    }

    private GoogleLoginSDK() {
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    }

    private void init(AppCompatActivity compatActivity) {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(compatActivity);
            mGoogleApiClient.disconnect();
        }

        mGoogleApiClient = getGoogleApiClient(compatActivity);
    }

    private void setLoginTask(SNSLogin SNSLogin) {
        mSNSLogin = SNSLogin;
    }

    public void doCloseGoogleAccountSession(AppCompatActivity compatActivity , final Runnable ... runnables) {
        init(compatActivity);

        try {
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        EasyLog.LogMessage(">> doCloseGoogleAccountSession .. onResult");

                        for (Runnable runnable : runnables) {
                            runnable.run();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doPrepareGoogleAccountSession(final AppCompatActivity activity , final SNSLogin SNSLogin) {
        init(activity);

        setLoginTask(SNSLogin);

        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }

    public void doSyncGoogleAccount(AppCompatActivity compatActivity , SNSLogin SNSLogin) {
        setLoginTask(SNSLogin);

        doSyncGoogleAccount(compatActivity);
    }

    public void doSyncGoogleAccount(AppCompatActivity compatActivity) {
        EasyLog.LogMessage(">> doSyncGoogleAccount ");

        init(compatActivity);

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (optionalPendingResult.isDone()) {
            GoogleSignInResult googleSignInResult = optionalPendingResult.get();
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

            mSNSLogin.setLoginResponseData(googleSignInAccount);
            mSNSLogin.run();
        } else {
            optionalPendingResult.setResultCallback(this);
        }
    }

    public GoogleSignInOptions getGoogleSignInOptions() {
        return mGoogleSignInOptions;
    }

    public GoogleApiClient getGoogleApiClient(AppCompatActivity appCompatActivity) {
        return new GoogleApiClient.Builder(appCompatActivity)
                                  .enableAutoManage(appCompatActivity , getGoogleFailedListener(appCompatActivity))
                                  .addApi(Auth.GOOGLE_SIGN_IN_API , mGoogleSignInOptions)
                                  .build();
    }

    public GoogleApiClient.OnConnectionFailedListener getGoogleFailedListener(final AppCompatActivity appCompatActivity) {
        return new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                EasyLog.LogMessage(">> onConnectionFailed");

                if (mGoogleApiClient != null) {
                    mGoogleApiClient.stopAutoManage(appCompatActivity);
                    mGoogleApiClient.disconnect();

                    if (mSNSLogin != null) {
                        mSNSLogin.onLoginError();
                    }
                }
            }
        };
    }

    public View.OnClickListener getGoogleAccountSignButtinListener(final AppCompatActivity compatActivity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init(compatActivity);

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                compatActivity.startActivityForResult(signInIntent, GOOGLE_ACCOUNT_REQUEST);

                mGoogleApiClient.connect();
            }
        };
    }

    public void onHandleGoogleLoginResult(GoogleSignInResult googleSignInResult) {
        EasyLog.LogMessage(">> onHandleGoogleLoginResult");

        if (googleSignInResult.isSuccess()) {
            EasyLog.LogMessage("++ onHandleGoogleLoginResult isSuccess");

            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

            if (mSNSLogin != null) {
                mSNSLogin.setLoginResponseData(googleSignInAccount);
                mSNSLogin.run();
            }
        } else {
            EasyLog.LogMessage("++ onHandleGoogleLoginResult fail..");

            if (mGoogleApiClient != null) {
                mGoogleApiClient.disconnect();
            }
            if (mSNSLogin != null) {
                mSNSLogin.onLoginError();
            }
        }
    }

    @Override
    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
        EasyLog.LogMessage(">> onResult ");

        onHandleGoogleLoginResult(googleSignInResult);
    }
}
