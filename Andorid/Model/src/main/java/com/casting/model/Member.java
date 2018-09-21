package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.session.SessionType;

public class Member extends BaseModel {

    private String          UserName;
    private String          Email;
    private String          UserId;
    private String          Description;
    private String          PassWord;
    private String          UserPicThumbnail;
    private String          AuthToken;
    private SessionType     mSessionType;
    private int     UserPoint;
    private String  UserLevel;
    private int     CorrectCast;
    private double  CorrectRate;
    private int     FollowingNum;
    private int     FollowerNum;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String nickName) {
        UserName = nickName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public SessionType getSessionType() {
        return mSessionType;
    }

    public void setSessionType(SessionType sessionType) {
        mSessionType = sessionType;
    }

    public int getUserPoint() {
        return UserPoint;
    }

    public void setUserPoint(int userPoint) {
        UserPoint = userPoint;
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

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public String getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(String userLevel) {
        UserLevel = userLevel;
    }
}
