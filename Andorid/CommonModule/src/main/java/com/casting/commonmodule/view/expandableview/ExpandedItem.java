package com.casting.commonmodule.view.expandableview;

/**
 * @author JJShin
 */
public abstract class ExpandedItem extends BaseExpandable {

    private Object mRootObject;

    public ExpandedItem(Object o) {
        mRootObject = o;
    }

    @Override
    public Object getRootObject() {
        return mRootObject;
    }

    @Override
    public int getExpandableItemType() {
        return EXPANDED_CHILD;
    }
}
