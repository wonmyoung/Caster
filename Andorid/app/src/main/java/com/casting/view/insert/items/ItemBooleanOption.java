package com.casting.view.insert.items;

public class ItemBooleanOption extends ItemInsert<Boolean> {

    private String BottomPrefix;

    public ItemBooleanOption()
    {
        InsertedData = false;
    }

    public String getBottomPrefix() {
        return BottomPrefix;
    }

    public void setBottomPrefix(String bottomPrefix) {
        BottomPrefix = bottomPrefix;
    }

    @Override
    public int getItemType()
    {
        return SELECT_BOOLEAN_OPTION;
    }

    public void notifySelectedData()
    {
        boolean b = getInsertedData();

        setChanged();

        notifyObservers(b);
    }
}
