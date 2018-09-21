package com.casting.component.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.casting.FutureCastingUtil;
import com.casting.R;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.model.Alarm;
import com.casting.model.Cast;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmActivity extends BaseFCActivity implements Handler.Callback {

    private Handler     mRemainingTimeTickHandler;
    private TextView    mRemainingTimeView;
    private TextView    mAlarmPrefixView;
    private TextView    mBottomTextView;

    private View        mCastInfoLayout;
    private ImageView   mTopImageView;
    private TextView    mTopTagView1;
    private TextView    mTopTagView2;
    private TextView    mTopCastingNumberView;
    private TextView    mTopCastingStatusView;
    private TextView    mTopCastTitle;
    private Button      mBottomButton;

    private Alarm       mAlarm;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_alarm);

        find(R.id.alarmPageBack).setOnClickListener(this);

        mRemainingTimeView = find(R.id.remainingTimeView);
        mBottomTextView = find(R.id.bottomText);

        mCastInfoLayout = find(R.id.castInfoLayout);
        mAlarmPrefixView = find(R.id.alarmPrefix);
        mTopImageView = find(R.id.castImageBack);
        mTopTagView1 = find(R.id.castTag1);
        mTopTagView2 = find(R.id.castTag2);
        mTopCastingNumberView = find(R.id.castTopText1);
        mTopCastingStatusView = find(R.id.castTopText2);
        mTopCastTitle = find(R.id.castTopTitle);
        mBottomButton = find(R.id.bottomButton);
        mBottomButton.setOnClickListener(this);

        Serializable s = getIntent().getSerializableExtra(DEFINE_ALARM);

        if (s != null && s instanceof Alarm)
        {
            mAlarm = (Alarm) s;

            Cast cast = mAlarm.getTargetCast();

            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String getchaCap = decimalFormat.format(2500);
            String castingNumber = Integer.toString(cast.getCasterNum());
            String title = cast.getTitle();
            String[] tags = cast.getTags();
            String tag1 = (tags != null && tags.length > 0 ? tags[0] : null);
            String tag2 = (tags != null && tags.length > 1 ? tags[1] : null);
            String endDate = mAlarm.getEndDate();
            String totalReward = Integer.toString(cast.getTotalReward());
            String thumbnail = cast.getThumbnail(0);

            EasyLog.LogMessage(this, "++ init title = " + title);
            EasyLog.LogMessage(this, "++ init castingNumber = " + castingNumber);
            EasyLog.LogMessage(this, "++ init tag1 = " + tag1);
            EasyLog.LogMessage(this, "++ init tag2 = " + tag2);
            EasyLog.LogMessage(this, "++ init endDate = " + endDate);
            EasyLog.LogMessage(this, "++ init totalReward = " + totalReward);
            EasyLog.LogMessage(this, "++ init thumbnail = " + thumbnail);

            if (FutureCastingUtil.isPast(endDate))
            {
                mAlarmPrefixView.setVisibility(View.INVISIBLE);
                mBottomTextView.setVisibility(View.VISIBLE);
                mCastInfoLayout.setVisibility(View.VISIBLE);

                int radius = (int) getResources().getDimension(R.dimen.dp25);

                ImageLoader.loadRoundImage(this, mTopImageView, thumbnail, radius);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                SpannableString spannableString1 = new SpannableString(getchaCap);
                SpannableString spannableString2 = new SpannableString("cap");
                spannableString2.setSpan(new RelativeSizeSpan(0.2f), 0, 3, 0);
                spannableStringBuilder.append(spannableString1);
                spannableStringBuilder.append(" ");
                spannableStringBuilder.append(spannableString2);
                mRemainingTimeView.setText(spannableStringBuilder);

                mBottomButton.setText("자세히 보러가기");
            }
            else
            {
                updateTimeTick();

                mAlarmPrefixView.setVisibility(View.VISIBLE);
                mBottomTextView.setVisibility(View.GONE);
                mCastInfoLayout.setVisibility(View.GONE);

                mRemainingTimeTickHandler = new Handler(getMainLooper(), this);
                mRemainingTimeTickHandler.sendEmptyMessageDelayed(0, 1000);

                mBottomButton.setText("다른 캐스트 보러가기");
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

            case R.id.bottomButton:
            {
                String endDate = mAlarm.getEndDate();

                if (FutureCastingUtil.isPast(endDate))
                {
                    Intent intent = new Intent(this, CastingActivity.class);
                    intent.putExtra(DEFINE_CAST, mAlarm.getTargetCast());

                    startActivity(intent);
                }

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
