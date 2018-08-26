package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.session.SessionType;

public class Member extends BaseModel {

    private String          mNickName;
    private String          mEmail;
    private String          UserId;
    private String          Description;
    private String          mPassWord;
    private String          UserGrade;
    private String          UserPicThumbnail;
    private SessionType     mSessionType;
    private int     UserCap;
    private int     CorrectCast;
    private double  CorrectRate;
    private int     FollowingNum;
    private int     FollowerNum;

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

    public int getUserCap() {
        return UserCap;
    }

    public void setUserCap(int userCap) {
        UserCap = userCap;
    }

    public int getCorrectCast() {
        return CorrectCast;
    }

    public void setCorrectCast(int correctCast) {
        CorrectCast = correctCast;
    }

    public double getCorrectRate() {
        return CorrectRate;
    }

    public void setCorrectRate(double correctRate) {
        CorrectRate = correctRate;
    }

    public int getFollowingNum() {
        return FollowingNum;
    }

    public void setFollowingNum(int followingNum) {
        FollowingNum = followingNum;
    }

    public int getFollowerNum() {
        return FollowerNum;
    }

    public void setFollowerNum(int followerNum) {
        FollowerNum = followerNum;
    }

    public String getUserPicThumbnail() {
        return UserPicThumbnail;
    }

    public void setUserPicThumbnail(String userPicThumbnail) {
        UserPicThumbnail = userPicThumbnail;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUserGrade() {
        return UserGrade;
    }

    public void setUserGrade(String userGrade) {
        UserGrade = userGrade;
    }
}
