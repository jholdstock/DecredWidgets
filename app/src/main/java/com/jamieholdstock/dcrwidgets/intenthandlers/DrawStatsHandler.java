package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.R;
import com.jamieholdstock.dcrwidgets.intents.IntentExtras;
import com.jamieholdstock.dcrwidgets.service.DcrStats;
import com.jamieholdstock.dcrwidgets.widget.TimeStamp;

public class DrawStatsHandler extends IntentHandler {

    public DrawStatsHandler(Intent intent, RemoteViews views) {
        super(intent, views);
    }

    @Override
    public void handle(Context context) {
        DcrStats stats = (DcrStats) intent.getExtras().get(IntentExtras.DCR_STATS);

        drawPriceStats(stats);
        drawStakeStats(stats);
        drawWorkStats(stats);

        views.setTextViewText(R.id.update_status, new TimeStamp().toString());
        showProgressBar(false);
    }

    private void drawPriceStats(DcrStats stats) {
        views.setTextViewText(R.id.text_usd_price, stats.getUsdPrice());
        views.setTextViewText(R.id.text_btc_price, stats.getBtcPrice());
    }

    private void drawStakeStats(DcrStats stats) {
        views.setTextViewText(R.id.text_ticket_price, stats.getTicketPrice());

        double ticketChange = stats.getPriceChangeInSeconds();

        views.setTextViewText(R.id.text_price_change, new ChangeTime(ticketChange).format());

        views.setTextViewText(R.id.text_est_new_price, stats.getEstNextPrice());
    }

    private void drawWorkStats(DcrStats stats) {
        views.setTextViewText(R.id.text_difficulty, stats.getDifficulty());

        double networkHash = stats.getNetworkHash();

        views.setTextViewText(R.id.text_network_hash,new HashRate(networkHash).format() + "h/s" );
    }
}
