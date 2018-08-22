package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

import java.util.ArrayList;

public class CastList extends BaseModel {

    public ArrayList<Cast> CastArrayList;

    public ArrayList<Cast> getCastArrayList() {
        return CastArrayList;
    }

    public void setCastArrayList(ArrayList<Cast> castArrayList) {
        CastArrayList = castArrayList;
    }
}
