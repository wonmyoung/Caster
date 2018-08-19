package com.casting.commonmodule;

import android.app.Application;

import com.casting.commonmodule.db.LocalDBRequest;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.session.SessionLogin;
import com.casting.commonmodule.session.SessionLogout;
import com.casting.commonmodule.session.SessionRequestHandler;
import com.casting.commonmodule.session.SessionWait;
import com.casting.commonmodule.thread.ThreadExecutor;

import java.util.concurrent.Executors;

public class RequestHandler<R extends BaseRequest> implements IRequestHandler<R> {

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

    @Override
    @SuppressWarnings("unchecked")
    public void request(R r)
    {
        if (r != null)
        {

            if (r instanceof SessionLogin ||
                r instanceof SessionLogout ||
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
            else if (r instanceof LocalDBRequest)
            {
                LocalDBRequest localDBRequest = (LocalDBRequest) r;

                Executors.newSingleThreadExecutor().execute(localDBRequest);
            }
        }
    }
}
