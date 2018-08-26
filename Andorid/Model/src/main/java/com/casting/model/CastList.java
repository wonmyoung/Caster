package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

import java.util.ArrayList;

public class CastList extends BaseModel {

    public ArrayList<Cast> CastArrayList;

    public ArrayList<Cast> getCastList() {
        return CastArrayList;
    }

    public void setCastList(ArrayList<Cast> castArrayList) {
        CastArrayList = castArrayList;
    }
}
