package com.casting.commonmodule.session;

import android.support.v7.app.AppCompatActivity;

public interface ISessionLogin {

    SessionType getTargetSessionType();

    ISessionLoginListener getSessionLoginListener();

    AppCompatActivity getAppCompatActivity();
}
