package com.casting.commonmodule.session;

import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.model.BaseRequest;

public abstract class SessionWait extends BaseRequest {

    public abstract ISessionLoginListener<?> getSessionLoginListener();

    public abstract AppCompatActivity getAppCompatActivity();
}
