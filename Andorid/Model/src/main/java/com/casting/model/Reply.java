package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

public class Reply extends BaseModel implements ICommonItem, ItemConstant {

    private Member member;

    @Override
    public int getItemType()
    {
        return TIME_LINE_REPLY;
    }
}
