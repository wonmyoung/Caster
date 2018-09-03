package com.casting.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.casting.R;

public class CommonCardItem extends CardView
{
    public CommonCardItem(Context context)
    {
        super(context);

        init(context, null);
    }

    public CommonCardItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs);
    }

    public CommonCardItem(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context c, AttributeSet attrs)
    {
        LayoutInflater.from(c).inflate(R.layout.view_item_common_card, this);
    }
}
