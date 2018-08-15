package com.casting.commonmodule.thread;

import android.text.TextUtils;

public abstract class ThreadUnit implements Runnable {

    @Override
    public final void run() {
        try
        {
            runSafely();
        }
        catch (Exception e)
        {
            e.printStackTrace();

            onError(e);
        }
    }

    protected void onError(Exception e)
    {

    }

    protected abstract void runSafely() throws Exception;

    public abstract String getName();

    public boolean isEqual(String s)
    {
        String name = getName();

        return (!TextUtils.isEmpty(s) && s.equalsIgnoreCase(name));
    }
}
