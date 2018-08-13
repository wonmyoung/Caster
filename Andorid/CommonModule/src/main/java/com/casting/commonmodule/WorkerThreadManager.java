package com.casting.commonmodule;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.thread.MultiThreadExecutor;

public class WorkerThreadManager {

    private MultiThreadExecutor mMultiThreadExecutor;

    private static class LazyHolder {
        private static WorkerThreadManager mInstance = new WorkerThreadManager();
    }

    public static WorkerThreadManager getInstance() {
        return LazyHolder.mInstance;
    }

    public WorkerThreadManager() {
        mMultiThreadExecutor = new MultiThreadExecutor(10);
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
