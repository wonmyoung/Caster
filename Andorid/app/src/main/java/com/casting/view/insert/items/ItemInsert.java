package com.casting.view.insert.items;

import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

import java.util.Observable;

public class ItemInsert extends Observable implements ICommonItem, ItemConstant
{
    private int mItemType;

    private String  InsertTitle;

    private Object  InsertedData;

    @Override
    public int getItemType() {
        return mItemType;
    }

    public void setItemType(int type)
    {
        this.mItemType = type;
    }

    public String getInsertTitle() {
        return InsertTitle;
    }

    public void setInsertTitle(String title) {
        InsertTitle = title;
    }

    public Object getInsertedData()
    {
        return InsertedData;
    }

    public void setInsertedData(Object data)
    {
        InsertedData = data;

        setChanged();

        notifyObservers(data);
    }
}