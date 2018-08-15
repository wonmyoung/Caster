package com.casting.commonmodule;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.thread.ThreadExecutor;

public class WorkerThreadManager {

    private ThreadExecutor mThreadExecutor;

    private static class LazyHolder {
        private static WorkerThreadManager mInstance = new WorkerThreadManager();
    }

    public static WorkerThreadManager getInstance() {
        return LazyHolder.mInstance;
    }

    public WorkerThreadManager() {
        mThreadExecutor = new ThreadExecutor(10);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseRequest> void request(T t)
    {
        if (t != null)
        {

        }
    }

    public <R extends Runnable> void runThread(R r)
    {

    }
}
