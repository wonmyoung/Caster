package com.casting.commonmodule.view.cardstack;

/**
 * Interface definition for a callback to be invoked when the top view was
 * swiped to the left / right or when the stack gets empty.
 */
public interface SwipeStackListener {

    /**
     * Called when a view has been dismissed to the left.
     *
     * @param position The position of the view in the adapter currently in use.
     */
    void onViewSwipedToLeft(int position);

    /**
     * Called when a view has been dismissed to the right.
     *
     * @param position The position of the view in the adapter currently in use.
     */
    void onViewSwipedToRight(int position);

    /**
     * Called when the last view has been dismissed.
     */
    void onStackEmpty();

    /**
     *
     * @param position
     */
    void onStackTopVisible(int position);
}
