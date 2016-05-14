package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.R;

public class ButtonPressedHandler extends IntentHandler {

    public ButtonPressedHandler(Intent intent, RemoteViews views) {
        super(intent, views);
    }

    @Override
    public void handle(Context context) {
        views.setTextViewText(R.id.text_btc_price, context.getString(R.string.dots));
        views.setTextViewText(R.id.text_usd_price, context.getString(R.string.dots));

        views.setTextViewText(R.id.text_ticket_price, context.getString(R.string.dots));
        views.setTextViewText(R.id.text_price_change, context.getString(R.string.dots));
        views.setTextViewText(R.id.text_est_new_price, context.getString(R.string.dots));

        views.setTextViewText(R.id.update_status, "");

        showProgressBar(true);
    }
}
