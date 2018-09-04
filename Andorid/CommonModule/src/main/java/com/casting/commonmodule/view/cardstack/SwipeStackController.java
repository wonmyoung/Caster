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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.database.DataSetObserver;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import android.widget.Adapter;

public class SwipeStackController extends DataSetObserver implements View.OnTouchListener {

    private static final float CLICK_EVENT_MINIMUM_AREA = 200;
    private static final float CLICK_EVENT_TIME_AREA = 200;

    private final SwipeStack mSwipeStack;

    private View mObservedView;

    private boolean mListenForTouchEvents;

    private float mEventDownX;
    private float mEventDownY;
    private float mEventDownTime;
    private float mInitialX;
    private float mInitialY;
    private float mRotateDegrees = SwipeStack.DEFAULT_SWIPE_ROTATION;
    private float mOpacityEnd = SwipeStack.DEFAULT_SWIPE_OPACITY;

    private int mPointerId;
    private int mAnimationDuration = SwipeStack.DEFAULT_ANIMATION_DURATION;

    private static long m_lLastClickTime = 0;

    public SwipeStackController(SwipeStack swipeStack)
    {
        mSwipeStack = swipeStack;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        try
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    if (!mListenForTouchEvents || !mSwipeStack.isEnabled())
                    {
                        return false;
                    }

                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    mSwipeStack.onSwipeStart();
                    mPointerId = event.getPointerId(0);
                    mEventDownX = event.getX(mPointerId);
                    mEventDownY = event.getY(mPointerId);
                    mEventDownTime = SystemClock.elapsedRealtime();

                    return true;

                case MotionEvent.ACTION_MOVE:

                    int pointerIndex = event.findPointerIndex(mPointerId);
                    if (pointerIndex < 0) return false;

                    float dx = event.getX(pointerIndex) - mEventDownX;
                    float dy = event.getY(pointerIndex) - mEventDownY;

                    float newX = mObservedView.getX() + dx;
                    float newY = mObservedView.getY() + dy;

                    mObservedView.setX(newX);
                    mObservedView.setY(newY);

                    float dragDistanceX = newX - mInitialX;
                    float swipeProgress = Math.min(Math.max(dragDistanceX / mSwipeStack.getWidth(), -1), 1);

                    mSwipeStack.onSwipeProgress(swipeProgress);

                    if (mRotateDegrees > 0)
                    {
                        float rotation = mRotateDegrees * swipeProgress;
                        mObservedView.setRotation(rotation);
                    }

                    if (mOpacityEnd < 1f)
                    {
                        float alpha = 1 - Math.min(Math.abs(swipeProgress * 2), 1);
                        mObservedView.setAlpha(alpha);
                    }
                    return true;

                case MotionEvent.ACTION_UP:

                    float diffTime = (mEventDownTime == 0 ?
                            0 : (SystemClock.elapsedRealtime() - mEventDownTime));
                    float differenceX = (mEventDownX == 0 || event.getX() == 0 ?
                            0 : Math.abs(mEventDownX - event.getX()));
                    float differenceY = (mEventDownY == 0 || event.getY() == 0 ?
                            0 : Math.abs(mEventDownY - event.getY()));

                    if (differenceX < CLICK_EVENT_MINIMUM_AREA &&
                        differenceY < CLICK_EVENT_MINIMUM_AREA &&
                        diffTime < CLICK_EVENT_TIME_AREA)
                    {
                        Adapter adapter = mSwipeStack.getAdapter();

                        if (adapter instanceof SwipeViewClickListener)
                        {
                            SwipeViewClickListener clickListener = (SwipeViewClickListener) adapter;

                            clickListener.onViewClick(v);
                        }
                    }

                    v.getParent().requestDisallowInterceptTouchEvent(false);

                    mSwipeStack.onSwipeEnd();

                    checkViewPosition();
                    return true;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onChanged()
    {
        super.onChanged();

        if (mSwipeStack != null)
        {
            mSwipeStack.requestLayout();
        }
    }

    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    public void setRotation(float rotation) {
        mRotateDegrees = rotation;
    }

    public void setOpacityEnd(float alpha) {
        mOpacityEnd = alpha;
    }

    public void swipeView()
    {
        mObservedView.animate().cancel();
        mObservedView.animate()
                .x(-mSwipeStack.getWidth() + mObservedView.getX())
                .rotation(-mRotateDegrees)
                .alpha(0f)
                .setDuration(mAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        mSwipeStack.onViewSwipedToLeft();
                    }
                });
    }

    public void swipeViewToLeft()
    {
        int childCount = (mSwipeStack == null ? 0 : mSwipeStack.getChildCount());
        if (childCount > 0)
        {
            swipeViewToLeft(mAnimationDuration);
        }
    }

    private void swipeViewToLeft(int duration)
    {
        if (!isDoubleEffect())
        {
            if (!mListenForTouchEvents) return;

            mListenForTouchEvents = false;

            mObservedView.animate().cancel();
            mObservedView.animate()
                    .x(-mSwipeStack.getWidth() + mObservedView.getX())
                    .rotation(-mRotateDegrees)
                    .alpha(0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            mSwipeStack.onViewSwipedToLeft();
                        }
                    });
        }
        else
        {
            rollbackCurrentViewPosition();
        }
    }

    public void swipeViewToRight()
    {
        int childCount = (mSwipeStack == null ? 0 : mSwipeStack.getChildCount());
        if (childCount > 0)
        {
            swipeViewToRight(mAnimationDuration);
        }
    }

    private void swipeViewToRight(int duration)
    {
        if (!isDoubleEffect())
        {
            if (!mListenForTouchEvents) return;

            mListenForTouchEvents = false;
            mObservedView.animate().cancel();
            mObservedView.animate()
                    .x(mSwipeStack.getWidth() + mObservedView.getX())
                    .rotation(mRotateDegrees)
                    .alpha(0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            mSwipeStack.onViewSwipedToRight();
                        }
                    });
        }
        else
        {
            rollbackCurrentViewPosition();
        }
    }

    public void registerObservedView(View view, float initialX, float initialY)
    {
        if (view == null) return;

        mObservedView = view;
        mObservedView.setOnTouchListener(this);

        mInitialX = initialX;
        mInitialY = initialY;

        mListenForTouchEvents = true;
    }

    public void unregisterObservedView()
    {
        if (mObservedView != null) {
            mObservedView.setOnTouchListener(null);
        }

        mObservedView = null;
        mListenForTouchEvents = false;
    }

    public void rollBack()
    {
        if (!mSwipeStack.getRemovedViewStack().isEmpty())
        {
            final View v = mSwipeStack.getRemovedViewStack().pop();

            int top = mSwipeStack.getChildCount();

            mSwipeStack.addView(v, top);

            v.animate().x(0).rotation(0).alpha(1.0f)
                    .setDuration(mAnimationDuration)
                    .setListener(new AnimatorListenerAdapter()
                    {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            mSwipeStack.onViewRollBack();
                        }
                    });
        }

    }

    private void rollbackCurrentViewPosition()
    {
        mObservedView.animate().x(mInitialX).y(mInitialY).rotation(0).alpha(1)
                .setDuration(mAnimationDuration)
                .setInterpolator(new OvershootInterpolator(1.4f))
                .setListener(null);
    }

    private void checkViewPosition()
    {
        if (!mSwipeStack.isEnabled())
        {
            rollbackCurrentViewPosition();
            return;
        }

        float viewCenterHorizontal = mObservedView.getX() + (mObservedView.getWidth() / 2);
        float parentFirstThird = mSwipeStack.getWidth() / 3f;
        float parentLastThird = parentFirstThird * 2;

        if (viewCenterHorizontal < parentFirstThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_RIGHT)
        {
            mListenForTouchEvents = true;

            swipeViewToLeft(mAnimationDuration / 2);
        }
        else if (viewCenterHorizontal > parentLastThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_LEFT)
        {
            mListenForTouchEvents = true;

            swipeViewToRight(mAnimationDuration / 2);
        }
        else
        {
            rollbackCurrentViewPosition();
        }
    }

    private static boolean isDoubleEffect()
    {
        if (SystemClock.elapsedRealtime() - m_lLastClickTime <= 100)
        {
            return true;
        }
        m_lLastClickTime = SystemClock.elapsedRealtime();

        return false;
    }
}
