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
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.model.insert.ItemSelectOptions;
import com.casting.view.ObserverView;
import com.nineoldandroids.view.ViewHelper;

import java.util.Observable;

public class InsertOptionsHorizontal extends FrameLayout implements View.OnClickListener {

    private View            mLineView;
    private FrameLayout     mItemLayout;

    private ItemSelectOptions   mOptionsData;

    public InsertOptionsHorizontal(@NonNull Context context)
    {
        super(context);

        init(context, null);
    }

    public InsertOptionsHorizontal(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs);
    }

    public InsertOptionsHorizontal(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
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
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int childCount = mItemLayout.getChildCount();
        if (childCount > 1)
        {
            int lineWidth = mItemLayout.getMeasuredWidth();

            View child1 = mItemLayout.getChildAt(0);

            int child1Width = child1.getMeasuredWidth();

            FrameLayout.LayoutParams lp1 = (LayoutParams) child1.getLayoutParams();
            lp1.leftMargin = 0;
            child1.setLayoutParams(lp1);
            child1.requestLayout();

            View child2 = mItemLayout.getChildAt((childCount - 1));

            int child2Width = child2.getMeasuredWidth();

            FrameLayout.LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
            lp2.leftMargin = 0;
            lp2.leftMargin += lineWidth;
            lp2.leftMargin -= child2Width;
            child2.setLayoutParams(lp2);
            child2.requestLayout();

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
                    remainingChild.requestLayout();
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
        int i = (int) v.getTag();

        ItemSelectOptions.Option option = mOptionsData.getOption(i);

        mOptionsData.setInsertedData(option);
    }

    public int getOptionsSize()
    {
        return (mOptionsData == null ? 0 : mOptionsData.getOptionsSize());
    }

    public void setSelectOptions(ItemSelectOptions itemSelectOptions)
    {
        mItemLayout.removeAllViews();

        mOptionsData = itemSelectOptions;

        int size = itemSelectOptions.getOptionsSize();

        for (int i = 0 ; i < size ; i++)
        {
            ItemSelectOptions.Option option = itemSelectOptions.getOption(i);

            addSelectableOption(i, option);
        }
    }

    private void addSelectableOption(int i, final ItemSelectOptions.Option option)
    {
        Context c = getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(c);

        FrameLayout.LayoutParams lp = new LayoutParams(
                (int) getResources().getDimension(R.dimen.dp40),
                LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.START;

        View view = layoutInflater.inflate(R.layout.view_item_option, null);
        view.setTag(i);
        view.setOnClickListener(this);
        view.setLayoutParams(lp);
        mItemLayout.addView(view);

        TextView textView = (TextView) view.findViewById(R.id.circlePointMessage);
        textView.setText(option.getName());

        View pointView = view.findViewById(R.id.circlePoint);

        ObserverView<View> observerView = new ObserverView<View>(pointView) {

            @Override
            protected void updateView(Observable observable, Object o) throws Exception
            {
                if (o instanceof ItemSelectOptions.Option)
                {
                    boolean selected = o.equals(option);

                    float scale = (selected ? 1.5f : 1.0f);

                    mRoot.setSelected(selected);

                    ViewHelper.setScaleX(mRoot, scale);
                    ViewHelper.setScaleY(mRoot, scale);
                }
            }
        };
        mOptionsData.addObserver(observerView);

        invalidate();
    }
}
