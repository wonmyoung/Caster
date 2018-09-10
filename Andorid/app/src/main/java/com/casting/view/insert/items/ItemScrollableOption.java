package com.casting.view.insert.items;

import com.casting.model.global.ItemConstant;

public class ItemScrollableOption extends ItemInsert {

    private int     MaxValue;
    private String  MaxValuePrefix;
    private int     MinValue;
    private String  MinValuePrefix;

    private String  ScrollPrefix;

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

    @Override
    public int getItemType() {
        return SELECT_SCROLLABLE_OPTION;
    }

    public void notifySelectedData()
    {

        int data = (int) getInsertedData();

        setChanged();

        notifyObservers(data);
    }
}
