package com.casting.commonmodule.sns;

import android.app.Application;

public class SNSLoginManager {

    private static class LazyHolder {
        private static SNSLoginManager mInstance = new SNSLoginManager();
    }

    public static SNSLoginManager getInstance() {
        return LazyHolder.mInstance;
    }

    public void init(Application application) {

    }

    public void prepare(SNSLogin snsLogin)
    {

    }

    public void login(SNSLogin snsLogin)
    {

    }

    public void loginFacebook(SNSLogin snsLogin)
    {

    }

    public void loginKaKao(SNSLogin snsLogin)
    {

    }

    public void loginGoogle(SNSLogin snsLogin)
    {

    }

    private void prepareFaceBookLogin()
    {

    }

    private void prepareGoogleLogin()
    {

    }

    private void prepareKaKaoLogin()
    {

    }
}
