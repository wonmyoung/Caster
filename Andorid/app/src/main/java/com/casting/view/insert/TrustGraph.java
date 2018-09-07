package com.casting.view.insert;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.view.ObserverView;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TrustGraph extends FrameLayout implements View.OnClickListener {

    private View            mLineView;
    private FrameLayout     mItemLayout;

    public class Point {

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

    private class SelectedPoint extends Observable
    {
        private Point target;

        public Point getPoint()
        {
            return target;
        }

        public void setPoint(Point p)
        {
            this.target = p;

            setChanged();
            notifyObservers(target);
        }
    }

    private SelectedPoint       mSelectedPoint;

    private ArrayList<Point>    PointArrayList = new ArrayList<>();

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
        mItemLayout = new FrameLayout(c);
        mItemLayout.setLayoutParams(lp2);
        mItemLayout.setClipChildren(false);
        addView(mItemLayout);

        setClipChildren(false);

        setWillNotDraw(false);

        mSelectedPoint = new SelectedPoint();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int childCount = mItemLayout.getChildCount();
        if (childCount > 0)
        {
            View child1 = mItemLayout.getChildAt(0);

            int child1Width = child1.getMeasuredWidth();

            FrameLayout.LayoutParams lp1 = (LayoutParams) child1.getLayoutParams();
            lp1.leftMargin = 0;
            child1.setLayoutParams(lp1);

            int lineWidth = mItemLayout.getMeasuredWidth();

            View child2 = mItemLayout.getChildAt((childCount - 1));

            int child2Width = child2.getMeasuredWidth();

            FrameLayout.LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
            lp2.leftMargin = 0;
            lp2.leftMargin += lineWidth;
            lp2.leftMargin -= child2Width;
            child2.setLayoutParams(lp2);

            if (childCount > 2)
            {
                int givenWidth = lineWidth;
                givenWidth -= (child1Width / 2);
                givenWidth -= (child2Width / 2);
                int distance = (givenWidth / (childCount - 1));

                for (int i = 1 ; i < (childCount - 1) ; i++)
                {
                    View remainingChild = mItemLayout.getChildAt(i);

                    FrameLayout.LayoutParams lp = (LayoutParams) remainingChild.getLayoutParams();
                    lp.leftMargin = (distance * i) - (remainingChild.getMeasuredWidth() / 2);
                    lp.leftMargin += (child1Width / 2);

                    remainingChild.setLayoutParams(lp);
                }
            }

            mItemLayout.requestLayout();

            FrameLayout.LayoutParams lineParam = (LayoutParams) mLineView.getLayoutParams();
            lineParam.leftMargin = (child1Width / 2);
            lineParam.rightMargin = (child2Width / 2);
            mLineView.setLayoutParams(lineParam);
            mLineView.requestLayout();
        }
    }

    @Override
    public void onClick(View v)
    {
        int selected = (int) v.getTag();

        Point point = PointArrayList.get(selected);

        mSelectedPoint.setPoint(point);
    }

    public ArrayList<Point> getPointArrayList()
    {
        return PointArrayList;
    }

    public int getPointArrayListSize()
    {
        return (PointArrayList == null ? 0 : PointArrayList.size());
    }

    public void setDefaultSelected()
    {
        int size = (PointArrayList == null ? 0 : PointArrayList.size());
        if (size > 0)
        {
            int middle = size / 2;

            Point point = PointArrayList.get(middle);

            mSelectedPoint.setPoint(point);
        }
    }

    public void addObserver(Observer observer)
    {
        if (mSelectedPoint != null)
        {
            mSelectedPoint.addObserver(observer);
        }
    }

    public void addPoint(String name, Object value)
    {
        int index = PointArrayList.size();

        final Point point = new Point();
        point.setName(name);
        point.setValue(value);

        PointArrayList.add(point);

        Context c = getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(c);

        FrameLayout.LayoutParams lp = new LayoutParams(
                (int) getResources().getDimension(R.dimen.dp40),
                LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.START;

        View view = layoutInflater.inflate(R.layout.view_item_trust_graph_point, null);
        view.setTag(index);
        view.setOnClickListener(this);
        view.setLayoutParams(lp);
        mItemLayout.addView(view);

        TextView textView = (TextView) view.findViewById(R.id.circlePointMessage);
        textView.setText(name);

        View pointView = view.findViewById(R.id.circlePoint);

        ObserverView<View> observerView = new ObserverView<View>(pointView) {

            @Override
            protected void updateView(Observable observable, Object o) throws Exception
            {
                if (o instanceof Point)
                {
                    boolean selected = o.equals(point);

                    float scale = (selected ? 1.5f : 1.0f);

                    mRoot.setSelected(selected);

                    ViewHelper.setScaleX(mRoot, scale);
                    ViewHelper.setScaleY(mRoot, scale);
                }
            }
        };
        mSelectedPoint.addObserver(observerView);

        invalidate();
    }
}
