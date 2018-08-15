package com.casting.commonmodule.session.google;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.casting.commonmodule.session.ISessionLoginListener;
import com.casting.commonmodule.session.ISessionLogoutListener;
import com.casting.commonmodule.session.ISessionSDK;
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

public class GoogleSessionSDK implements ISessionSDK<GoogleSignInAccount> {

    public static final int GOOGLE_ACCOUNT_REQUEST = 9001;

    private GoogleSignInOptions     mGoogleSignInOptions;
    private GoogleApiClient         mGoogleApiClient;

    private ISessionLoginListener<GoogleSignInAccount> mSessionLoginListener;

    private static class LazyHolder {
        private static GoogleSessionSDK mInstance = new GoogleSessionSDK();
    }

    public static GoogleSessionSDK getInstance() {
        return GoogleSessionSDK.LazyHolder.mInstance;
    }

    private GoogleSessionSDK() {
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    }

    @Override
    public void waitSession(final AppCompatActivity activity ,
                            final ISessionLoginListener<GoogleSignInAccount> loginListener)
    {
        setGoogleApiClient(activity);

        mSessionLoginListener = loginListener;

        if (mGoogleApiClient.isConnected())
        {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }

    @Override
    public void logout(final AppCompatActivity compatActivity ,
                       final ISessionLogoutListener loginListener) {

        setGoogleApiClient(compatActivity);

        try {
            if (mGoogleApiClient.isConnected())
            {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status)
                    {
                        EasyLog.LogMessage(">> doCloseGoogleAccountSession .. onResult");


                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login
            (AppCompatActivity compatActivity ,
             ISessionLoginListener<GoogleSignInAccount> loginListener)
    {
        mSessionLoginListener = loginListener;

        setGoogleApiClient(compatActivity);

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (optionalPendingResult.isDone())
        {
            GoogleSignInResult googleSignInResult = optionalPendingResult.get();

            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

            if (mSessionLoginListener != null) {
                mSessionLoginListener.onLogin(googleSignInAccount);
            }

        }
        else
        {
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {

                    if (googleSignInResult.isSuccess())
                    {
                        EasyLog.LogMessage("++ handleGoogleLoginResult isSuccess");

                        GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                        if (mSessionLoginListener != null) {
                            mSessionLoginListener.onLogin(googleSignInAccount);
                        }
                    }
                    else
                    {
                        EasyLog.LogMessage("++ handleGoogleLoginResult fail..");

                        if (mGoogleApiClient != null) {
                            mGoogleApiClient.disconnect();
                        }

                        if (mSessionLoginListener != null) {
                            mSessionLoginListener.onError();
                        }
                    }
                }
            });
        }
    }

    private void setGoogleApiClient(AppCompatActivity compatActivity) {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(compatActivity);
            mGoogleApiClient.disconnect();
        }

        mGoogleApiClient = getGoogleApiClient(compatActivity);
    }


    private GoogleSignInOptions getGoogleSignInOptions() {
        return mGoogleSignInOptions;
    }

    private GoogleApiClient getGoogleApiClient(AppCompatActivity appCompatActivity)
    {
        return new GoogleApiClient.Builder(appCompatActivity)
                                  .enableAutoManage(appCompatActivity , getGoogleFailedListener(appCompatActivity))
                                  .addApi(Auth.GOOGLE_SIGN_IN_API , mGoogleSignInOptions)
                                  .build();
    }

    private GoogleApiClient.OnConnectionFailedListener getGoogleFailedListener(final AppCompatActivity appCompatActivity) {
        return new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                EasyLog.LogMessage(">> onConnectionFailed");

                if (mGoogleApiClient != null) {
                    mGoogleApiClient.stopAutoManage(appCompatActivity);
                    mGoogleApiClient.disconnect();

                    if (mSessionLoginListener != null) {
                        mSessionLoginListener.onError();
                    }
                }
            }
        };
    }

    private View.OnClickListener getGoogleAccountSignButtinListener(final AppCompatActivity compatActivity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoogleApiClient(compatActivity);

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

                compatActivity.startActivityForResult(signInIntent, GOOGLE_ACCOUNT_REQUEST);

                mGoogleApiClient.connect();
            }
        };
    }
}
