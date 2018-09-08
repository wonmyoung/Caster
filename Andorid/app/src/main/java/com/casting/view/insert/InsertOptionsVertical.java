package com.casting.view.insert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class InsertOptionsVertical extends FrameLayout
{

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

    }
}
