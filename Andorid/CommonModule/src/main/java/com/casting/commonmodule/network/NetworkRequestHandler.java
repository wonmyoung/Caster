package com.casting.commonmodule.network;

import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.util.Log;

import com.casting.commonmodule.IRequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.base.HttpRequest;
import com.casting.commonmodule.network.base.NetworkExecutor;
import com.casting.commonmodule.network.base.NetworkConstant;
import com.casting.commonmodule.network.base.NetworkResponse;
import com.casting.commonmodule.network.base.URLGeneratorStrategy;
import com.casting.commonmodule.IResponseListener;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkRequestHandler implements NetworkConstant, IRequestHandler<NetworkRequest> {

    private static class LazyHolder
    {
        private static NetworkRequestHandler mInstance = new NetworkRequestHandler();
    }

    public static NetworkRequestHandler getInstance()
    {
        return LazyHolder.mInstance;
    }

    public void init()
    {

    }

    private URLGeneratorStrategy    mURLGeneratorStrategy;

    private ConcurrentHashMap<BaseRequest, Queue<IResponseListener>> protocolsQueueHashMap = new ConcurrentHashMap<>();

    public boolean isAnyNetworkThreadProcess()
    {
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

    public boolean isNetworkThreadProcess(NetworkRequest r)
    {
        Queue<IResponseListener> queue = protocolsQueueHashMap.get(r);

        return (queue != null && queue.size() > 0);
    }

    public boolean isNetworkThreadIdle(NetworkRequest r) {
        return !isNetworkThreadProcess(r);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void request(NetworkRequest r)
    {
        if (NetworkState.getInstance().isNetworkAvailable())
        {
            if (isNetworkThreadIdle(r))
            {
                IResponseListener responseListener = r.getResponseListener();

                if (protocolsQueueHashMap.get(r) == null) {
                    protocolsQueueHashMap.put(r , new LinkedList<IResponseListener>());
                }
                protocolsQueueHashMap.get(r).add(responseListener);

                String url = (mURLGeneratorStrategy == null ? null :
                              mURLGeneratorStrategy.create(r));

                if (!TextUtils.isEmpty(url))
                {
                    HttpRequest httpRequest = new HttpRequest();
                    httpRequest.setNetworkRequest(r);
                    httpRequest.setUrlData(url);
                    httpRequest.setHttpMethod(r.getHttpMethod());
                    httpRequest.setRequestHttpHeader(r.getHttpRequestHeader());
                    httpRequest.setParameterValues(r.getHttpRequestParameter());

                    NetworkExecutor networkExecutor = new NetworkExecutor();
                    networkExecutor.setNetworkRequester(httpRequest);
                    networkExecutor.execute();
                }
            }
        }
        else
        {
            NetworkState.getInstance().enqueuePreservedNetworkTask(r);
        }
    }

    /**
     * Cf . 현재 상태에서는 protocolsQueueHashMap 에 대한 무결성은 volatile 만으로 충분하지만
     * protocolsQueueHashMap 에 대한 연산의 원자성을 보장해주기 위해 동기화를 설정..
     *
     * @param response {@link NetworkResponse}
     */
    @SuppressWarnings("unchecked")
    @MainThread
    public void receiveResponse(NetworkResponse response)
    {
        try
        {
            BaseRequest request = response.getSourceRequest();

            Queue<IResponseListener> queue = protocolsQueueHashMap.get(request);

            if (queue != null)
            {
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

    public void setURLGeneratorStrategy(URLGeneratorStrategy strategy) {
        this.mURLGeneratorStrategy = strategy;
    }
}
