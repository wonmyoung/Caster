package com.casting;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.session.SessionType;
import com.casting.commonmodule.view.component.CommonApplication;
import com.casting.model.request.Login;
import com.casting.model.request.RegisterMember;
import com.casting.model.request.RequestCast;
import com.casting.model.request.RequestCastList;
import com.casting.model.request.RequestTimeLine;
import com.casting.model.request.RequestTimeLineList;

public class FutureCasting extends CommonApplication {

    public static final String HTTP_PROTOCOL    = "http://";
    public static final String SERVER_DOMAIN    = "voltsoftware.co.kr";
    public static final String SERVER_PORT      = ":8080/";
    public static final String SERVER_TARGET    = "edu/";

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
            Login login = (Login) request;

            stringBuilder.append(createLoginUrl(login));
        }
        else if (request.isRight(RegisterMember.class))
        {
            stringBuilder.append("/account/join");
        }
        else if (request.isRight(RequestCastList.class))
        {
            RequestCastList requestCastList = (RequestCastList) request;

            stringBuilder.append(createCastListUrl(requestCastList));
        }
        else if (request.isRight(RequestCast.class))
        {

        }
        else if (request.isRight(RequestCastList.class))
        {

        }
        else if (request.isRight(RequestTimeLine.class))
        {

        }
        else if (request.isRight(RequestTimeLineList.class))
        {

        }

        return stringBuilder.toString();
    }

    private String createLoginUrl(Login login)
    {
        SessionType sessionType = login.getSessionType();

        if (sessionType == SessionType.FACEBOOK)
        {
            return "/auth/facebook";
        }
        else if (sessionType == SessionType.KAKAO)
        {
            return null;
        }
        else if (sessionType == SessionType.GOOGLE)
        {
            return null;
        }
        else
        {
            return "/account/login";
        }
    }

    private String createCastListUrl(RequestCastList requestCastList)
    {
        switch (requestCastList.getOrder())
        {
            case Available:
                return "/survey/surveyInfo";

            case Popular:
                return null;

            case RewardBig:
                return null;

            default:
                return null;
        }
    }
}
