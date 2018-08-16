package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

public class Cast extends BaseModel {

    private double      RemainingTime;
    private String      Title;
    private String[]    Tags;
    private int         RewardCash;
    private String[]    Thumbnails;

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
}
