package com.casting.view.insert;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.view.ObserverView;
import com.casting.view.insert.items.ItemBooleanOption;

import java.util.Observable;

public class InsertOptionsBoolean extends LinearLayout implements View.OnClickListener {

    private static final String YES = "YES";
    private static final String NO = "NO";

    private TextView    mSelectYes;
    private TextView    mMiddlePrefixView;
    private TextView    mSelectNo;

    private ItemBooleanOption SelectedBoolean;

    public InsertOptionsBoolean(Context context) {
        super(context);

        init(context, null);
    }

    public InsertOptionsBoolean(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public InsertOptionsBoolean(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context c, @Nullable AttributeSet attrs)
    {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        Resources res = getResources();

        mSelectYes = new TextView(c);
        mSelectYes.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        mSelectYes.setText(YES);
        mSelectYes.setTag(true);
        mSelectYes.setTextSize(40);
        mSelectYes.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mSelectYes.setTextColor(UtilityUI.getColor(c, R.color.color_999999));
        mSelectYes.setOnClickListener(this);
        addView(mSelectYes);

        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins((int) res.getDimension(R.dimen.dp30), (int) res.getDimension(R.dimen.dp5),
                      (int) res.getDimension(R.dimen.dp30), 0);

        mMiddlePrefixView = new TextView(c);
        mMiddlePrefixView.setLayoutParams(lp);
        mMiddlePrefixView.setTextColor(UtilityUI.getColor(c, R.color.color_999999));
        mMiddlePrefixView.setTextSize(18);
        mMiddlePrefixView.setText("혹은");
        mMiddlePrefixView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        addView(mMiddlePrefixView);

        mSelectNo = new TextView(c);
        mSelectNo.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        mSelectNo.setText(NO);
        mSelectNo.setTag(false);
        mSelectNo.setTextSize(40);
        mSelectNo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mSelectNo.setTextColor(UtilityUI.getColor(c, R.color.color_999999));
        mSelectNo.setOnClickListener(this);
        addView(mSelectNo);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (SelectedBoolean != null &&
            SelectedBoolean.getInsertedData() != null)
        {
            Context c = getContext();

            mSelectYes.setTextColor((SelectedBoolean.getInsertedData() ?
                    UtilityUI.getColor(c, R.color.mainColor0) :
                    UtilityUI.getColor(c, R.color.color_999999)));

            mSelectNo.setTextColor((SelectedBoolean.getInsertedData() ?
                    UtilityUI.getColor(c, R.color.color_999999) :
                    UtilityUI.getColor(c, R.color.mainColor0)));

            setWillNotDraw(true);
        }
    }

    @Override
    public void onClick(View v)
    {
        boolean b = (boolean) v.getTag();

        if (SelectedBoolean != null)
        {
            SelectedBoolean.setInsertedData(b);
        }
    }

    public ItemBooleanOption getSelectedBoolean() {
        return SelectedBoolean;
    }

    public void setSelectedBoolean(ItemBooleanOption selectedBoolean)
    {

        if (selectedBoolean != null)
        {
            SelectedBoolean = selectedBoolean;

            ObserverView<TextView> observerView1 = new ObserverView<TextView>(mSelectYes)
            {
                @Override
                protected void updateView(Observable observable, Object o) throws Exception
                {
                    Context c = getContext();

                    mRoot.setTextColor((SelectedBoolean.getInsertedData() ?
                            UtilityUI.getColor(c, R.color.mainColor0) :
                            UtilityUI.getColor(c, R.color.color_999999)));
                }
            };
            SelectedBoolean.addObserver(observerView1);

            ObserverView<TextView> observerView2 = new ObserverView<TextView>(mSelectNo)
            {
                @Override
                protected void updateView(Observable observable, Object o) throws Exception
                {
                    Context c = getContext();

                    mRoot.setTextColor((SelectedBoolean.getInsertedData() ?
                            UtilityUI.getColor(c, R.color.color_999999) :
                            UtilityUI.getColor(c, R.color.mainColor0)));
                }
            };
            SelectedBoolean.addObserver(observerView2);

            setWillNotDraw(false);

            invalidate();
        }
    }
}
