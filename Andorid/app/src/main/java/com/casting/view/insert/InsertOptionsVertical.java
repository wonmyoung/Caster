package com.casting.view.insert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.casting.R;
import com.casting.view.ObserverView;
import com.casting.view.insert.items.ItemSelectOptions;
import com.nineoldandroids.view.ViewHelper;

import java.util.Observable;

public class InsertOptionsVertical extends LinearLayout implements View.OnClickListener
{
    private ItemSelectOptions mOptionsData;

    public InsertOptionsVertical(@NonNull Context context)
    {
        super(context);

        init(context, null);
    }

    public InsertOptionsVertical(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs);
    }

    public InsertOptionsVertical(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(@NonNull Context c, @Nullable AttributeSet attrs)
    {
        setOrientation(VERTICAL);

        setClipChildren(false);
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
        removeAllViews();

        mOptionsData = itemSelectOptions;

        int size = itemSelectOptions.getOptionsSize();

        for (int i = 0 ; i < size ; i++)
        {
            ItemSelectOptions.Option option = itemSelectOptions.getOption(i);

            addSelectableOption(i, option);
        }

        requestLayout();
    }

    private void addSelectableOption(int i, final ItemSelectOptions.Option option)
    {
        Context c = getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(c);

        int childCount = getChildCount();

        LinearLayout.LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.START;
        lp.topMargin = (getChildCount() == 0 ? 0 : (int) getResources().getDimension(R.dimen.dp10));

        View view = layoutInflater.inflate(R.layout.view_item_option_wide, null);
        view.setTag(i);
        view.setOnClickListener(this);
        addViewInLayout(view, childCount, lp);

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

                    float scale = (selected ? 1.1f : 1.0f);

                    mRoot.setSelected(selected);

                    ViewHelper.setScaleX(mRoot, scale);
                    ViewHelper.setScaleY(mRoot, scale);
                }
            }
        };
        mOptionsData.addObserver(observerView);
    }
}
