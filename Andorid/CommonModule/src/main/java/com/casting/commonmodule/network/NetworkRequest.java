package com.casting.commonmodule.network;

import android.content.ContentValues;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.base.NetworkConstant;
import com.casting.commonmodule.network.base.NetworkParcelable;

public abstract class NetworkRequest<M extends BaseModel> extends BaseRequest<M> implements Runnable, NetworkConstant
{
    public abstract String getHttpMethod();

    public abstract ContentValues getHttpRequestHeader();

    public abstract ContentValues getHttpRequestParameter();

    public abstract NetworkParcelable getNetworkParcelable();

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
