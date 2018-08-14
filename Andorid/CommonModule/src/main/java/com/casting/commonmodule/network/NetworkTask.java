package com.casting.commonmodule.network;

import android.text.TextUtils;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.thread.SafeThread;
import com.casting.commonmodule.thread.ThreadType;

public class NetworkTask<R extends BaseRequest> extends SafeThread {

    private R mBaseRequest;

    public NetworkTask(R request) {
        super(ThreadType.NETWORK_THREAD);

        mBaseRequest = request;
    }

    @Override
    public void runTask() throws Exception
    {
        NetworkThreadManager.getInstance().requestHttpExecute(mBaseRequest);
    }

    @Override
    public String getThreadName() {
        if (TextUtils.isEmpty(super.getThreadName()))
        {
            return mBaseRequest.getTargetClass().getSimpleName();
        }

        return super.getThreadName();
    }
}
