package com.casting.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.utility.UtilityUI;

public class CustomTabLayout extends TabLayout implements TabLayout.OnTabSelectedListener {

    public CustomTabLayout(Context context) {
        super(context);

        init(context, null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        addOnTabSelectedListener(this);
    }

    public void addTextTab(String tabText)
    {
        Context c = getContext();

        TabLayout.Tab tab = this.newTab();

        TextView textView = new TextView(c);
        textView.setText(tabText);
        textView.setTextSize(12);
        textView.setTextColor(UtilityUI.getColor(c, R.color.color_7c7c7c));
        textView.setSingleLine();
        textView.setMaxLines(1);
        textView.setGravity(Gravity.CENTER);

        tab.setCustomView(textView);

        this.addTab(tab);
    }

    @Override
    public void onTabSelected(Tab tab)
    {
        View v = tab.getCustomView();

        if (v != null && v instanceof TextView)
        {
            Context c = getContext();

            TextView textView = (TextView) v;
            textView.setTextColor(UtilityUI.getColor(c, R.color.mainColor0));
        }

    }

    @Override
    public void onTabUnselected(Tab tab)
    {
        View v = tab.getCustomView();

        if (v != null && v instanceof TextView)
        {
            Context c = getContext();

            TextView textView = (TextView) v;
            textView.setTextColor(UtilityUI.getColor(c, R.color.color_7c7c7c));
        }
    }

    @Override
    public void onTabReselected(Tab tab)
    {

    }
}
