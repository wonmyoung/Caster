package com.casting;

import com.casting.commonmodule.utility.EasyLog;
import com.casting.interfaces.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FutureCastingUtil implements Constants {

    public static boolean isPast(String s)
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT, Locale.KOREA);

            Date endDate = simpleDateFormat.parse(s);

            EasyLog.LogMessage("++ FutureCastingUtil isPast s = " + s);

            long endDateTime = endDate.getTime();
            long fromDateTime = (new Date()).getTime();

            long diffDateTime = endDateTime - fromDateTime;

            return !(diffDateTime > 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }


    public static String getTimeFormattedString(String s)
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT, Locale.KOREA);

            Date endDate = simpleDateFormat.parse(s);

            long endDateTime = endDate.getTime();
            long fromDateTime = (new Date()).getTime();

            long diffDateTime = Math.abs((endDateTime - fromDateTime));

            if (diffDateTime > 0)
            {
                StringBuilder stringBuilder = new StringBuilder();

                int day = (int) (diffDateTime / DAY);
                if (day > 0)
                {
                    stringBuilder.append(day).append("일 ");

                    return stringBuilder.toString();
                }

                diffDateTime %= DAY;

                int hour = (int) (diffDateTime / HOUR);
                if (hour >= 0)
                {
                    stringBuilder.append(String.format(Locale.KOREA, "%02d", hour)).append(" 시간");

                    return stringBuilder.toString();
                }

                diffDateTime %= HOUR;

                int minute = (int) (diffDateTime / MINUTE);
                if (minute >= 0)
                {
                    stringBuilder.append(String.format(Locale.KOREA, "%02d", minute)).append(" 분");

                    return stringBuilder.toString();
                }

                diffDateTime %= MINUTE;

                int second = (int) (diffDateTime / SECOND);
                if (second >= 0)
                {
                    stringBuilder.append(String.format(Locale.KOREA, "%02d", second)).append(" 초");
                }
                return stringBuilder.toString();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
