package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;

import static com.casting.model.global.ItemConstant.RANKING;

public class Ranking extends BaseModel implements ICommonItem {

    private String Level;
    private String UserId;
    private String UserName;
    private String Avatar;
    private int     Reward;
    private int     Point;
    private int     HitRatio;

    @Override
    public int getItemType() {
        return RANKING;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public int getReward() {
        return Reward;
    }

    public void setReward(int reward) {
        Reward = reward;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int point) {
        Point = point;
    }

    public int getHitRatio() {
        return HitRatio;
    }

    public void setHitRatio(int hitRatio) {
        HitRatio = hitRatio;
    }
}
