package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

import java.util.ArrayList;

public class CastList extends BaseModel {

    public ArrayList<Cast> CastArrayList = new ArrayList<>();

    public ArrayList<Cast> getCastList() {
        return CastArrayList;
    }

    public void setCastList(ArrayList<Cast> castArrayList) {
        CastArrayList = castArrayList;
    }

    public void addCast(Cast cast)
    {
        if (CastArrayList != null)
        {
            CastArrayList.add(cast);
        }
    }

    public int getSize()
    {
        return (CastArrayList != null ? CastArrayList.size() : 0);
    }
}
