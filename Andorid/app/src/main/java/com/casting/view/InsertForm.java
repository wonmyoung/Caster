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

import android.text.InputType;

public class InsertForm extends LinearLayout {

    private TextView    TitleView;
    private EditText    InsertView;

    private String      mTitle;
    private float       mTitleSize;
    private float       mTextSize;
    private int         mTextType;

    private TextView.OnEditorActionListener     mEditorActionListener;

    public InsertForm(Context context)
    {
        super(context);

        init(context, null, 0);
    }

    public InsertForm(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public InsertForm(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
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

        float defaultTitleSize = c.getResources().getDimension(R.dimen.dp7);
        float defaultTextSize = c.getResources().getDimension(R.dimen.dp18);
        mTitleSize = a.getDimension(R.styleable.InsertForm_titleSize, defaultTitleSize);
        mTextSize = a.getDimension(R.styleable.InsertForm_insertTextSize, defaultTextSize);

        mTextType = a.getInt(R.styleable.InsertForm_insertTextType, 0);

        a.recycle();

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        TitleView.setText(mTitle);

        InsertView.setOnEditorActionListener(mEditorActionListener);

        switch (mTextType)
        {
            case 0:
                InsertView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;

            case 1:
                InsertView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;

            case 2:
                InsertView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
                break;

            case 3:
                InsertView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                break;
        }
    }

    public TextView getTitleView()
    {
        return TitleView;
    }

    public EditText getInsertView()
    {
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

    public TextView.OnEditorActionListener getEditorActionListener()
    {
        return mEditorActionListener;
    }

    public void setEditorActionListener(TextView.OnEditorActionListener actionListener)
    {
        this.mEditorActionListener = actionListener;

        invalidate();
    }
}
