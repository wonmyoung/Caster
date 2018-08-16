package com.casting.commonmodule.view.list;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class CompositeViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mChildViewArray;

    public CompositeViewHolder(View itemView) {
        super(itemView);

        mChildViewArray = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V find(int viewResId) {

        View view = mChildViewArray.get(viewResId);

        if (view == null) {
            view = itemView.findViewById(viewResId);

            mChildViewArray.put(viewResId , view);
        }

        return (V) view;
    }
}
