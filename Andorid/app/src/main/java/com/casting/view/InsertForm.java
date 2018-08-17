package com.casting.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.casting.R;

public class InsertForm extends LinearLayout {

    private TextView    TitleView;
    private EditText    InsertView;

    private String      mTitle;

    public InsertForm(Context context) {
        super(context);

        init(context, null, 0);
    }

    public InsertForm(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public InsertForm(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context c, AttributeSet attrs, int defStyle)
    {
        LayoutInflater.from(c).inflate(R.layout.layout_insert_form, this);

        TitleView = find(R.id.title_view);
        InsertView = find(R.id.insert_form);

        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.InsertForm, defStyle, 0);

        mTitle = a.getString(R.styleable.InsertForm_title);

        a.recycle();

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        TitleView.setText(mTitle);
    }

    public TextView getTitleView() {
        return TitleView;
    }

    public EditText getInsertView() {
        return InsertView;
    }

    public String getInsertedText()
    {
        return (InsertView != null ? InsertView.getText().toString() : null);
    }

    @SuppressWarnings("unchecked")
    protected <V extends View> V find(int id)
    {
        return (V) findViewById(id);
    }
}
