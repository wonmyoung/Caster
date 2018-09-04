package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

import java.util.ArrayList;

public class CurrentCastingStatus extends BaseModel implements ICommonItem, ItemConstant {

    public class CastingStatus
    {
        public String  Name;
        public int     Progress;

        public String getName()
        {
            return Name;
        }

        public void setName(String name)
        {
            Name = name;
        }

        public int getProgress()
        {
            return Progress;
        }

        public void setProgress(int progress)
        {
            Progress = progress;
        }
    }

    private ArrayList<CastingStatus> StatusArrayList;

    @Override
    public int getItemType()
    {
        return CURRENT_CASTING_STATUS;
    }
}
