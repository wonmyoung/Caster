package com.casting.commonmodule.view.cardstack;

public interface SwipeProgressListener {
    /**
     * Called when the user starts interacting with the top view of the stack.
     *
     * @param position The position of the view in the currently set adapter.
     */
    void onSwipeStart(int position);

    /**
     * Called when the user is dragging the top view of the stack.
     *
     * @param position The position of the view in the currently set adapter.
     * @param progress Represents the horizontal dragging position in relation to
     *                 the start position of the drag.
     */
    void onSwipeProgress(int position, float progress);

    /**
     * Called when the user has stopped interacting with the top view of the stack.
     *
     * @param position The position of the view in the currently set adapter.
     */
    void onSwipeEnd(int position);
}
