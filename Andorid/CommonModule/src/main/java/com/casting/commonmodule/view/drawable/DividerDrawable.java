package com.casting.commonmodule.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

public class DividerDrawable extends Drawable {

    public enum PathType {
        DASH, LINE, SHAPE, ROUND_SHAPE;
    }

    public static final int LEFT    = 0X0001;
    public static final int TOP     = 0X0010;
    public static final int RIGHT   = 0X0100;
    public static final int BOTTOM  = 0X1000;
    public static final int SHAPE_LINE_ALL = 0X1111;

    private int     mOrientation;
    private int     mLineWidth;
    private int     mPathColor;
    private int     mBackGroundColor = 0;
    private int     mVisibleShapeLine = SHAPE_LINE_ALL;

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private float mCornerRadius;

    private int mDelegatedLeft;
    private int mDelegatedTop;
    private int mDelegatedRight;
    private int mDelegatedBottom;

    private Paint       mPaint;
    private Path        mPath;
    private Rect        mRect;
    private RectF       mRectF;
    private Paint.Style mPaintStyle;
    private DividerDrawable.PathType    mPathType;
    private PathEffect                  mPathEffect;

    public DividerDrawable() {

        init();
    }

    private void init() {

        mOrientation = LinearLayout.VERTICAL;

        mLineWidth = 4;

        mPathColor = Color.BLUE;
        mBackGroundColor = Color.TRANSPARENT;

        mPath = new Path();

        mPaintStyle = Paint.Style.STROKE;
        mPathType = DividerDrawable.PathType.DASH;

        mPathEffect = new DashPathEffect(new float[]{10 , 5} , 10);

        mDelegatedLeft = -1;
        mDelegatedTop = -1;
        mDelegatedRight = -1;
        mDelegatedBottom = -1;

        mCornerRadius = 10;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        if (mPaint == null) {
            mPaint = new Paint();
        }

        mPaint.setColor(mBackGroundColor);
        mPaint.setStyle(Paint.Style.FILL);

        if (mRect == null) {
            mRect = new Rect();
        }
        mRect.set(0, 0, canvas.getWidth(), canvas.getHeight());

        canvas.drawRect(mRect, mPaint);

        mPaint.setColor(mPathColor);
        mPaint.setStyle(mPaintStyle);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setPathEffect(mPathEffect);

        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;

        int startX = 0, startY = 0, endX = 0, endY = 0;

        switch (mPathType) {
            case LINE:
            case DASH:
                if (mOrientation == LinearLayout.HORIZONTAL)
                {
                    startX = getPaddingLeft(); startY = centerY;
                    endX = (canvas.getWidth() - getPaddingRight());
                    endY = centerY;
                }
                else
                {
                    startX = centerX; startY = getPaddingTop();
                    endX = centerX;
                    endY = (canvas.getHeight() - getPaddingBottom());
                }

                mPath.moveTo(startX, startY);
                mPath.quadTo(startX, startY, endX, endY);

                canvas.drawPath(mPath, mPaint);
                break;

            case SHAPE:

                if (mVisibleShapeLine == SHAPE_LINE_ALL)
                {
                    if (mRect == null) {
                        mRect = new Rect();
                    }

                    startX = mLineWidth;

                    if (mDelegatedLeft > -1) {
                        startX += mDelegatedLeft;
                    }

                    startY = mLineWidth;

                    if (mDelegatedTop > -1) {
                        startY += mDelegatedTop;
                    }

                    endX = (canvas.getWidth() - mLineWidth);

                    if (mDelegatedRight > -1) {
                        endX -= mDelegatedRight;
                    }

                    endY = (canvas.getHeight() - mLineWidth);

                    if (mDelegatedBottom > -1) {
                        endY -= mDelegatedBottom;
                    }

                    mRect.set(startX, startY, endX, endY);

                    canvas.drawRect(mRect, mPaint);
                }
                else
                {
                    if ((mVisibleShapeLine & LEFT) == LEFT) {

                        startX = getPaddingLeft(); startY = getPaddingTop();
                        endX = getPaddingLeft();
                        endY = (canvas.getHeight() - getPaddingBottom());

                        mPath.moveTo(startX, startY);
                        mPath.quadTo(startX, startY, endX, endY);

                        canvas.drawPath(mPath, mPaint);
                    }

                    if ((mVisibleShapeLine & TOP) == TOP) {

                        startX = getPaddingLeft(); startY = getPaddingTop();
                        endX = (canvas.getWidth() - getPaddingRight());
                        endY = getPaddingTop();

                        mPath.moveTo(startX, startY);
                        mPath.quadTo(startX, startY, endX, endY);

                        canvas.drawPath(mPath, mPaint);
                    }

                    if ((mVisibleShapeLine & RIGHT) == RIGHT) {

                        startX = (canvas.getWidth() - getPaddingLeft()); startY = getPaddingTop();
                        endX = (canvas.getWidth() - getPaddingRight());
                        endY = (canvas.getHeight() - getPaddingBottom());

                        mPath.moveTo(startX, startY);
                        mPath.quadTo(startX, startY, endX, endY);

                        canvas.drawPath(mPath, mPaint);
                    }

                    if ((mVisibleShapeLine & BOTTOM) == BOTTOM) {

                        startX = getPaddingLeft(); startY = (canvas.getHeight() - getPaddingBottom());
                        endX = (canvas.getWidth() - getPaddingRight());
                        endY = (canvas.getHeight() - getPaddingBottom());

                        mPath.moveTo(startX, startY);
                        mPath.quadTo(startX, startY, endX, endY);

                        canvas.drawPath(mPath, mPaint);
                    }
                }

                break;

            case ROUND_SHAPE: {
                mPaint.setAntiAlias(true);

                if (mRectF == null) {
                    mRectF = new RectF();
                }
                int padding = (mLineWidth / 2);

                mRectF.set(padding, padding,
                        (canvas.getWidth() - padding), (canvas.getHeight() - padding));

                canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mPaint);
                break;
            }
        }
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

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.mPaddingLeft = paddingLeft;

        invalidateSelf();
    }

    public int getPaddingRight() {
        return mPaddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.mPaddingRight = paddingRight;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.mPaddingTop = paddingTop;

        invalidateSelf();
    }

    public int getPaddingBottom() {
        return mPaddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.mPaddingBottom = paddingBottom;

        invalidateSelf();
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;

        invalidateSelf();
    }

    public int getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(int lineWidth) {
        mLineWidth = lineWidth;

        invalidateSelf();
    }

    public int getPathColor() {
        return mPathColor;
    }

    public void setPathColor(int color) {
        mPathColor = color;

        invalidateSelf();
    }

    public void setPaint(Paint paint) {
        mPaint = paint;

        invalidateSelf();
    }

    public PathEffect getPathEffect() {
        return mPathEffect;
    }

    public void setPathEffect(PathEffect pathEffect) {
        mPathEffect = pathEffect;

        invalidateSelf();
    }

    public DividerDrawable.PathType getPathType() {
        return mPathType;
    }

    public void setPathType(DividerDrawable.PathType pathType) {
        mPathType = pathType;

        switch (mPathType) {
            case DASH:
                mPathEffect = new DashPathEffect(new float[]{10 , 5} , 10);
                break;

            case LINE:
            case SHAPE:
            case ROUND_SHAPE:
                mPathEffect = null;
                break;
        }

        invalidateSelf();
    }

    public int getBackgroundColor() {
        return mBackGroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackGroundColor = backgroundColor;

        invalidateSelf();
    }

    public RectF getRectF() {
        return mRectF;
    }

    public void setRectF(RectF rectF) {
        mRectF = rectF;

        invalidateSelf();
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.mCornerRadius = cornerRadius;

        invalidateSelf();
    }

    public int getVisibleShapeLine() {
        return mVisibleShapeLine;
    }

    public void setVisibleShapeLine(int visibleShapeLine) {
        mVisibleShapeLine = visibleShapeLine;

        invalidateSelf();
    }

    public Paint.Style getPaintStyle() {
        return mPaintStyle;
    }

    public void setPaintStyle(Paint.Style paintStyle) {
        mPaintStyle = paintStyle;
    }

    public int getDelegatedLeft() {
        return mDelegatedLeft;
    }

    public void setDelegatedLeft(int delegatedLeft) {
        mDelegatedLeft = delegatedLeft;

        invalidateSelf();
    }

    public int getDelegatedTop() {
        return mDelegatedTop;
    }

    public void setDelegatedTop(int delegatedTop) {
        mDelegatedTop = delegatedTop;

        invalidateSelf();
    }

    public int getDelegatedRight() {
        return mDelegatedRight;
    }

    public void setDelegatedRight(int delegatedRight) {
        mDelegatedRight = delegatedRight;

        invalidateSelf();
    }

    public int getDelegatedBottom() {
        return mDelegatedBottom;
    }

    public void setDelegatedBottom(int delegatedBottom) {
        mDelegatedBottom = delegatedBottom;

        invalidateSelf();
    }

    public void setDelegatedPosition(int ... param) {
        mDelegatedLeft = (param != null && param.length > 0 ? param[0] : -1);
        mDelegatedTop = (param != null && param.length > 1 ? param[1] : -1);
        mDelegatedRight = (param != null && param.length > 2 ? param[2] : -1);
        mDelegatedBottom = (param != null && param.length > 3 ? param[3] : -1);

        invalidateSelf();
    }
}
