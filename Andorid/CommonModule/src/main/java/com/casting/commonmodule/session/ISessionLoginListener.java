package com.casting.commonmodule.session;

public interface ISessionLoginListener<R> {

    void onLogin(R r);

    void onError(Object ... o);
}
