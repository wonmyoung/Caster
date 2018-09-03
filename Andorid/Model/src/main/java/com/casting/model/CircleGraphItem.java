package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

public class CircleGraphItem extends BaseModel implements ICommonItem, ItemConstant {

    @Override
    public int getItemType()
    {
        return GRAPH_A;
    }
}
