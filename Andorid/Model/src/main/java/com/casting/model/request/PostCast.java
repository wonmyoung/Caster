package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.Cast;
import com.casting.model.insert.ItemInsert;

import org.json.JSONObject;

import java.util.ArrayList;

public class PostCast extends NetworkRequest implements JSONParcelable<Cast> {

    private ArrayList<ItemInsert> InsertArrayList = new ArrayList<>();

    @Override
    public String getHttpMethod() {
        return HttpPost;
    }

    @Override
    public ContentValues getHttpRequestHeader() {
        return null;
    }

    @Override
    public ContentValues getHttpRequestParameter() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONParcelable<Cast> getNetworkParcelable() {
        return this;
    }

    @Override
    public Cast parse(JSONObject jsonObject) {
        return null;
    }

    public ArrayList<ItemInsert> getInsertArrayList() {
        return InsertArrayList;
    }

    public void setInsertArrayList(ArrayList<ItemInsert> insertArrayList)
    {
        InsertArrayList = insertArrayList;
    }

    public void addInsertItem(ItemInsert itemInsert)
    {
        if (InsertArrayList != null)
        {
            InsertArrayList.add(itemInsert);
        }
    }

    public void addInsertItemList(ArrayList<ItemInsert> insertArrayList)
    {
        if (InsertArrayList != null)
        {
            InsertArrayList.addAll(insertArrayList);
        }
    }
}
