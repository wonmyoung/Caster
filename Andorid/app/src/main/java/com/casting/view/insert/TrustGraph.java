package com.casting.view.insert;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.utility.UtilityUI;

import java.util.ArrayList;

public class TrustGraph extends FrameLayout implements View.OnClickListener {

    private View            mLineView;
    private LinearLayout    mItemLayout;

    private class Point {

        private String Name;
        private Object Value;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public Object getValue() {
            return Value;
        }

        public void setValue(Object value) {
            Value = value;
        }
    }

    private ArrayList<Point> PointArrayList = new ArrayList<>();

    public TrustGraph(@NonNull Context context)
    {
        super(context);

        init(context, null);
    }

    public TrustGraph(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs);
    }

    public TrustGraph(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
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
        mItemLayout = new LinearLayout(c);
        mItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        mItemLayout.setLayoutParams(lp2);
        mItemLayout.setClipChildren(false);
        addView(mItemLayout);

        setClipChildren(false);

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int childCount = mItemLayout.getChildCount();
        if (childCount > 0)
        {

            int lineWidth = mItemLayout.getMeasuredWidth();
            int leftMarginUnit = (lineWidth / childCount);

            for (int i = 0 ; i < childCount ; i++)
            {
                View v = mItemLayout.getChildAt(i);

                int childWidth = (int) getResources().getDimension(R.dimen.dp80);

                int childX = 0;
                childX += (leftMarginUnit * i);
                childX -= (childWidth / 2);

                if (childX < 0) {
                    childX = 0;
                }

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
                lp.leftMargin = childX;

                v.setLayoutParams(lp);
            }

            mItemLayout.requestLayout();
        }
    }

    @Override
    public void onClick(View v)
    {

    }

    public void addPoint(String name, Object value)
    {
        Context c = getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(c);

        View view = layoutInflater.inflate(R.layout.view_item_trust_graph_point, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mItemLayout.addView(view);

        TextView textView = (TextView) view.findViewById(R.id.circlePointMessage);
        textView.setText(name);

        Point point = new Point();
        point.setName(name);
        point.setValue(value);

        PointArrayList.add(point);

        invalidate();
    }
}
