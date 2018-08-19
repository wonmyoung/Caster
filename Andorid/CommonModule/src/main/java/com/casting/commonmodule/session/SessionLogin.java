package com.casting.commonmodule.session;

import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.model.BaseRequest;

public abstract class SessionLogin<R> extends BaseRequest<SessionResponse> {

    public abstract SessionType getTargetSessionType();

    public abstract ISessionLoginListener<R> getSessionLoginListener();

    public abstract AppCompatActivity getAppCompatActivity();
}
