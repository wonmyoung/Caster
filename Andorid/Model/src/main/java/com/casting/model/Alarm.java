package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;

public class Alarm extends BaseModel implements ICommonItem {

    private int mItemType;

    private String EndDate;

    public void setItemType(int itemType)
    {
        mItemType = itemType;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }
}
