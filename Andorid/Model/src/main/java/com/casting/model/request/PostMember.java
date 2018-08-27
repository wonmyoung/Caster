package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.IFileUpLoader;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.model.Member;

public class PostMember extends NetworkRequest implements IFileUpLoader {

    private Member  mMember;

    private String  FilePath;

    @Override
    public String getHttpMethod()
    {
        return HttpPost;
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
    public JSONParcelable getNetworkParcelable() {
        return null;
    }

    public Member getMember() {
        return mMember;
    }

    public void setMember(Member member) {
        this.mMember = member;
    }

    @Override
    public String getTargetFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }
}
