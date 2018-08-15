package com.casting.commonmodule.sns.facebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.sns.SNSLogin;
import com.casting.commonmodule.utility.EasyLog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLoginSDK implements FacebookCallback<LoginResult> {

    private CallbackManager mCallbackManager;

    private SNSLogin mSNSLogin;

    private static class LazyHolder {
        private static FacebookLoginSDK mInstance = new FacebookLoginSDK();
    }

    public static FacebookLoginSDK getInstance() {
        return LazyHolder.mInstance;
    }

    private FacebookLoginSDK() {
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onSuccess(LoginResult loginResult)
    {
        EasyLog.LogMessage(">> FacebookLoginSDK onSuccess");

        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                if (mSNSLogin != null) {
                    mSNSLogin.setLoginResponseData(response);
                    mSNSLogin.run();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void onCancel()
    {
        EasyLog.LogMessage(">> FacebookLoginSDK onCancel");

        if (mSNSLogin != null) {
            mSNSLogin.onLoginError();
        }
    }

    @Override
    public void onError(FacebookException error)
    {
        EasyLog.LogMessage(">> FacebookLoginSDK onError ");
        EasyLog.LogMessage(">> FacebookLoginSDK onError " + error.getMessage());
        EasyLog.LogMessage(">> FacebookLoginSDK onError " + error.getLocalizedMessage());

        if (mSNSLogin != null) {
            mSNSLogin.onLoginError();
        }
    }

    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }

    public void setLoginTask(SNSLogin SNSLogin) {
        mSNSLogin = SNSLogin;
    }

    public void doCloseFacebookAccountSession(Runnable runnable)
    {
        LoginManager.getInstance().logOut();

        runnable.run();
    }

    public void doPrepareFacebookAccountSession(SNSLogin SNSLogin)
    {
        LoginManager.getInstance().logOut();

        setLoginTask(SNSLogin);
    }

    public void doSyncFacebookAccount(AppCompatActivity compatActivity , SNSLogin SNSLogin)
    {
        setLoginTask(SNSLogin);

        LoginManager.getInstance().registerCallback(FacebookLoginSDK.getInstance().getCallbackManager(),
                                                    FacebookLoginSDK.getInstance());
        LoginManager.getInstance().logInWithReadPermissions(compatActivity , Arrays.asList("email"));
    }
}
