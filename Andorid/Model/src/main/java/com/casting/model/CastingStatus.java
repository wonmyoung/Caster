package com.casting.model;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

import java.util.ArrayList;

public class CastingStatus extends BaseModel implements ICommonItem, ItemConstant {

    public class CastingOption {

        public String   Name;
        public int      Percentage;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getPercentage() {
            return Percentage;
        }

        public void setPercentage(int percentage) {
            Percentage = percentage;
        }
    }

    private ArrayList<CastingOption> OptionArrayList;

    public ArrayList<CastingOption> getOptionArrayList() {
        return OptionArrayList;
    }

    public void setOptionArrayList(ArrayList<CastingOption> optionArrayList) {
        OptionArrayList = optionArrayList;
    }

    public void addStatus(CastingOption castingOption)
    {
        if (OptionArrayList == null)
        {
            OptionArrayList = new ArrayList<>();
        }

        OptionArrayList.add(castingOption);
    }

    public void addStatus(String name, int percentage)
    {
        if (OptionArrayList == null)
        {
            OptionArrayList = new ArrayList<>();
        }

        CastingOption castingOption = new CastingOption();
        castingOption.setName(name);
        castingOption.setPercentage(percentage);

        OptionArrayList.add(castingOption);
    }

    public CastingOption getCastingOption(int i)
    {
        return (OptionArrayList != null && OptionArrayList.size() > i ?
                OptionArrayList.get(i) : null);
    }

    @Override
    public int getItemType() {
        return CURRENT_CASTING_STATUS;
    }
}
