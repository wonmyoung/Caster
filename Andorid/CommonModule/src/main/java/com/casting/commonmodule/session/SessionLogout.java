package com.casting.commonmodule.session;

import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.model.BaseRequest;

public abstract class SessionLogout extends BaseRequest<SessionResponse> {

    public abstract SessionType getTargetSessionType();

    public abstract ISessionLogoutListener getSessionLogoutListener();

    public abstract AppCompatActivity getAppCompatActivity();
}
