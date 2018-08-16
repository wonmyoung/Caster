package com.casting.commonmodule.view.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonRecyclerAdapter<D extends ICommonItem> extends RecyclerView.Adapter<CompositeViewHolder> implements ICommonListConstant {

    private ArrayList<D> mdArrayList;

    private Context         mContext;

    private LayoutInflater  mLayoutInflater;

    private View    mHeaderView;
    private View    mFooterView;
    private View    mEmptyView;

    public CommonRecyclerAdapter(Context context)
    {
        mContext = context;

        mLayoutInflater = LayoutInflater.from(context);

        mdArrayList = new ArrayList<>();
    }

    @Override
    public final CompositeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        switch (viewType)
        {
            case LIST_HEADER_VIEW:
                return new CompositeViewHolder(mHeaderView);

            case LIST_FOOTER_VIEW:
                return new CompositeViewHolder(mFooterView);

            case LIST_EMPTY_VIEW:
                return new CompositeViewHolder(mEmptyView);

            default:
                return createBodyViewHolder(parent , viewType);
        }
    }

    @Override
    public final void onBindViewHolder(CompositeViewHolder holder, int position)
    {
        try
        {

            int viewType = getItemViewType(position);

            D d = getItem(position);

            bindBodyItemView(holder, position, viewType, d);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected abstract CompositeViewHolder createBodyViewHolder(ViewGroup parent, int viewType);

    protected abstract void bindBodyItemView(CompositeViewHolder holder, int position , int viewType, D item) throws Exception;

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemViewType(int position)
    {

        int itemSize = (mdArrayList == null ? 0 : mdArrayList.size());
        if (itemSize == 0)
        {
            return (isUsingEmptyView() ? LIST_EMPTY_VIEW : -1);
        }
        else
        {

            if (isUsingHeader() && position == 0)
            {
                return LIST_HEADER_VIEW;
            }
            else
            {
                position -= (isUsingHeader() ? 1 : 0);

                return ((position < itemSize) ? getBodyItemViewType(position) : LIST_FOOTER_VIEW);
            }
        }
    }

    public abstract int getBodyItemViewType(int position);

    @Override
    public int getItemCount()
    {
        int size = (mdArrayList == null ? 0 : mdArrayList.size());
        if (size == 0)
        {
            return (isUsingEmptyView() ? 1 : 0);
        }
        else
        {
            int itemCount = mdArrayList.size();

            itemCount += (isUsingHeader() ? 1 : 0);
            itemCount += (isUsingFooter() ? 1 : 0);

            return itemCount;
        }
    }

    public D getItem(int listPosition)
    {
        int itemPosition = listPosition;
        if (itemPosition > 0 && isUsingHeader()) {
            itemPosition -= 1;
        }

        try
        {
            return mdArrayList.get(itemPosition);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public int getItemPosition(D d)
    {
        try
        {
            int index = mdArrayList.indexOf(d);
            if (index > -1) {
                index += (isUsingHeader() ? 1 : 0);
            }

            return index;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return -1;
        }
    }

    public List<D> getItemList() {
        return mdArrayList;
    }

    public void setItemList(ArrayList<D> collection)
    {
        this.mdArrayList = collection;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public View getHeaderView(int headerType) {
        return mFooterView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public int removeItem(D d)
    {
        if (d != null)
        {
            int removedPosition = mdArrayList.indexOf(d);
            if (removedPosition > 0)
            {
                mdArrayList.remove(d);

                return removedPosition;
            }
        }

        return -1;
    }

    public void removeItem(final int position)
    {
        int size = mdArrayList.size();
        if (size > position)
        {
            if (mdArrayList.get(position) != null) {
                mdArrayList.remove(position);
            }
        }
    }

    public boolean removeItemList(ArrayList<D> dCollection)
    {
        if (dCollection != null)
        {
            return mdArrayList.removeAll(dCollection);
        }

        return false;
    }

    public void addItem(final D d)
    {
        if (mdArrayList != null) {
            mdArrayList.add(d);
        }
    }

    public void addItem(final int position , final D d)
    {
        if (mdArrayList != null)
        {
            int size = mdArrayList.size();
            if (size < position)
            {
                mdArrayList.add(size , d);
            }
            else
            {
                mdArrayList.add(position , d);
            }
        }
    }

    public void addItemList(final ArrayList<D> dCollection)
    {
        if (mdArrayList != null && dCollection != null) {
            mdArrayList.addAll(dCollection);
        }
    }

    public void addItemList(final int start , final ArrayList<D> dArrayList)
    {
        if (dArrayList != null && mdArrayList != null)
        {

            int end = (start + dArrayList.size());
            int size = mdArrayList.size();
            if (size < end)
            {
                mdArrayList.addAll(size, dArrayList);
            }
            else
            {
                mdArrayList.addAll(start, dArrayList);
            }
        }
    }

    public boolean changeItem(final int position, D d)
    {
        boolean result = false;

        int size = mdArrayList.size();
        if (size > position)
        {
            result = (mdArrayList.set(position, d) != null);
        }

        return result;
    }

    public int changeItem(D d)
    {
        int position = mdArrayList.indexOf(d);
        if (position > -1) {
            mdArrayList.set(position, d);
        }

        return position;
    }

    public void changeItemList(ArrayList<D> dArrayList)
    {
        clear();

        addItemList(dArrayList);
    }

    public void clear()
    {
        if (mdArrayList != null) {
            mdArrayList.clear();
        }
    }

    public boolean isBelongItem(D d) {
        return (mdArrayList != null && mdArrayList.contains(d));
    }

    public boolean isUsingHeader() {
        return mHeaderView != null;
    }

    public boolean isUsingFooter() {
        return mFooterView != null;
    }

    public boolean isUsingEmptyView() {
        return (mEmptyView != null);
    }

    @SuppressWarnings("unchecked")
    protected <V extends View> V  inflateView(int resourceId , ViewGroup parent)
    {
        return (V) mLayoutInflater.inflate(resourceId, parent , false);
    }
}
