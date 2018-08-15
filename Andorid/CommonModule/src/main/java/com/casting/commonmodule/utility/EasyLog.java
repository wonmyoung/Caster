package com.casting.commonmodule.utility;

import android.util.Log;

public class EasyLog {

    private static final String LOG_TAG = "futureCasting";

    public static void LogMessage(String ... message) {

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : message)
        {
            stringBuilder.append(s);
        }

        Log.d(LOG_TAG , stringBuilder.toString());
    }

    public static void ErrorMessage(Exception e , String ... messages) {

        StringBuilder errorMessage = new StringBuilder();

        errorMessage.append(e.getMessage());

        for (String s : messages)
        {
            errorMessage.append(s);
        }

        Log.e(LOG_TAG , errorMessage.toString());
    }
}
