package com.casting.commonmodule.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by 신종진 on 2017-05-22.
 */

public class HideScroller extends Scroller {

    private static final int AUTOSCROLL_DURATION = 500;

    private boolean mbAutoScroll = true;

    /**
     *
     * @param context Context
     */
    public HideScroller(Context context) {
        super(context);
    }

    /**
     *
     * @param context context
     * @param interpolator Interpolator
     */
    public HideScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    /**
     *
     * @return mbAutoScroll
     */
    public boolean isAutoScroll() {
        return mbAutoScroll;
    }

    /**
     *
     * @param autoscroll boolean
     */
    public void setAutoScroll(boolean autoscroll) {
        this.mbAutoScroll = autoscroll;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        if (isAutoScroll()) {
            duration = AUTOSCROLL_DURATION;
        }

        super.startScroll(startX, startY, dx, dy, duration);
    }
}
