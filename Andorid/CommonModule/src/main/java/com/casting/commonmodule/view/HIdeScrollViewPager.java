package com.casting.commonmodule.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by 신종진 on 2017-05-22.
 */

public class HIdeScrollViewPager extends ViewPager {

    private GestureDetector moDetector;
    private boolean mbScrollEnabled = true; // 이 것이 스크롤을 막아주는 중요 변수!

    private GestureDetector.OnGestureListener mlGestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent arg0) {
			/* 세로 스크롤 허용 */
            getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        }

        @Override
        public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
			/* 세로 스크롤시 호출되지 않음, 가로스크롤시만 호출되기에 이때 부모의 세로스크롤을 불허용 처리함 */
            getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent arg0) {

        }

        @Override
        public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
			/* 세로 스크롤시 호출되지 않음, 가로스크롤시만 호출되기에 이때 부모의 세로스크롤을 불허용 처리함 */
            getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }

        @Override
        public void onShowPress(MotionEvent arg0) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent arg0) {
            return false;
        }

    };

    /**
     * ViewPagerWidget Constructor.
     *
     * @param context
     *            : Context
     * @param attrs
     *            : AttributeSet
     */
    public HIdeScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        moDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isScrollEnabled()) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (mlWindowFocusChangedListener != null) {
            mlWindowFocusChangedListener.onWindowFocusChangedFromViewPager(hasWindowFocus);
        }

        super.onWindowFocusChanged(hasWindowFocus);
    }

    /**
     * isScrollEnabled.
     * @return mbScrollEnabled
     */
    public boolean isScrollEnabled() {
        return mbScrollEnabled;
    }

    /**
     * 이 메소드를 이용해서 스크롤을 풀어줍니다.
     */
    public void setPageScrollEnabled() {
        this.mbScrollEnabled = true;
    }

    /**
     * 이 메소드를 이용해서 스크롤을 막아줍니다.
     */
    public void setPageScrollDisabled() {
        this.mbScrollEnabled = false;
    }

    private IWindowFocusChanged mlWindowFocusChangedListener = null;

    /**
     *
     * @param listener
     *            {@link IWindowFocusChanged}
     */
    public void setWindowFocusChangedListener(IWindowFocusChanged listener) {
        mlWindowFocusChangedListener = listener;
    }

    /**
     *
     * @param context Context
     */
    private void init(Context context) {
        moDetector = new GestureDetector(context, mlGestureListener);

        setPageTransformer(false , new FadePageTransformer());
    }

    /**
     * startScrollPage..
     */
    private void startScrollPage() {

    }

    /**
     * stopScrollPage..
     */
    private void stopScrollPage() {

    }

    /**
     * IWindowFocusChanged.
     */
    public interface IWindowFocusChanged {
        /**
         *
         * @param hasWindowFocus
         *            : boolean
         */
        public void onWindowFocusChangedFromViewPager(boolean hasWindowFocus);
    }

    /**
     * @author JJShin
     */
    @Deprecated
    public class FadePageTransformer implements PageTransformer {
        @Override
        public void transformPage(final View view, float position) {

            // 16.11.11 LMW. Float의 값을 비교하여 같은지를 판단한다. 아래와 같이 코드를 변경한다.
            if (Float.compare(position, 0f) == 0 || Float.compare(position, -1f) == 0) {
                ViewHelper.setTranslationX(view , (view.getWidth() * -position));

                if (Float.compare(ViewHelper.getAlpha(view), 1f) == 0) {
                    ViewHelper.setAlpha(view , Math.abs(position));

                    float transAlpha = 1.0F - Math.abs(position);
                    ViewPropertyAnimator.animate(view).setDuration(500).alpha(transAlpha);
                }
            } else {
                if (position > 0.0f && position < 1.0f) {
                    ViewHelper.setTranslationX(view , (view.getWidth() * -position));
                    ViewHelper.setAlpha(view , 1.0F - Math.abs(position));
                } else if (position < 0.0f && position > -1.0f) {
                    ViewHelper.setTranslationX(view , (view.getWidth() * -position));
                    ViewHelper.setAlpha(view , 1.0F - Math.abs(position));
                }
            }
        }
    }
}
