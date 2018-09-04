package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

public class Cast extends BaseModel implements ICommonItem, ItemConstant {

    private String      CastId;

    private double      RemainingTime;
    private String      Title;
    private String[]    Tags;
    private int         RewardCash;
    private String[]    Thumbnails;

    private int   mItemType;

    private boolean Done;

    public double getRemainingTime() {
        return RemainingTime;
    }

    public void setRemainingTime(double remainingTime) {
        RemainingTime = remainingTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String[] getTags() {
        return Tags;
    }

    public void setTags(String ... tags) {
        Tags = tags;
    }

    public int getRewardCash() {
        return RewardCash;
    }

    public void setRewardCash(int rewardCash) {
        RewardCash = rewardCash;
    }

    public String[] getThumbnails() {
        return Thumbnails;
    }

    public void setThumbnails(String ... thumbnails) {
        Thumbnails = thumbnails;
    }

    public void setItemType(int itemType)
    {
        mItemType = itemType;
    }

    @Override
    public int getItemType()
    {
        return mItemType;
    }

    public String getCastId() {
        return CastId;
    }

    public void setCastId(String castId) {
        CastId = castId;
    }

    public boolean isDone() {
        return Done;
    }

    public void setDone(boolean done) {
        Done = done;
    }
}
