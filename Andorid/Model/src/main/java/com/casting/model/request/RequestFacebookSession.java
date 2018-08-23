package com.casting.model.request;

import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.session.SessionLogin;
import com.casting.commonmodule.session.ISessionLoginListener;
import com.casting.commonmodule.session.SessionResponse;
import com.casting.commonmodule.session.SessionType;
import com.casting.model.Member;
import com.facebook.GraphResponse;

import java.lang.ref.WeakReference;

public class RequestFacebookSession extends SessionLogin<GraphResponse> {

    private WeakReference<AppCompatActivity> ActivityReference;

    @Override
    public SessionType getTargetSessionType() {
        return SessionType.FACEBOOK;
    }

    @Override
    public ISessionLoginListener<GraphResponse> getSessionLoginListener()
    {
        return new ISessionLoginListener<GraphResponse>() {
            @Override
            public void onLogin(GraphResponse graphResponse)
            {

                Member member = new Member();

                SessionResponse<Member> sessionResponse = new SessionResponse<>();
                sessionResponse.setResponseCode(1);
                sessionResponse.setSessionType(SessionType.FACEBOOK);
                sessionResponse.setResponseModel(member);

                IResponseListener responseListener = getResponseListener();

                if (responseListener != null) {
                    responseListener.onThreadResponseListen(sessionResponse);
                }
            }

            @Override
            public void onError(Object... o)
            {

            }
        };
    }


    @Override
    public AppCompatActivity getAppCompatActivity()
    {
        return (ActivityReference != null ? ActivityReference.get() : null);
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity)
    {
        ActivityReference = new WeakReference<>(appCompatActivity);
    }
}
