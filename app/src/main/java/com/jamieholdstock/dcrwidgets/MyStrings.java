package com.jamieholdstock.dcrwidgets;

import android.content.Context;

public class MyStrings {
    public static String error;
    public static String offlineMsg;
    public static String dots;

    public static void init(Context context) {
        offlineMsg = context.getString(R.string.offline);
        dots = context.getString(R.string.dots);
        error = context.getString(R.string.error);
    }
}
