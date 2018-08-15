package com.casting.commonmodule;

import android.app.Application;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.session.ISessionLogin;
import com.casting.commonmodule.session.ISessionLogout;
import com.casting.commonmodule.session.SessionRequestHandler;
import com.casting.commonmodule.session.SessionWait;
import com.casting.commonmodule.thread.ThreadExecutor;

public class RequestHandler {

    private ThreadExecutor mThreadExecutor;

    private static class LazyHolder {
        private static RequestHandler mInstance = new RequestHandler();
    }

    public static RequestHandler getInstance() {
        return LazyHolder.mInstance;
    }

    private RequestHandler() {
        mThreadExecutor = new ThreadExecutor(10);
    }

    public void init(Application a)
    {
        SessionRequestHandler.getInstance().init(a);
    }

    @SuppressWarnings("unchecked")
    public <R extends BaseRequest> void request(R r)
    {
        if (r != null)
        {

            if (r instanceof ISessionLogin ||
                r instanceof ISessionLogout ||
                r instanceof SessionWait)
            {
                SessionRequestHandler.getInstance().request(r);
            }
            else if (r instanceof NetworkRequest)
            {
                NetworkRequest networkRequest = (NetworkRequest) r;

                if (mThreadExecutor != null) {
                    mThreadExecutor.execute(networkRequest);
                }
            }

        }
    }
}
