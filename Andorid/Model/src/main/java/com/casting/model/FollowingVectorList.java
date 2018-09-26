package com.casting.model;

import com.casting.commonmodule.model.BaseModel;

import java.util.ArrayList;

public class FollowingVectorList extends BaseModel {

    private ArrayList<FollowingVector>  vectorArrayList = new ArrayList<>();

    public ArrayList<FollowingVector> getVectorArrayList() {
        return vectorArrayList;
    }

    public void setVectorArrayList(ArrayList<FollowingVector> vectorArrayList) {
        this.vectorArrayList = vectorArrayList;
    }

    public void addFollowingVector(FollowingVector followingVector)
    {
        if (vectorArrayList != null)
        {
            vectorArrayList.add(followingVector);
        }
    }

    public int size()
    {
        return (vectorArrayList == null ? 0 : vectorArrayList.size());
    }

}
