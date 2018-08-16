package com.casting.commonmodule.view.expandableview;

/**
 * @author JJShin
 */
public class ExpandedItem extends BaseExpandable {

    private Object mRootObject;

    public ExpandedItem(Object o) {
        mRootObject = o;
    }

    @Override
    public int getExpandableItemType() {
        return EXPANDED_CHILD;
    }

    @Override
    public Object getRootObject() {
        return mRootObject;
    }
}
