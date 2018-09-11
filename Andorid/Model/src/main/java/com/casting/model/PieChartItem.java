package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class PieChartItem extends BaseModel implements ICommonItem, ItemConstant {

    private ArrayList<PieEntry>    PointEntries;

    private ArrayList<String>   PointNameList = new ArrayList<>();

    @Override
    public int getItemType()
    {
        return PIE_CHART;
    }

    public List<PieEntry> getPointEntries()
    {
        return PointEntries;
    }

    public void setPointEntries(ArrayList<PieEntry> pointEntries)
    {
        PointEntries = pointEntries;
    }

    public void addPointEntrie(PieEntry entry)
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

    public void addPointEntrie(String name, int value)
    {
        if (PointEntries == null)
        {
            PointEntries = new ArrayList<>();
        }

        int x = PointEntries.size();

        PointNameList.add(x, name);

        PointEntries.add(new PieEntry(x, value));
    }
}
