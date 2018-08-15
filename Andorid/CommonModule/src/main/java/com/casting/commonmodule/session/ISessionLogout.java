package com.casting.commonmodule.session;

import android.support.v7.app.AppCompatActivity;

public interface ISessionLogout {

    SessionType getTargetSessionType();

    ISessionLogoutListener getSessionLogoutListener();

    AppCompatActivity getAppCompatActivity();
}
