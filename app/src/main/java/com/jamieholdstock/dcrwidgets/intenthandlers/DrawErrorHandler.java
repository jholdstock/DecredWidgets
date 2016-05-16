package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.R;

public class DrawErrorHandler extends IntentHandler {

    public DrawErrorHandler(Intent intent, RemoteViews views) {
        super(intent, views);
    }

    @Override
    public void handle(Context context) {
        // Price
        views.setTextViewText(R.id.text_btc_price, "");
        views.setTextViewText(R.id.text_usd_price, "");

        // Stake
        views.setTextViewText(R.id.text_ticket_price, "");
        views.setTextViewText(R.id.text_price_change, "");
        views.setTextViewText(R.id.text_est_new_price, "");

        // Work
        views.setTextViewText(R.id.text_difficulty, "");
        views.setTextViewText(R.id.text_network_hash, "");

        // All
        views.setTextViewText(R.id.update_status, context.getString(R.string.offline));
        showProgressBar(false);
    }
}
