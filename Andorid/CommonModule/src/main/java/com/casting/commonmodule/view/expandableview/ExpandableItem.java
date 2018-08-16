package com.casting.commonmodule.view.expandableview;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author JJShin
 */
public class ExpandableItem extends BaseExpandable {

    private Object mRootObject;
    private boolean mExpanded = false;

    private LinkedList<ExpandedItem> mExpandedItemChildren;

    public ExpandableItem(Object o) {
        mRootObject = o;
        mExpandedItemChildren = new LinkedList<>();
    }

    @Override
    public int getExpandableItemType() {
        return EXPANDABLE_PARENT;
    }

    @Override
    public Object getRootObject() {
        return mRootObject;
    }

    public LinkedList<ExpandedItem> getExpandedChilds() {
        return mExpandedItemChildren;
    }

    public void setExpandedChilds(LinkedList<ExpandedItem> expandedItemChildren) {
        this.mExpandedItemChildren = expandedItemChildren;
    }

    public void setExpandedChilds(ExpandedItem ... expandedItemChildren) {
        if (expandedItemChildren != null) {
            mExpandedItemChildren.addAll(Arrays.asList(expandedItemChildren));
        }
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.mExpanded = expanded;
    }
}
