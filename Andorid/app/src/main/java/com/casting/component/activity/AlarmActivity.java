package com.casting.component.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.casting.R;
import com.casting.model.Alarm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmActivity extends BaseFCActivity implements Handler.Callback {

    private Handler     mRemainingTimeTickHandler;
    private TextView    mRemainingTimeView;
    private TextView    mBottomTextView;

    private Alarm       mAlarm;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_alarm);

        find(R.id.alarmPageBack).setOnClickListener(this);

        mRemainingTimeView = find(R.id.remainingTimeView);
        mBottomTextView = find(R.id.bottomText);

        Serializable s = getIntent().getSerializableExtra(DEFINE_ALARM);

        if (s != null && s instanceof Alarm)
        {
            mAlarm = (Alarm) s;

            if (updateTimeTick())
            {
                mBottomTextView.setVisibility(View.GONE);

                mRemainingTimeTickHandler = new Handler(getMainLooper(), this);
                mRemainingTimeTickHandler.sendEmptyMessageDelayed(0, 1000);
            }
            else
            {
                mBottomTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onClickEvent(View v)
    {
        super.onClickEvent(v);

        switch (v.getId())
        {
            case R.id.alarmPageBack:
            {
                finish();
                break;
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        if (updateTimeTick())
        {
            mBottomTextView.setVisibility(View.GONE);

            mRemainingTimeTickHandler.sendEmptyMessageDelayed(0, 1000);
        }
        else
        {
            mBottomTextView.setVisibility(View.VISIBLE);
        }

        return false;
    }

    private boolean updateTimeTick()
    {
        try
        {
            String endDateText = mAlarm.getEndDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT, Locale.KOREA);

            Date endDate = simpleDateFormat.parse(endDateText);

            long endDateTime = endDate.getTime();
            long fromDateTime = (new Date()).getTime();

            long diffDateTime = (endDateTime - fromDateTime);

            if (diffDateTime > 0)
            {
                StringBuilder stringBuilder = new StringBuilder();

                int day = (int) (diffDateTime / DAY);
                if (day > 0)
                {
                    stringBuilder.append(day).append("일 ");
                }

                diffDateTime %= DAY;

                int hour = (int) (diffDateTime / HOUR);
                if (hour >= 0)
                {
                    stringBuilder.append(String.format(Locale.KOREA, "%02d", hour)).append(":");
                }

                diffDateTime %= HOUR;

                int minute = (int) (diffDateTime / MINUTE);
                if (minute >= 0)
                {
                    stringBuilder.append(String.format(Locale.KOREA, "%02d", minute)).append(":");
                }

                diffDateTime %= MINUTE;

                int second = (int) (diffDateTime / SECOND);
                if (second >= 0)
                {
                    stringBuilder.append(String.format(Locale.KOREA, "%02d", second));
                }

                mRemainingTimeView.setText(stringBuilder);

                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
