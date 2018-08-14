package com.casting.commonmodule.network;

import android.support.annotation.MainThread;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.base.BaseHttpRequester;
import com.casting.commonmodule.network.base.INetworkRequest;
import com.casting.commonmodule.network.base.INetworkResponseListener;
import com.casting.commonmodule.network.base.NetworkExecutor;
import com.casting.commonmodule.network.base.NetworkProtocol;
import com.casting.commonmodule.network.base.NetworkResponse;
import com.casting.commonmodule.network.base.URLGeneratorStrategy;
import com.casting.commonmodule.thread.ThreadResponseListener;

import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkThreadManager implements INetworkResponseListener, NetworkProtocol {

    private static SoftReference<NetworkThreadManager> mReference;

    private URLGeneratorStrategy    mURLGeneratorStrategy;

    private volatile ConcurrentHashMap<BaseRequest, Queue<ThreadResponseListener>> protocolsQueueHashMap;

    public static NetworkThreadManager getInstance() {
        NetworkThreadManager protocolManager = (mReference == null ? null : mReference.get());

        if (protocolManager == null) {
            synchronized (NetworkThreadManager.class) {
                protocolManager = (mReference == null ? null : mReference.get());
                if (protocolManager == null) {
                    protocolManager = new NetworkThreadManager();
                    mReference = new SoftReference<>(protocolManager);
                }
                return protocolManager;
            }
        }
        return protocolManager;
    }

    public NetworkThreadManager() {
        protocolsQueueHashMap = new ConcurrentHashMap<>();
    }

    public boolean isAnyNetworkThreadProcess() {

        boolean networkInProgress = false;

        for (BaseRequest request : protocolsQueueHashMap.keySet()) {

            Queue<ThreadResponseListener> networkResponseListenerQueue = protocolsQueueHashMap.get(request);

            if (networkResponseListenerQueue.size() > 0) {
                networkInProgress = true;
                break;
            }
        }
        return networkInProgress;
    }

    public <R extends BaseRequest> boolean isNetworkThreadProcess(R r) {

        Queue<ThreadResponseListener> queue = protocolsQueueHashMap.get(r);

        return (queue != null && queue.size() > 0);
    }

    public <R extends BaseRequest> boolean isNetworkThreadIdle(R r) {
        return !isNetworkThreadProcess(r);
    }

    /**
     * Cf . 현재 상태에서는 protocolsQueueHashMap 에 대한 무결성은 volatile 만으로 충분하지만
     * protocolsQueueHashMap 에 대한 연산의 원자성을 보장해주기 위해 동기화를 설정..
     *
     * @param response {@link NetworkResponse}
     */
    @MainThread
    @Override
    synchronized public void onNetworkResponse(NetworkResponse response) {
        try {

            BaseRequest request = response.getSourceRequest();

            Queue<ThreadResponseListener> queue = protocolsQueueHashMap.get(request);

            if (queue != null) {

                ThreadResponseListener responseListener = queue.poll();

                if (responseListener != null) {
                    responseListener.onThreadResponseListen(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <R extends BaseRequest> void requestHttpExecute(R r) {

        if (NetworkStatus.getInstance().isNetworkAvailable())
        {

            if (isNetworkThreadIdle(r))
            {
                if (protocolsQueueHashMap.get(r) == null) {
                    protocolsQueueHashMap.put(r , new LinkedList<ThreadResponseListener>());
                }
                protocolsQueueHashMap.get(r).add(r.getResponseListener());

                INetworkRequest networkRequest = (INetworkRequest) r;

                BaseHttpRequester baseHttpRequester = new BaseHttpRequester();
                baseHttpRequester.setRequestCommandTag(r);
                baseHttpRequester.setUrlData(mURLGeneratorStrategy.create(r));
                baseHttpRequester.setHttpMethod(networkRequest.getHttpMethod());
                baseHttpRequester.setDownloadPath(networkRequest.getDownloadPath());
                baseHttpRequester.setRequestHttpHeader(networkRequest.getHttpRequestHeader());
                baseHttpRequester.setParameterValues(networkRequest.getHttpRequestParameter());

                NetworkExecutor networkExecutor = new NetworkExecutor();
                networkExecutor.setNetworkRequestor(baseHttpRequester);
                networkExecutor.setNetworkResponseListener(this);
                networkExecutor.execute();
            }
        } else {
            NetworkTask networkTask = new NetworkTask(r);
            NetworkStatus.getInstance().enqueuePreservedNetworkTask(networkTask);
        }
    }

    public URLGeneratorStrategy getURLGeneratorStrategy() {
        return mURLGeneratorStrategy;
    }

    public void setURLGeneratorStrategy(URLGeneratorStrategy strategy) {
        this.mURLGeneratorStrategy = strategy;
    }
}
