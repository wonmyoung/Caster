package com.casting.commonmodule.session.facebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.session.ISessionLoginListener;
import com.casting.commonmodule.session.ISessionLogoutListener;
import com.casting.commonmodule.session.ISessionSDK;
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

public class FacebookSessionSDK implements ISessionSDK<GraphResponse> {

    private CallbackManager mCallbackManager;

    private ISessionLoginListener<GraphResponse> mLoginListener;

    private static class LazyHolder {
        private static FacebookSessionSDK mInstance = new FacebookSessionSDK();
    }

    public static FacebookSessionSDK getInstance() {
        return LazyHolder.mInstance;
    }

    private FacebookSessionSDK() {
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void waitSession(AppCompatActivity a, ISessionLoginListener<GraphResponse> loginListener)
    {
        mLoginListener = loginListener;

        LoginManager.getInstance().logOut();
    }

    @Override
    public void logout(AppCompatActivity a, ISessionLogoutListener listener)
    {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void login(AppCompatActivity compatActivity,
                      ISessionLoginListener<GraphResponse> loginListener)
    {
        mLoginListener = loginListener;

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult)
            {
                EasyLog.LogMessage(">> FacebookSessionSDK onSuccess");

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        if (mLoginListener != null) {
                            mLoginListener.onLogin(response);
                        }
                    }
                });

                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,name,email,gender,birthday,picture.type(large)");

                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel()
            {
                EasyLog.LogMessage(">> FacebookSessionSDK onCancel");

                if (mLoginListener != null) {
                    mLoginListener.onError();
                }
            }

            @Override
            public void onError(FacebookException error)
            {
                EasyLog.LogMessage(">> FacebookSessionSDK onError ");
                EasyLog.LogMessage(">> FacebookSessionSDK onError " + error.getMessage());
                EasyLog.LogMessage(">> FacebookSessionSDK onError " + error.getLocalizedMessage());

                if (mLoginListener != null) {
                    mLoginListener.onError();
                }
            }

        });
        LoginManager.getInstance().logInWithReadPermissions(compatActivity , Arrays.asList("email"));
    }
}
