package com.casting.commonmodule.view.expandableview;

import android.content.Context;

import com.casting.commonmodule.view.list.CommonRecyclerAdapter;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;

import java.util.LinkedList;

public abstract class ExpandableRecyclerViewAdapter<P extends ExpandableItem , E extends ExpandedItem> extends CommonRecyclerAdapter {

    public ExpandableRecyclerViewAdapter(Context context) {
        super(context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void bindBodyItemView(CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception {

        switch (viewType) {
            case BaseExpandable.EXPANDABLE_PARENT:
                P p = (P) item;
                onBindExpandableParentView(holder , position , p);
                break;

            case BaseExpandable.EXPANDED_CHILD:
                E e = (E) item;
                onBindExpandedChildView(holder , position , e);
                break;

            default:
                onBindStaticView(holder , position , item);
                break;
        }
    }

    protected abstract void onBindExpandableParentView(
            CompositeViewHolder holder, int position , P item);

    protected abstract void onBindExpandedChildView(
            CompositeViewHolder holder, int position , E item);

    protected abstract void onBindStaticView(
            CompositeViewHolder holder, int position , ICommonItem item);

    /**
     *
     * @param p P
     */
    @SuppressWarnings("unchecked")
    synchronized protected void expandItems(P p) {
        LinkedList<ExpandedItem> eArrayList = p.getExpandedChilds();

        int expandPosition = getItemList().indexOf(p);
        ((P) getItemList().get(expandPosition)).setExpanded(true);
        int index = expandPosition + 1;

        for (ExpandedItem e : eArrayList) {
            getItemList().add(index , e);

            index += 1;
        }

        notifyItemRangeInserted((expandPosition + 1) , eArrayList.size());
    }

    /**
     *
     * @param p P
     */
    @SuppressWarnings("unchecked")
    synchronized protected void collapseItems(P p) {
        LinkedList<ExpandedItem> eArrayList = p.getExpandedChilds();

        int collapsePosition = getItemList().indexOf(p);

//        ((P) getItemList().get(collapsePosition)).setExpanded(false);
//
//        int index = collapsePosition + 1;
//
//        while (getItemList().size() > index &&
//               getItem(index).getExpandableItemType() == BaseExpandable.EXPANDED_CHILD) {
//
//            getItemList().remove(index);
//        }

        //notifyItemRangeRemoved(index , eArrayList.size());
    }

}
