package com.casting;

import android.app.Application;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.NetworkRequestHandler;
import com.casting.commonmodule.network.base.URLGeneratorStrategy;
import com.casting.model.request.Login;
import com.casting.model.request.RegisterMember;

public class FutureCasting extends Application implements URLGeneratorStrategy {

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkRequestHandler.getInstance().setURLGeneratorStrategy(this);
    }

    @Override
    public String create(BaseRequest request) {


        if (request.isRight(Login.class))
        {

        }
        else if (request.isRight(RegisterMember.class))
        {

        }

        return null;
    }
}
