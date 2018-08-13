package com.casting.commonmodule.network.base;

import android.content.ContentValues;

public interface INetworkRequest extends NetworkProtocol {

    String getDownloadPath();

    String getHttpMethod();

    ContentValues getHttpRequestHeader();

    ContentValues getHttpRequestParameter();
}
