package com.casting.commonmodule.session;

import com.casting.commonmodule.session.facebook.FacebookSessionSDK;
import com.casting.commonmodule.session.google.GoogleSessionSDK;
import com.casting.commonmodule.session.kakao.KaKaoSessionSDK;

public enum SessionType {

    KAKAO(KaKaoSessionSDK.getInstance()),
    FACEBOOK(FacebookSessionSDK.getInstance()),
    GOOGLE(GoogleSessionSDK.getInstance());

    private ISessionSDK mSessionLoginSDK;

    SessionType(ISessionSDK s)
    {
        mSessionLoginSDK = s;
    }

    public ISessionSDK getSessionLoginSDK() {
        return mSessionLoginSDK;
    }
}
