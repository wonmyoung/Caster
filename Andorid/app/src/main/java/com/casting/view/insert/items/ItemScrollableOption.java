package com.casting.view.insert.items;

public class ItemScrollableOption extends ItemInsert<Integer> {

    private int     MaxValue;
    private String  MaxValuePrefix;
    private int     MinValue;
    private String  MinValuePrefix;

    private String  ScrollPrefix;
    private String  BottomPrefix;

    public ItemScrollableOption()
    {
        InsertedData = -1;
    }

    public int getMaxValue() {
        return MaxValue;
    }

    public void setMaxValue(int maxValue) {
        MaxValue = maxValue;
    }

    public String getMaxValuePrefix() {
        return MaxValuePrefix;
    }

    public void setMaxValuePrefix(String maxValuePrefix) {
        MaxValuePrefix = maxValuePrefix;
    }

    public int getMinValue() {
        return MinValue;
    }

    public void setMinValue(int minValue) {
        MinValue = minValue;
    }

    public String getMinValuePrefix() {
        return MinValuePrefix;
    }

    public void setMinValuePrefix(String minValuePrefix) {
        MinValuePrefix = minValuePrefix;
    }

    public String getScrollPrefix() {
        return ScrollPrefix;
    }

    public void setScrollPrefix(String scrollPrefix) {
        ScrollPrefix = scrollPrefix;
    }

    public String getBottomPrefix() {
        return BottomPrefix;
    }

    public void setBottomPrefix(String bottomPrefix) {
        BottomPrefix = bottomPrefix;
    }

    @Override
    public int getItemType() {
        return SELECT_SCROLLABLE_OPTION;
    }

    public void notifySelectedData()
    {
        int data = getInsertedData();

        if (data == -1)
        {
            data = 0;
            data += (MaxValue - MinValue) / 2;
        }

        setChanged();

        notifyObservers(data);
    }
}
