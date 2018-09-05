package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.ReplyList;
import com.casting.model.TimeLine;

import org.json.JSONObject;

public class RequestReplyList extends NetworkRequest implements JSONParcelable<ReplyList> {

    private TimeLine TargetTimeLine;

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

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable<ReplyList> getNetworkParcelable() {
        return this;
    }

    @Override
    public ReplyList parse(JSONObject jsonObject) {
        return null;
    }

    public TimeLine getTargetTimeLine() {
        return TargetTimeLine;
    }

    public void setTargetTimeLine(TimeLine timeLine) {
        TargetTimeLine = timeLine;
    }
}
