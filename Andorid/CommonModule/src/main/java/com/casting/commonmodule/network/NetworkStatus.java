package com.casting.commonmodule.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.casting.commonmodule.thread.MultiThreadExecutor;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.commonmodule.view.BaseObservable;

import java.util.LinkedList;
import java.util.Queue;

public class NetworkStatus extends BaseObservable {

    public static final int NETWORK_STATE = -9999;

    private static NetworkStatus mInstance;

    private boolean NetworkAvailable;

    private NetworkEnum mNetworkState;

    private NetworkStatusReceiver mNetworkStatusReceiver;

    public NetworkStatus() {
        super(NETWORK_STATE);
    }

    private Queue<NetworkTask> mPreservedTasks = new LinkedList<>();

    public static NetworkStatus getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkStatus();
        }
        return mInstance;
    }

    public void registerReceiver(Context context) {

        try {

            if (mNetworkStatusReceiver == null) {
                mNetworkStatusReceiver = new NetworkStatusReceiver();

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                context.registerReceiver(mNetworkStatusReceiver , intentFilter);

                NetworkAvailable = UtilityData.confirmNetworkAccessable(context);
            }
        } catch (Exception e) {
            e.printStackTrace();

            mNetworkStatusReceiver = null;
        }
    }

    public void unregisterReceiver(Context context) {
        try {
            if (mNetworkStatusReceiver != null) {
                context.unregisterReceiver(mNetworkStatusReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mNetworkStatusReceiver = null;
        }
    }

    public boolean isNotNetworkAvailable() {
        return !isNetworkAvailable();
    }

    public boolean isNetworkAvailable() {
        return NetworkAvailable;
    }

    public NetworkEnum getNetworkState() {
        return mNetworkState;
    }

    public boolean enqueuePreservedNetworkTask(NetworkTask networkTask) {
        try {
            String enqueueIdentifer = networkTask.getThreadName();

            for (NetworkTask preservedTask : mPreservedTasks) {
                if (preservedTask.getThreadName().equalsIgnoreCase(enqueueIdentifer)) {
                    return false;
                }
            }
            mPreservedTasks.add(networkTask);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public void dequeuePreservedNetworkTask() {
        try {
            MultiThreadExecutor threadExecutor = new MultiThreadExecutor(1);

            while (mPreservedTasks.peek() != null) {
                Runnable runnable = mPreservedTasks.poll();
                threadExecutor.execute(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class NetworkStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (activeNetworkInfo == null) {

                if (NetworkAvailable) {
                    NetworkAvailable = false;

                    setChanged();
                }

                mNetworkState = NetworkEnum.NO_NETWORK;

            } else {

                if (NetworkAvailable != activeNetworkInfo.isAvailable()) {
                    NetworkAvailable = (activeNetworkInfo.isAvailable());

                    setChanged();
                }

                mNetworkState = (wifiNetworkInfo.isAvailable() ? NetworkEnum.WIFI : NetworkEnum.LTE);

                if (NetworkAvailable) {
                    dequeuePreservedNetworkTask();
                }
            }

            notifyObservers(mNetworkState);
        }
    }
}
