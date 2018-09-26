package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;

public class FollowingVector extends BaseModel implements ICommonItem {

    private String  UserName;
    private String  UserId;
    private String  Avatar;

    private int mItemType;

    public void setItemType(int itemType)
    {
        mItemType = itemType;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }
}
