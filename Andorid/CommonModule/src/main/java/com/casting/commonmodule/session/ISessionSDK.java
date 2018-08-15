package com.casting.commonmodule.session;

import android.support.v7.app.AppCompatActivity;

public interface ISessionSDK<R> {

    void waitSession(AppCompatActivity a, ISessionLoginListener<R> loginListener);

    void login(AppCompatActivity a, ISessionLoginListener<R> loginListener);

    void logout(AppCompatActivity a, ISessionLogoutListener logoutListener);
}
