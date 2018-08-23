package com.casting.commonmodule.view.component;

import android.app.Application;

import com.casting.commonmodule.network.NetworkRequestHandler;
import com.casting.commonmodule.network.NetworkState;
import com.casting.commonmodule.network.base.URLGeneratorStrategy;
import com.casting.commonmodule.session.SessionRequestHandler;

public abstract class CommonApplication extends Application implements URLGeneratorStrategy {

    @Override
    public void onCreate()
    {
        super.onCreate();

        NetworkState.getInstance().registerReceiver(this);

        NetworkRequestHandler.getInstance().setURLGeneratorStrategy(this);

        SessionRequestHandler.getInstance().init(this);
    }
}
