package com.casting.commonmodule.session;

import android.support.v7.app.AppCompatActivity;

public interface ISessionLogin<R> {

    SessionType getTargetSessionType();

    ISessionLoginListener<R> getSessionLoginListener();

    AppCompatActivity getAppCompatActivity();
}
