package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.R;

abstract public class IntentHandler {

    protected final Intent intent;
    protected final RemoteViews views;

    public IntentHandler(Intent intent, RemoteViews views) {
        this.intent = intent;
        this.views = views;
    }

    abstract public void handle(Context context);

    protected void showProgressBar(boolean show) {
        if (show) {
            views.setViewVisibility(R.id.progressBar, View.VISIBLE);
        }
        else {
            views.setViewVisibility(R.id.progressBar, View.GONE);
        }
    }

    protected void showInAllStats(String show) {
        int[] ids = {
                R.id.text_btc_price,
                R.id.text_usd_price,
                R.id.text_ticket_price,
                R.id.text_price_change,
                R.id.text_est_new_price,
                R.id.text_network_hash,
                R.id.text_difficulty,
                R.id.update_status
        };

        for (int id:ids) {
            views.setTextViewText(id, show);
        }
    }

}
