package com.casting.model.request;

import android.support.v7.app.AppCompatActivity;

import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.session.SessionLogin;
import com.casting.commonmodule.session.ISessionLoginListener;
import com.casting.commonmodule.session.SessionResponse;
import com.casting.commonmodule.session.SessionType;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Member;
import com.facebook.GraphResponse;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class RequestFacebookSession extends SessionLogin<GraphResponse> {

    private WeakReference<AppCompatActivity> ActivityReference;

    @Override
    public SessionType getTargetSessionType()
    {
        return SessionType.FACEBOOK;
    }

    @Override
    public ISessionLoginListener<GraphResponse> getSessionLoginListener()
    {
        return new ISessionLoginListener<GraphResponse>() {
            @Override
            public void onLogin(GraphResponse graphResponse)
            {

                JSONObject jsonObject = graphResponse.getJSONObject();

                if (jsonObject != null)
                {
                    EasyLog.LogMessage(this, "++ onLogin jsonObject = " + jsonObject.toString());

                    String memberFacebookId = UtilityData.convertStringFromJSON(jsonObject, "id");
                    String memberEmail = UtilityData.convertStringFromJSON(jsonObject, "email");
                    String memberFacebookPw = UtilityData.createMD5HashCode("facebook"+memberFacebookId);
                    String memberName = UtilityData.convertStringFromJSON(jsonObject, "name");

                    JSONObject picInfo1 = UtilityData.convertJsonFromJson(jsonObject, "picture");
                    JSONObject picInfo2 = UtilityData.convertJsonFromJson(picInfo1, "data");
                    String thumbNailPath = UtilityData.convertStringFromJSON(picInfo2, "url");

                    EasyLog.LogMessage(this, "++ onLogin memberFacebookId = " + memberFacebookId);
                    EasyLog.LogMessage(this, "++ onLogin memberFacebookPw = " + memberFacebookPw);
                    EasyLog.LogMessage(this, "++ onLogin memberName = " + memberName);
                    EasyLog.LogMessage(this, "++ onLogin thumbNailPath = " + thumbNailPath);

                    Member member = new Member();
                    member.setUserId(memberFacebookId);
                    member.setEmail(memberEmail);
                    member.setPassWord(memberFacebookPw);
                    member.setUserName(memberName);
                    member.setUserPicThumbnail(thumbNailPath);

                    SessionResponse<Member> sessionResponse = new SessionResponse<>();
                    sessionResponse.setSourceRequest(RequestFacebookSession.this);
                    sessionResponse.setResponseCode(1);
                    sessionResponse.setSessionType(SessionType.FACEBOOK);
                    sessionResponse.setResponseModel(member);

                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null) {
                        responseListener.onThreadResponseListen(sessionResponse);
                    }
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
