package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Intent;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.MyStrings;
import com.jamieholdstock.dcrwidgets.R;
import com.jamieholdstock.dcrwidgets.widget.TimeStamp;

public class ErrorHandler extends IntentHandler {
    @Override
    public void handle(Intent intent, RemoteViews views) {
        views.setTextViewText(R.id.text_btc_price, MyStrings.error);
        views.setTextViewText(R.id.text_usd_price, MyStrings.error);

        showRefresh(false, views);
        showError(views);
    }

    private void showError(RemoteViews views) {
        views.setTextViewText(R.id.update_status, MyStrings.offlineMsg);
    }


}
