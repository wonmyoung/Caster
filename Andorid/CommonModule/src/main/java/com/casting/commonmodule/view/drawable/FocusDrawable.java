package com.casting.commonmodule.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FocusDrawable extends Drawable {

    private Rect    mRect;
    private Paint   mPaint;
    private int     mOutSideColor;
    private int     mFocusLeft;
    private int     mFocusTop;
    private int     mFocusRight;
    private int     mFocusBottom;

    public FocusDrawable() {
        mOutSideColor = Color.parseColor("#a0000000");

        mFocusLeft = 0;
        mFocusTop = 0;
        mFocusRight = 0;
        mFocusBottom = 0;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        if (mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setColor(mOutSideColor);
        mPaint.setStyle(Paint.Style.FILL);

        if (mRect == null) {
            mRect = new Rect();
        }

        mRect.set(0, 0, mFocusLeft, canvas.getHeight());
        canvas.drawRect(mRect, mPaint);

        mRect.set(mFocusLeft, mFocusBottom, canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(mRect, mPaint);

        mRect.set(mFocusRight, 0, canvas.getWidth(), mFocusBottom);
        canvas.drawRect(mRect, mPaint);

        mRect.set(mFocusLeft, 0, mFocusRight, mFocusTop);
        canvas.drawRect(mRect, mPaint);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    public int getFocusLeft() {
        return mFocusLeft;
    }

    public void setFocusLeft(int focusLeft) {
        mFocusLeft = focusLeft;

        invalidateSelf();
    }

    public int getFocusTop() {
        return mFocusTop;
    }

    public void setFocusTop(int focusTop) {
        mFocusTop = focusTop;

        invalidateSelf();
    }

    public int getFocusRight() {
        return mFocusRight;
    }

    public void setFocusRight(int focusRight) {
        mFocusRight = focusRight;

        invalidateSelf();
    }

    public int getFocusBottom() {
        return mFocusBottom;
    }

    public void setFocusBottom(int focusBottom) {
        mFocusBottom = focusBottom;

        invalidateSelf();
    }

    public void setFocus(int ... param) {
        mFocusLeft = (param != null && param.length > 0 ? param[0] : 0);
        mFocusTop = (param != null && param.length > 1 ? param[1] : 0);
        mFocusRight = (param != null && param.length > 2 ? param[2] : 0);
        mFocusBottom = (param != null && param.length > 3 ? param[3] : 0);

        invalidateSelf();
    }
}
