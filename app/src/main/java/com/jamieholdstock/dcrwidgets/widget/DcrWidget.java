package com.jamieholdstock.dcrwidgets.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.L;
import com.jamieholdstock.dcrwidgets.R;
import com.jamieholdstock.dcrwidgets.intents.IntentExtras;
import com.jamieholdstock.dcrwidgets.intents.MyIntents;
import com.jamieholdstock.dcrwidgets.service.DcrStats;
import com.jamieholdstock.dcrwidgets.service.DcrStatsService;

public class DcrWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
            addClickIntent(R.id.root_layout, views, context);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    private void draw_buttonPressed(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
            views.setTextViewText(R.id.text_btc_price, "...");
            views.setTextViewText(R.id.text_usd_price, "...");

            showRefresh(true, views);

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    private void draw_stats(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, DcrStats stats) {
        double dUsdPrice = stats.getUsdPrice();
        String usdPrice = String.format("%.2f", dUsdPrice);

        double dBtcPrice = stats.getBtcPrice();
        String btcPrice = String.format("%.4f", dBtcPrice);
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
            views.setTextViewText(R.id.text_btc_price, btcPrice);
            views.setTextViewText(R.id.text_usd_price, usdPrice);

            showRefresh(false, views);
            views.setTextViewText(R.id.text_update_time, new TimeStamp().toString());
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    private void draw_error(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
            views.setTextViewText(R.id.text_btc_price, "err");
            views.setTextViewText(R.id.text_usd_price, "err");

            showRefresh(false, views);
            views.setTextViewText(R.id.text_update_time, new TimeStamp().toString());
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    private void addClickIntent(int id, RemoteViews views, Context context) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(MyIntents.BUTTON_PRESSED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        views.setOnClickPendingIntent(id, pi);
    }

    private void showRefresh(boolean show, RemoteViews views) {
        if (show) {
            views.setViewVisibility(R.id.refreshButton, View.VISIBLE);
        }
        else {
            views.setViewVisibility(R.id.refreshButton, View.GONE);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        L.l("Widget received " + action);
        if (action.equals(MyIntents.BUTTON_PRESSED))
        {
            Intent msgIntent = new Intent(context, DcrStatsService.class);
            msgIntent.setAction(MyIntents.GET_STATS);
            context.startService(msgIntent);
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, this.getClass()));
            this.draw_buttonPressed(context, gm, ids);
        }
        else if (action.equals(MyIntents.UPDATE_WIDGET)) {
            DcrStats stats = (DcrStats) intent.getExtras().get(IntentExtras.DCR_STATS);

            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, this.getClass()));
            this.draw_stats(context, gm, ids, stats);
        }
        else if (action.equals(MyIntents.UPDATE_WIDGET_ERROR)) {
            L.l("widget received error '" + intent.getStringExtra(IntentExtras.ERROR_MESSAGE) + "' from service");

            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, this.getClass()));
            this.draw_error(context, gm, ids);
        }

        super.onReceive(context, intent);
    }
}
