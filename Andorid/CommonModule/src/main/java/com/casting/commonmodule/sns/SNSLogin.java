package com.casting.commonmodule.sns;

import android.support.annotation.NonNull;

public abstract class SNSLogin implements Runnable {

    private Object mLoginResponseData;

    @Override
    public final void run()
    {
        if (mLoginResponseData != null)
        {
            try
            {
                login(mLoginResponseData);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public abstract void onLoginError();

    public abstract void login(@NonNull Object loginResponseData) throws Exception;

    public Object getLoginResponseData() {
        return mLoginResponseData;
    }

    public void setLoginResponseData(Object mLoginResponseData) {
        this.mLoginResponseData = mLoginResponseData;
    }
}
