package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Intent;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.MyStrings;
import com.jamieholdstock.dcrwidgets.R;

public class DrawErrorHandler extends IntentHandler {

    public DrawErrorHandler(Intent intent, RemoteViews views) {
        super(intent, views);
    }

    @Override
    public void handle() {
        views.setTextViewText(R.id.text_btc_price, "");
        views.setTextViewText(R.id.text_usd_price, "");

        views.setTextViewText(R.id.text_ticket_price, "");
        views.setTextViewText(R.id.text_price_change, "");

        views.setTextViewText(R.id.update_status, MyStrings.offlineMsg);

        showProgressBar(false);
    }
}
