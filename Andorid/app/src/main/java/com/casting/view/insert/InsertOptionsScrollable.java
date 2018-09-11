package com.casting.view.insert;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.model.insert.ItemScrollableOption;
import com.nineoldandroids.view.ViewHelper;

public class InsertOptionsScrollable extends FrameLayout implements View.OnTouchListener {

    private View            mLineView;
    private FrameLayout     mItemLayout;

    private View        mMinPointView;
    private View        mMaxPointView;
    private View        mScrollableView;

    private int     mScrollEndX;
    private int     mPointerId;
    private float   mEventDownX;

    private ItemScrollableOption    mItemScrollableOption;

    public InsertOptionsScrollable(@NonNull Context context) {
        super(context);

        init(context, null);
    }

    public InsertOptionsScrollable(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public InsertOptionsScrollable(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(@NonNull Context c, @Nullable AttributeSet attrs)
    {
        Resources res = getResources();

        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.dp2));
        lp1.gravity = Gravity.TOP;
        lp1.topMargin = (int) res.getDimension(R.dimen.dp13);
        mLineView = new View(c);
        mLineView.setLayoutParams(lp1);
        mLineView.setBackgroundColor(UtilityUI.getColor(c, R.color.color_999999));
        addView(mLineView);

        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        lp2.gravity = Gravity.TOP;
        mItemLayout = new FrameLayout(c);
        mItemLayout.setLayoutParams(lp2);
        mItemLayout.setClipChildren(false);
        addView(mItemLayout);

        setClipChildren(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMinPointView != null && mMaxPointView != null)
        {
            int lineWidth = mItemLayout.getMeasuredWidth();

            FrameLayout.LayoutParams lp1 = (LayoutParams) mMinPointView.getLayoutParams();
            lp1.leftMargin = 0;
            lp1.gravity = Gravity.START;
            mMinPointView.setLayoutParams(lp1);

            FrameLayout.LayoutParams lp2 = (LayoutParams) mMaxPointView.getLayoutParams();
            lp2.leftMargin = 0;
            lp2.leftMargin += lineWidth;
            lp2.leftMargin -= mMaxPointView.getMeasuredWidth();
            lp2.gravity = Gravity.START;
            mMaxPointView.setLayoutParams(lp2);

            mScrollEndX = lineWidth - mMaxPointView.getMeasuredWidth();

            mScrollableView.setOnTouchListener(this);

            FrameLayout.LayoutParams lp3 = (LayoutParams) mScrollableView.getLayoutParams();
            lp3.leftMargin = 0;

            int selectedValue = (mItemScrollableOption != null &&
                    mItemScrollableOption.getInsertedData() != null ?
                    (int) mItemScrollableOption.getInsertedData() : -1);
            if (selectedValue == -1)
            {
                lp3.leftMargin += (lineWidth / 2);
                lp3.leftMargin -= (mScrollableView.getMeasuredWidth() / 2);
            }
            else
            {
                int maxValue = mItemScrollableOption.getMaxValue();
                int minValue = mItemScrollableOption.getMinValue();

                int percentage = (selectedValue * 100) / (maxValue - minValue);

                lp3.leftMargin = (mScrollEndX * percentage) / 100;
            }

            lp3.gravity = Gravity.START;
            mScrollableView.setLayoutParams(lp3);

            mItemLayout.requestLayout();

            FrameLayout.LayoutParams lineParam = (LayoutParams) mLineView.getLayoutParams();
            lineParam.leftMargin = (mMinPointView.getMeasuredWidth() / 2);
            lineParam.rightMargin = (mMaxPointView.getMeasuredWidth() / 2);
            mLineView.setLayoutParams(lineParam);
            mLineView.requestLayout();

            setWillNotDraw(true);
        }
    }

    public void setScrollableOption(ItemScrollableOption itemScrollableOption)
    {
        mItemScrollableOption = itemScrollableOption;

        Context c = getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(c);

        mMinPointView= layoutInflater.inflate(R.layout.view_item_option, null);
        mMinPointView.setLayoutParams(new LayoutParams(
                (int) getResources().getDimension(R.dimen.dp40), LayoutParams.WRAP_CONTENT));
        mItemLayout.addView(mMinPointView);

        TextView textView1 = (TextView) mMinPointView.findViewById(R.id.circlePointMessage);
        textView1.setText(itemScrollableOption.getMinValuePrefix());

        mMaxPointView= layoutInflater.inflate(R.layout.view_item_option, null);
        mMaxPointView.setLayoutParams(new LayoutParams(
                (int) getResources().getDimension(R.dimen.dp40), LayoutParams.WRAP_CONTENT));
        mItemLayout.addView(mMaxPointView);

        TextView textView2 = (TextView) mMaxPointView.findViewById(R.id.circlePointMessage);
        textView2.setText(itemScrollableOption.getMaxValuePrefix());

        mScrollableView = layoutInflater.inflate(R.layout.view_item_option, null);
        mScrollableView.setLayoutParams(new LayoutParams(
                (int) getResources().getDimension(R.dimen.dp60), LayoutParams.WRAP_CONTENT));
        mItemLayout.addView(mScrollableView);

        TextView textView3 = (TextView) mScrollableView.findViewById(R.id.circlePointMessage);
        textView3.setText(itemScrollableOption.getScrollPrefix());

        View pointView = mScrollableView.findViewById(R.id.circlePoint);

        pointView.setSelected(true);

        ViewHelper.setScaleX(pointView, 1.5f);
        ViewHelper.setScaleY(pointView, 1.5f);

        setWillNotDraw(false);

        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                mScrollableView.getParent().requestDisallowInterceptTouchEvent(true);

                mPointerId = event.getPointerId(0);
                mEventDownX = event.getX(mPointerId);
                return true;

            case MotionEvent.ACTION_MOVE:

                int pointerIndex = event.findPointerIndex(mPointerId);
                if (pointerIndex < 0) return false;

                float dx = event.getX(pointerIndex) - mEventDownX;
                float newX = mScrollableView.getX() + dx;

                if (newX < 0) {
                    newX = 0;
                }
                if (newX > mScrollEndX) {
                    newX = mScrollEndX;
                }

                mScrollableView.setX(newX);

                int percentage = (int) ((newX * 100) / mScrollEndX);

                int maxValue = mItemScrollableOption.getMaxValue();
                int minValue = mItemScrollableOption.getMinValue();
                int selectedValue = minValue;

                selectedValue += ((maxValue - minValue) * percentage) / 100;

                mItemScrollableOption.setInsertedData(selectedValue);
                return true;

            case MotionEvent.ACTION_UP:

                mScrollableView.getParent().requestDisallowInterceptTouchEvent(false);

                return true;
        }

        return false;
    }
}
