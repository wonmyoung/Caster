package com.casting.model.request;

import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.session.ISessionLogin;
import com.casting.commonmodule.session.ISessionLoginListener;
import com.casting.commonmodule.session.SessionType;
import com.facebook.GraphResponse;

import java.lang.ref.WeakReference;

public class RequestFacebookSession extends BaseRequest implements ISessionLogin<GraphResponse> {

    WeakReference<AppCompatActivity> ActivityReference;

    @Override
    public SessionType getTargetSessionType() {
        return SessionType.FACEBOOK;
    }

    @Override
    public ISessionLoginListener<GraphResponse> getSessionLoginListener()
    {
        return new ISessionLoginListener<GraphResponse>() {
            @Override
            public void onLogin(GraphResponse graphResponse)
            {

            }

            @Override
            public void onError(Object... o)
            {

            }
        };
    }


    @Override
    public AppCompatActivity getAppCompatActivity()
    {
        return (ActivityReference != null ? ActivityReference.get() : null);
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity)
    {
        ActivityReference = new WeakReference<>(appCompatActivity);
    }
}
