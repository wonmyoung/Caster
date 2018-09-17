package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

import java.util.ArrayList;

public class AlarmList extends BaseModel {

    private ArrayList<Alarm> AlarmArrayList = new ArrayList<>();

    public ArrayList<Alarm> getAlarmArrayList() {
        return AlarmArrayList;
    }

    public void setAlarmArrayList(ArrayList<Alarm> alarmArrayList) {
        AlarmArrayList = alarmArrayList;
    }

    public void addAlarm(Alarm alarm)
    {
        if (AlarmArrayList != null)
        {
            AlarmArrayList.add(alarm);
        }
    }

    public int getAlarmSize()
    {
        return (AlarmArrayList != null ? AlarmArrayList.size() : 0);
    }
}
