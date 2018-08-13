package com.casting.commonmodule.utility;

import android.util.Log;

public class EasyLog {

    public static final String LOG_TAG = "futureCasting";

    public static void LogMessage(String message) {
        Log.d(LOG_TAG , message);
    }

    public static void ErrorMessage(Exception e , String ... messages) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(e.getMessage());
        for (String message : messages) {
            errorMessage.append(message);
        }
        Log.e(LOG_TAG , errorMessage.toString());
    }
}
