package com.casting.commonmodule.thread;

import android.text.TextUtils;

public abstract class ThreadUnit implements Runnable {

    private String      mThreadName;

    private ThreadType  mThreadUnitKind;

    public ThreadUnit() {
    }

    public ThreadUnit(String threadName) {
    }

    public ThreadUnit(ThreadType threadUnit) {
        mThreadUnitKind = threadUnit;
    }

    @Override
    public void run() {
        try {
            runTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void runTask() throws Exception;

    public String getThreadName() {
        return mThreadName;
    }

    public void setThreadName(String idetifier) {
        this.mThreadName = idetifier;
    }

    public boolean isEqual(String idetifier) {
        if (!TextUtils.isEmpty(idetifier)) {
            return idetifier.equalsIgnoreCase(mThreadName);
        } else {
            return false;
        }
    }

    public ThreadType getThreadUnitKind() {
        return mThreadUnitKind;
    }
}
