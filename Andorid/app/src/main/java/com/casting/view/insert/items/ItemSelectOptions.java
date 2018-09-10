package com.casting.view.insert.items;

import java.util.ArrayList;

public class ItemSelectOptions extends ItemInsert {

    public class Option {

        private String Name;
        private Object Value;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public Object getValue() {
            return Value;
        }

        public void setValue(Object value) {
            Value = value;
        }
    }

    private boolean Horizontal;

    private String BottomPrefix;

    private ArrayList<Option> OptionArrayList = new ArrayList<>();

    public ArrayList<Option> getOptionArrayList() {
        return OptionArrayList;
    }

    public void setOptionArrayList(ArrayList<Option> optionArrayList) {
        this.OptionArrayList = optionArrayList;
    }

    @Override
    public int getItemType() {
        return (Horizontal ? SELECT_HORIZONTAL_OPTIONS : SELECT_VERTICAL_OPTIONS);
    }

    public String getBottomPrefix() {
        return BottomPrefix;
    }

    public void setBottomPrefix(String prefix) {
        BottomPrefix = prefix;
    }

    public boolean isHorizontal() {
        return Horizontal;
    }

    public boolean isVertical()
    {
        return !Horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        Horizontal = horizontal;
    }

    public void setVertical(boolean vertical)
    {
        Horizontal = !vertical;
    }

    public int getSelectedIndex()
    {
        Object o = getInsertedData();

        return ((o != null && o instanceof Option) ? OptionArrayList.indexOf(o) : -1);
    }

    public int getOptionsSize()
    {
        return (OptionArrayList != null ? OptionArrayList.size() : 0);
    }

    public Option getOption(int i)
    {
        return (OptionArrayList != null && OptionArrayList.size() > i ? OptionArrayList.get(i) : null);
    }

    public void addOption(String name, Object o)
    {
        if (OptionArrayList == null) {
            OptionArrayList = new ArrayList<>();
        }

        Option option = new Option();
        option.setName(name);
        option.setValue(o);

        OptionArrayList.add(option);
    }

    public void addScrollableOption(String name)
    {
        if (OptionArrayList == null) {
            OptionArrayList = new ArrayList<>();
        }

        Option option = new Option();
        option.setName(name);

        OptionArrayList.add(option);
    }

    public void notifySelectedData()
    {
        Object o = getInsertedData();

        if (o == null)
        {
            int size = getOptionsSize();

            o = getOption((size / 2));

            setInsertedData(o);
        }

        setChanged();

        notifyObservers(o);
    }
}
