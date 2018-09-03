/*
 * Copyright (C) 2016 Frederik Schweiger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.casting.commonmodule.view.cardstack;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;

import com.casting.commonmodule.R;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class SwipeStack extends ViewGroup
{
    public static final int SWIPE_DIRECTION_BOTH = 0;
    public static final int SWIPE_DIRECTION_ONLY_LEFT = 1;
    public static final int SWIPE_DIRECTION_ONLY_RIGHT = 2;

    public static final int DEFAULT_ANIMATION_DURATION = 300;
    public static final int DEFAULT_STACK_SIZE = 3;
    public static final int DEFAULT_STACK_ROTATION = 8;

    public static final float DEFAULT_SWIPE_ROTATION = 30f;
    public static final float DEFAULT_SWIPE_OPACITY = 1f;
    public static final float DEFAULT_SCALE_FACTOR = 1f;

    public static final boolean DEFAULT_DISABLE_HW_ACCELERATION = true;

    private static final String KEY_SUPER_STATE     = "superState";
    private static final String KEY_CURRENT_INDEX   = "currentIndex";

    private Adapter mAdapter;
    private Random  mRandom;

    private int mAllowedSwipeDirections;
    private int mAnimationDuration;
    private int mCurrentViewIndex;
    private int mNumberOfStackedViews;
    private int mViewSpacing;
    private int mViewRotation;

    private float mSwipeRotation;
    private float mSwipeOpacity;
    private float mScaleFactor;

    private boolean mDisableHwAcceleration;
    private boolean mIsFirstLayout = true;

    private View mTopView;

    private SwipeStackController    mSwipeStackController;

    private LinkedList<SwipeStackListener>      mStackListenerList;
    private LinkedList<SwipeProgressListener>   mProgressListenerList;

    private Stack<View> mRemovedViewStack = new Stack<>();

    public SwipeStack(Context context)
    {
        this(context, null);
    }

    public SwipeStack(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);

        init(context, attrs);
    }

    public SwipeStack(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context c, AttributeSet attributeSet)
    {
        TypedArray attrs = getContext().obtainStyledAttributes(attributeSet, R.styleable.SwipeStack);

        try {
            mAllowedSwipeDirections =
                    attrs.getInt(R.styleable.SwipeStack_allowed_swipe_directions,
                            SWIPE_DIRECTION_BOTH);
            mAnimationDuration =
                    attrs.getInt(R.styleable.SwipeStack_animation_duration,
                            DEFAULT_ANIMATION_DURATION);
            mNumberOfStackedViews =
                    attrs.getInt(R.styleable.SwipeStack_stack_size, DEFAULT_STACK_SIZE);
            mViewSpacing =
                    attrs.getDimensionPixelSize(R.styleable.SwipeStack_stack_spacing,
                            getResources().getDimensionPixelSize(R.dimen.default_stack_spacing));
            mViewRotation =
                    attrs.getInt(R.styleable.SwipeStack_stack_rotation, DEFAULT_STACK_ROTATION);
            mSwipeRotation =
                    attrs.getFloat(R.styleable.SwipeStack_swipe_rotation, DEFAULT_SWIPE_ROTATION);
            mSwipeOpacity =
                    attrs.getFloat(R.styleable.SwipeStack_swipe_opacity, DEFAULT_SWIPE_OPACITY);
            mScaleFactor =
                    attrs.getFloat(R.styleable.SwipeStack_scale_factor, DEFAULT_SCALE_FACTOR);
            mDisableHwAcceleration =
                    attrs.getBoolean(R.styleable.SwipeStack_disable_hw_acceleration,
                            DEFAULT_DISABLE_HW_ACCELERATION);
        }
        finally
        {
            attrs.recycle();
        }

        mRandom = new Random();

        setClipToPadding(false);
        setClipChildren(false);

        mSwipeStackController = new SwipeStackController(this);
        mSwipeStackController.setAnimationDuration(mAnimationDuration);
        mSwipeStackController.setRotation(mSwipeRotation);
        mSwipeStackController.setOpacityEnd(mSwipeOpacity);
    }

    @Override
    public Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putInt(KEY_CURRENT_INDEX, mCurrentViewIndex - getChildCount());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            mCurrentViewIndex = bundle.getInt(KEY_CURRENT_INDEX);

            state = bundle.getParcelable(KEY_SUPER_STATE);
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {

        if (mAdapter != null && !mAdapter.isEmpty())
        {
            int itemCount = mAdapter.getCount();
            int visibleViewCount = getChildCount();

            if (visibleViewCount < mNumberOfStackedViews)
            {
                for (int x = visibleViewCount; x < mNumberOfStackedViews && mCurrentViewIndex < itemCount; x++)
                {
                    addNextView();
                }
            }
            else if (visibleViewCount > mNumberOfStackedViews)
            {
                for (int x = visibleViewCount; x > mNumberOfStackedViews && mCurrentViewIndex < itemCount; x--)
                {
                    removeBottomView();
                }
            }

            reorderItems();

            if (mAdapter.getCount() > 0)
            {
                int currentPosition = getCurrentPosition();

                notifyStackPop(currentPosition);
            }
            else
            {
                notifyStackEmpty();
            }

            mIsFirstLayout = false;
        }
        else
        {
            mCurrentViewIndex = 0;

            removeAllViewsInLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public void onSwipeStart()
    {
        int currentPosition = getCurrentPosition();

        notifySwipeStart(currentPosition);
    }

    public void onSwipeProgress(float progress)
    {
        int currentPosition = getCurrentPosition();

        notifySwipeProgress(currentPosition, progress);
    }

    public void onSwipeEnd()
    {
        notifySwipeEnd(getCurrentPosition());
    }

    public void onViewSwipedToLeft()
    {
        removeTopView();

        notifyStackSwipedLeft(getCurrentPosition());
    }

    public void onViewSwipedToRight()
    {
        removeTopView();

        notifyStackSwipedRight(getCurrentPosition());
    }

    public void onViewRollBack()
    {
        if (getChildCount() > 0)
        {
            int visibleViewCount = getChildCount();
            if (visibleViewCount > mNumberOfStackedViews)
            {
                notifyRollBack(getCurrentPosition());
            }
        }
    }

    /**
     * Returns the current adapter position.
     *
     * @return The current position.
     */
    public int getCurrentPosition()
    {
        return mCurrentViewIndex - getChildCount();
    }

    /**
     * Returns the adapter currently in use in this SwipeStack.
     *
     * @return The adapter currently used to display data in this SwipeStack.
     */
    public Adapter getAdapter()
    {
        return mAdapter;
    }

    /**
     * Sets the data behind this SwipeView.
     *
     * @param adapter The Adapter which is responsible for maintaining the
     *                data backing this list and for producing a view to represent an
     *                item in that data set.
     * @see #getAdapter()
     */
    public void setAdapter(Adapter adapter)
    {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mSwipeStackController);
        }

        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mSwipeStackController);
    }

    /**
     * Returns the allowed swipe directions.
     *
     * @return The currently allowed swipe directions.
     */
    public int getAllowedSwipeDirections() {
        return mAllowedSwipeDirections;
    }

    /**
     * Sets the allowed swipe directions.
     *
     * @param directions One of {@link #SWIPE_DIRECTION_BOTH},
     *                   {@link #SWIPE_DIRECTION_ONLY_LEFT}, or {@link #SWIPE_DIRECTION_ONLY_RIGHT}.
     */
    public void setAllowedSwipeDirections(int directions) {
        mAllowedSwipeDirections = directions;
    }

    /**
     * Register a callback to be invoked when the user has swiped the top view
     * left / right or when the stack gets empty.
     *
     * @param listener The callback that will run
     */
    public void addStackListener(@Nullable SwipeStackListener listener)
    {
        if (mStackListenerList == null) {
            mStackListenerList = new LinkedList<>();
        }

        mStackListenerList.add(listener);
    }

    public void removeStackListener(SwipeStackListener listener)
    {
        if (mStackListenerList != null) {
            mStackListenerList.remove(listener);
        }
    }

    /**
     * Register a callback to be invoked when the user starts / stops interacting
     * with the top view of the stack.
     *
     * @param listener The callback that will run
     */
    public void addSwipeProgressListener(@Nullable SwipeProgressListener listener)
    {
        if (mProgressListenerList == null) {
            mProgressListenerList = new LinkedList<>();
        }

        mProgressListenerList.add(listener);
    }

    public void removeSwipeProgressListener(@Nullable SwipeProgressListener listener)
    {
        if (mStackListenerList != null) {
            mStackListenerList.remove(listener);
        }
    }

    /**
     * Get the view from the top of the stack.
     *
     * @return The view if the stack is not empty or null otherwise.
     */
    public View getTopView() {
        return mTopView;
    }

    public Stack<View> getRemovedViewStack()
    {
        return mRemovedViewStack;
    }

    public int getViewRotation() {
        return mViewRotation;
    }

    public void setViewRotation(int viewRotation) {
        this.mViewRotation = viewRotation;
        requestLayout();
    }

    public SwipeStackController getSwipeStackController()
    {
        return mSwipeStackController;
    }

    private void notifyStackPop(int position)
    {
        if (mStackListenerList != null)
        {
            for (SwipeStackListener swipeStackListener : mStackListenerList)
            {
                swipeStackListener.onStackTopVisible(position);
            }
        }
    }

    private void notifyStackSwipedLeft(int position)
    {
        if (mStackListenerList != null)
        {
            for (SwipeStackListener swipeStackListener : mStackListenerList)
            {
                swipeStackListener.onViewSwipedToLeft(position);
            }
        }
    }

    private void notifyStackSwipedRight(int position)
    {
        if (mStackListenerList != null)
        {
            for (SwipeStackListener swipeStackListener : mStackListenerList)
            {
                swipeStackListener.onViewSwipedToRight(position);
            }
        }
    }

    private void notifyStackEmpty()
    {
        if (mStackListenerList != null)
        {
            for (SwipeStackListener swipeStackListener : mStackListenerList)
            {
                swipeStackListener.onStackEmpty();
            }
        }
    }

    private void notifySwipeStart(int position)
    {
        if (mProgressListenerList != null)
        {
            for (SwipeProgressListener swipeProgressListener : mProgressListenerList)
            {
                swipeProgressListener.onSwipeStart(position);
            }
        }
    }

    private void notifySwipeProgress(int position, float progress)
    {
        if (mProgressListenerList != null)
        {
            for (SwipeProgressListener swipeProgressListener : mProgressListenerList)
            {
                swipeProgressListener.onSwipeProgress(position, progress);
            }
        }
    }

    private void notifySwipeEnd(int position)
    {
        if (mProgressListenerList != null)
        {
            for (SwipeProgressListener swipeProgressListener : mProgressListenerList)
            {
                swipeProgressListener.onSwipeEnd(position);
            }
        }
    }

    private void notifyRollBack(int position)
    {
        if (mStackListenerList != null)
        {
            for (SwipeStackListener swipeStackListener : mStackListenerList)
            {
                swipeStackListener.onStackRollBack(position);
            }
        }
    }

    private void addNextView()
    {
        Log.d("confirm" , ">> confirm addNextView ");

        if (mCurrentViewIndex < mAdapter.getCount())
        {
            View view = mAdapter.getView(mCurrentViewIndex, null, this);

            view.setTag(R.id.new_view, true);

            if (!mDisableHwAcceleration)
            {
                view.setLayerType(LAYER_TYPE_HARDWARE, null);
            }

            if (mViewRotation > 0)
            {
                view.setRotation(mRandom.nextInt(mViewRotation) - (mViewRotation / 2));
            }

            int width = getWidth() - (getPaddingLeft() + getPaddingRight());
            int height = getHeight() - (getPaddingTop() + getPaddingBottom());

            LayoutParams params = view.getLayoutParams();

            if (params == null) {
                params = new LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
            }

            int measureSpecWidth = MeasureSpec.AT_MOST;
            int measureSpecHeight = MeasureSpec.AT_MOST;

            if (params.width == LayoutParams.MATCH_PARENT)
            {
                measureSpecWidth = MeasureSpec.EXACTLY;
            }

            if (params.height == LayoutParams.MATCH_PARENT)
            {
                measureSpecHeight = MeasureSpec.EXACTLY;
            }

            view.measure(measureSpecWidth | width, measureSpecHeight | height);

            addViewInLayout(view, 0, params, true);

            mCurrentViewIndex++;
        }
    }

    private void reorderItems()
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            View childView = getChildAt(i);

            int topViewIndex = getChildCount() - 1;

            int distanceToViewAbove = (topViewIndex * mViewSpacing) - (i * mViewSpacing);

            int newPositionX = (getWidth() - childView.getMeasuredWidth()) / 2;
            int newPositionY = distanceToViewAbove + getPaddingTop();

            childView.layout(newPositionX, getPaddingTop(),
                    newPositionX + childView.getMeasuredWidth(),
                    getPaddingTop() + childView.getMeasuredHeight());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                childView.setTranslationZ(i);
            }

            boolean isNewView = (boolean) childView.getTag(R.id.new_view);

            float scaleFactor = (float) Math.pow(mScaleFactor, getChildCount() - i);

            if (i == topViewIndex)
            {
                mSwipeStackController.unregisterObservedView();

                mTopView = childView;

                mSwipeStackController.registerObservedView(mTopView, newPositionX, newPositionY);
            }

            if (!mIsFirstLayout)
            {
                if (isNewView)
                {
                    childView.setTag(R.id.new_view, false);
                    childView.setAlpha(0);
                    childView.setY(newPositionY);
                    childView.setScaleY(scaleFactor);
                    childView.setScaleX(scaleFactor);
                }

                childView.animate()
                        .y(newPositionY)
                        .scaleX(scaleFactor)
                        .scaleY(scaleFactor)
                        .alpha(1)
                        .setDuration(mAnimationDuration);

            }
            else
            {
                childView.setTag(R.id.new_view, false);
                childView.setY(newPositionY);
                childView.setScaleY(scaleFactor);
                childView.setScaleX(scaleFactor);
            }
        }
    }

    private void removeTopView()
    {
        if (mTopView != null)
        {
            mRemovedViewStack.add(mTopView);

            removeView(mTopView);
        }
    }

    private void removeBottomView()
    {
        Log.d("confirm" , ">> confirm removeBottomView ");

        if (getChildCount() > 0)
        {
            View v = getChildAt(0);

            removeViewInLayout(v);

            mCurrentViewIndex--;
        }
    }

    /**
     * Resets the current adapter position and repopulates the stack.
     */
    public void resetStack()
    {
        mCurrentViewIndex = 0;

        removeAllViewsInLayout();

        mIsFirstLayout = true;

        requestLayout();
    }
}
