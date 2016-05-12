package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Intent;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.R;
import com.jamieholdstock.dcrwidgets.intents.IntentExtras;
import com.jamieholdstock.dcrwidgets.service.DcrStats;
import com.jamieholdstock.dcrwidgets.widget.TimeStamp;

public class UpdateWidgetHandler extends IntentHandler {
    @Override
    public void handle(Intent intent, RemoteViews views) {
        DcrStats stats = (DcrStats) intent.getExtras().get(IntentExtras.DCR_STATS);
        double dUsdPrice = stats.getUsdPrice();
        String usdPrice = String.format("%.2f", dUsdPrice);

        double dBtcPrice = stats.getBtcPrice();
        String btcPrice = String.format("%.4f", dBtcPrice);

        views.setTextViewText(R.id.text_btc_price, btcPrice);
        views.setTextViewText(R.id.text_usd_price, usdPrice);

        showRefresh(false, views);
        showTimestamp(views);
    }
    private void showTimestamp(RemoteViews views) {
        views.setTextViewText(R.id.update_status, new TimeStamp().toString());
    }

}
