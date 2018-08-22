package com.casting.commonmodule.view.cardstack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;

import java.util.ArrayList;
import java.util.Collection;


public abstract class SwipeStackAdapter<T extends ICommonItem> extends BaseAdapter {

    private Context         mContext;
    private ViewGroup       mViewGroup;

    private ArrayList<T>    tArrayList = new ArrayList<>();

    private int mItemLayoutId;

    public SwipeStackAdapter(Context c, int resource)
    {
        mContext = c;

        mItemLayoutId = resource;
    }

    @Override
    public int getCount()
    {
        return tArrayList.size();
    }

    @Override
    public T getItem(int position)
    {
        try
        {
            if (position < tArrayList.size())
            {
                return tArrayList.get(position);
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (mViewGroup == null) {
            mViewGroup = parent;
        }

        CompositeViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(mItemLayoutId , parent , false);

            viewHolder = new CompositeViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (CompositeViewHolder) convertView.getTag();
        }

        bindItemDataView(viewHolder , position , getItem(position));

        return viewHolder.itemView;
    }

    protected abstract void bindItemDataView(CompositeViewHolder viewHolder, int position , T item);

    @SuppressWarnings("unchecked")
    public void setItemList(ArrayList<T> itemList)
    {
        if (itemList != null)
        {
            if (tArrayList == null) {
                tArrayList = new ArrayList<>();
            }
            tArrayList.addAll((Collection<? extends T>) itemList.clone());
        }
    }
}
