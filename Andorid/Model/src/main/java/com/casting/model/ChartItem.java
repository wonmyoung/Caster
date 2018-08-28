package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;

public class ChartItem extends BaseModel implements ICommonItem {

    private int mItemType;

    public void setItemType(int itemType)
    {
        mItemType = itemType;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }
}
