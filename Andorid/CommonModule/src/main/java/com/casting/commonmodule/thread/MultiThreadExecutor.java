package com.casting.commonmodule.thread;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThreadExecutor extends ThreadPoolExecutor {

    private static final int MAX_THREAD_COUNT = 100;
    private static final int THREAD_TIMEOUT = 30;

    private LinkedBlockingQueue<SafeThread> preservedTasks = new LinkedBlockingQueue<>();

    public MultiThreadExecutor(int concurrentThreadCount) {
        super(concurrentThreadCount , MAX_THREAD_COUNT , THREAD_TIMEOUT , TimeUnit.SECONDS , new LinkedBlockingQueue<Runnable>());
    }

    public void executeMultiThread(Runnable... runnables) {
        for (Runnable task : runnables) {
            if (task instanceof SafeThread) {
                preservedTasks.add((SafeThread) task);
            }
            execute(task);
        }
    }

    public void dequeuePreservedTask(String taskIdentifier) {
        for (SafeThread thread : preservedTasks) {
            if (thread.isEqual(taskIdentifier)) {
                preservedTasks.remove(thread);
            }
        }
    }

    public boolean isRequestUnderProcess(int commandId) {
        String strIdentifier = "command"+commandId;

        for (SafeThread thread : preservedTasks) {
            if (thread.isEqual(strIdentifier)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRequestUnderProcess(String taskIdentifier) {
        for (SafeThread thread : preservedTasks) {
            if (thread.isEqual(taskIdentifier)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNotRequestUnderProcess(int commandId) {
        return !isRequestUnderProcess(commandId);
    }

    public void clear() {
        if (getQueue() != null) {
            getQueue().clear();
        }
        preservedTasks.clear();

        purge();
    }
}
