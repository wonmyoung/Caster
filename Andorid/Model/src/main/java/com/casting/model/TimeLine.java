package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

public class TimeLine extends BaseModel implements ICommonItem, ItemConstant
{
    private Member  mMember;

    public Member getMember() {
        return mMember;
    }

    public void setMember(Member member) {
        mMember = member;
    }

    @Override
    public int getItemType() {
        return TIME_LINE;
    }
}
