package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.session.SessionType;

public class Member extends BaseModel {

    private String          mNickName;
    private String          mEmail;
    private String          mPassWord;
    private SessionType     mSessionType;

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassWord() {
        return mPassWord;
    }

    public void setPassWord(String passWord) {
        mPassWord = passWord;
    }

    public SessionType getSessionType() {
        return mSessionType;
    }

    public void setSessionType(SessionType sessionType) {
        mSessionType = sessionType;
    }
}
