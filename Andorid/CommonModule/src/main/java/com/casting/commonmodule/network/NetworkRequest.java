package com.casting.commonmodule.network;

import android.content.ContentValues;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.model.BaseRequest;

public abstract class NetworkRequest<M extends BaseModel> extends BaseRequest<M> implements Runnable
{
    public abstract String getDownloadPath();

    public abstract String getHttpMethod();

    public abstract ContentValues getHttpRequestHeader();

    public abstract ContentValues getHttpRequestParameter();



    @Override
    public void run() {

        try
        {
            NetworkRequestHandler.getInstance().requestHttpExecute(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
