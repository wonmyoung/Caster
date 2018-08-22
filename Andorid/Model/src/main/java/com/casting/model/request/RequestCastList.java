package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.CastList;

import org.json.JSONObject;

public class RequestCastList extends NetworkRequest implements JSONParcelable<CastList> {

    public enum Order {
        Popular, Available, RewardBig;
    }

    private String  mEmailAddress;
    private int     PageIndex;
    private int     Size = 10;
    private Order   mOrder;

    @Override
    public String getHttpMethod() {
        return null;
    }

    @Override
    public ContentValues getHttpRequestHeader() {
        return null;
    }

    @Override
    public ContentValues getHttpRequestParameter() {
        return null;
    }

    @Override
    public NetworkParcelable getNetworkParcelable() {
        return this;
    }

    @Override
    public CastList parse(JSONObject jsonObject) {
        return null;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        this.Size = size;
    }

    public Order getOrder() {
        return mOrder;
    }

    public void setOrder(Order order) {
        this.mOrder = order;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String address) {
        this.mEmailAddress = address;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }
}
