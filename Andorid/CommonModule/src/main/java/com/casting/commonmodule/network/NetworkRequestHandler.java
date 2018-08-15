package com.casting.commonmodule.network;

import android.support.annotation.MainThread;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.base.HttpRequest;
import com.casting.commonmodule.network.base.NetworkExecutor;
import com.casting.commonmodule.network.base.NetworkProtocol;
import com.casting.commonmodule.network.base.NetworkResponse;
import com.casting.commonmodule.network.base.URLGeneratorStrategy;
import com.casting.commonmodule.IResponseListener;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkRequestHandler implements NetworkProtocol {

    private static class LazyHolder {

        private static NetworkRequestHandler mInstance = new NetworkRequestHandler();
    }

    public static NetworkRequestHandler getInstance() {
        return LazyHolder.mInstance;
    }

    private URLGeneratorStrategy    mURLGeneratorStrategy;

    private ConcurrentHashMap<BaseRequest, Queue<IResponseListener>> protocolsQueueHashMap = new ConcurrentHashMap<>();

    public boolean isAnyNetworkThreadProcess() {

        boolean networkInProgress = false;

        for (BaseRequest request : protocolsQueueHashMap.keySet())
        {

            Queue<IResponseListener> queue = protocolsQueueHashMap.get(request);

            if (queue.size() > 0)
            {
                networkInProgress = true;
                break;
            }
        }
        return networkInProgress;
    }

    public <R extends BaseRequest> boolean isNetworkThreadProcess(R r) {

        Queue<IResponseListener> queue = protocolsQueueHashMap.get(r);

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
    public void receiveResponse(NetworkResponse response)
    {
        try
        {
            BaseRequest request = response.getSourceRequest();

            Queue<IResponseListener> queue = protocolsQueueHashMap.get(request);

            if (queue != null) {

                IResponseListener responseListener = queue.poll();

                if (responseListener != null) {
                    responseListener.onThreadResponseListen(response);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <R extends BaseRequest> void requestHttpExecute(R r)
    {

        if (NetworkState.getInstance().isNetworkAvailable())
        {

            if (isNetworkThreadIdle(r))
            {
                if (protocolsQueueHashMap.get(r) == null) {
                    protocolsQueueHashMap.put(r , new LinkedList<IResponseListener>());
                }
                protocolsQueueHashMap.get(r).add(r.getResponseListener());

                INetworkRequest networkRequest = (INetworkRequest) r;

                HttpRequest httpRequest = new HttpRequest();
                httpRequest.setRequestCommandTag(r);
                httpRequest.setUrlData(mURLGeneratorStrategy.create(r));
                httpRequest.setHttpMethod(networkRequest.getHttpMethod());
                httpRequest.setDownloadPath(networkRequest.getDownloadPath());
                httpRequest.setRequestHttpHeader(networkRequest.getHttpRequestHeader());
                httpRequest.setParameterValues(networkRequest.getHttpRequestParameter());

                NetworkExecutor networkExecutor = new NetworkExecutor();
                networkExecutor.setNetworkRequester(httpRequest);
                networkExecutor.execute();
            }
        }
        else
        {
            NetworkRequest networkRequest = new NetworkRequest(r);

            NetworkState.getInstance().enqueuePreservedNetworkTask(networkRequest);
        }
    }

    public void setURLGeneratorStrategy(URLGeneratorStrategy strategy) {
        this.mURLGeneratorStrategy = strategy;
    }
}
