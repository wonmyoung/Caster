package com.casting.commonmodule;

import android.app.Application;
import android.util.Log;

import com.casting.commonmodule.db.LocalDBRequest;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.NetworkState;
import com.casting.commonmodule.session.SessionLogin;
import com.casting.commonmodule.session.SessionLogout;
import com.casting.commonmodule.session.SessionRequestHandler;
import com.casting.commonmodule.session.SessionWait;
import com.casting.commonmodule.thread.ThreadExecutor;
import com.casting.commonmodule.view.component.CommonApplication;

import java.util.concurrent.Executors;

public class RequestHandler implements IRequestHandler<BaseRequest> {

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

    @Override
    @SuppressWarnings("unchecked")
    public void request(BaseRequest r)
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
