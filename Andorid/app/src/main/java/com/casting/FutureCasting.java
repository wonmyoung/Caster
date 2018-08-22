package com.casting;

import android.app.Application;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.NetworkRequestHandler;
import com.casting.commonmodule.network.base.URLGeneratorStrategy;
import com.casting.model.request.Login;
import com.casting.model.request.RegisterMember;
import com.casting.model.request.RequestCast;
import com.casting.model.request.RequestCastList;

public class FutureCasting extends Application implements URLGeneratorStrategy {

    public static final String HTTP_PROTOCOL    = "http://";
    public static final String SERVER_DOMAIN    = "voltsoftware.co.kr";
    public static final String SERVER_PORT      = ":8080/";
    public static final String SERVER_TARGET    = "edu/";

    @Override
    public void onCreate()
    {
        super.onCreate();

        NetworkRequestHandler.getInstance().setURLGeneratorStrategy(this);
    }

    @Override
    public String create(BaseRequest request)
    {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HTTP_PROTOCOL);
        stringBuilder.append(SERVER_DOMAIN);
        stringBuilder.append(SERVER_PORT);
        stringBuilder.append(SERVER_TARGET);

        if (request.isRight(Login.class))
        {

        }
        else if (request.isRight(RegisterMember.class))
        {

        }
        else if (request.isRight(RequestCastList.class))
        {

        }
        else if (request.isRight(RequestCast.class))
        {

        }

        return stringBuilder.toString();
    }
}
