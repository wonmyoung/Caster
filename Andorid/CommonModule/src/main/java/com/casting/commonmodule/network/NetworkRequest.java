package com.casting.commonmodule.network;

import android.content.ContentValues;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.base.NetworkConstant;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.base.NetworkResponse;

public abstract class NetworkRequest extends BaseRequest<NetworkResponse> implements Runnable, NetworkConstant
{
    public abstract String getHttpMethod();

    public abstract ContentValues getHttpRequestHeader();

    public abstract ContentValues getHttpRequestParameter();

    public abstract <P extends NetworkParcelable> P getNetworkParcelable();

    @SuppressWarnings("unchecked")
    @Override
    public void run() {

        try
        {
            NetworkRequestHandler.getInstance().request(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
