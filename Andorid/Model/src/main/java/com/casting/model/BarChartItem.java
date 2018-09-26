package com.casting.model;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class BarChartItem extends CommonGraphItem {

    private String ItemTitle;
    private String ItemBottomTitle;

    private ArrayList<BarEntry>    PointEntries;

    private ArrayList<String>      PointNameList = new ArrayList<>();

    @Override
    public int getItemType()
    {
        return BAR_CHART;
    }

    public List<BarEntry> getPointEntries()
    {
        return PointEntries;
    }

    public void setPointEntries(ArrayList<BarEntry> pointEntries)
    {
        PointEntries = pointEntries;
    }

    public void addPointEntry(BarEntry entry)
    {
        if (PointEntries == null)
        {
            PointEntries = new ArrayList<>();
        }
        PointEntries.add(entry);
    }

    public String getEntrieName(int index)
    {
        if (PointNameList != null && PointNameList.size() > index)
        {
            return PointNameList.get(index);
        }

        return null;
    }

    public void addPointEntry(String name, int value)
    {
        if (PointEntries == null)
        {
            PointEntries = new ArrayList<>();
        }

        int x = PointEntries.size();

        PointNameList.add(x, name);

        PointEntries.add(new BarEntry(x, value));
    }

    public String getItemTitle() {
        return ItemTitle;
    }

    public void setItemTitle(String itemTitle) {
        ItemTitle = itemTitle;
    }

    public String getItemBottomTitle() {
        return ItemBottomTitle;
    }

    public void setItemBottomTitle(String itemBottomTitle) {
        ItemBottomTitle = itemBottomTitle;
    }
}
