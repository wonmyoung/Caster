package com.casting.commonmodule.view.expandableview;

import com.casting.commonmodule.view.list.ICommonItem;

/**
 * @author JJShin
 */
public abstract class BaseExpandable implements ICommonItem {

    public static final int STATIC_ITEM = -10;
    public static final int EXPANDED_CHILD = 10;
    public static final int EXPANDABLE_PARENT = 20;

    public abstract int getExpandableItemType();

    public abstract Object getRootObject();
}
