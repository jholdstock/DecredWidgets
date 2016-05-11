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

    private static String offlineMsg;
    private static String error;
    private static String dots;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        offlineMsg = context.getString(R.string.offline);
        dots = context.getString(R.string.dots);
        error = context.getString(R.string.error);
        this.sendIntentToService(context);
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
            addClickIntent(R.id.root_layout, views, context);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    private void draw_buttonPressed(RemoteViews views) {
        views.setTextViewText(R.id.text_btc_price, dots);
        views.setTextViewText(R.id.text_usd_price, dots);

        showRefresh(true, views);
    }

    private void draw_stats(DcrStats stats, RemoteViews views) {
        double dUsdPrice = stats.getUsdPrice();
        String usdPrice = String.format("%.2f", dUsdPrice);

        double dBtcPrice = stats.getBtcPrice();
        String btcPrice = String.format("%.4f", dBtcPrice);

        views.setTextViewText(R.id.text_btc_price, btcPrice);
        views.setTextViewText(R.id.text_usd_price, usdPrice);

        showRefresh(false, views);
        showTimestamp(views);
    }

    private void draw_error(RemoteViews views) {
        views.setTextViewText(R.id.text_btc_price, error);
        views.setTextViewText(R.id.text_usd_price, error);

        showRefresh(false, views);
        showError(views);
    }

    private void showError(RemoteViews views) {
        views.setTextViewText(R.id.update_status, offlineMsg);
    }

    private void showTimestamp(RemoteViews views) {
        views.setTextViewText(R.id.update_status, new TimeStamp().toString());
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
        if (!action.contains(MyIntents.INTENT_PREFIX)) {
            super.onReceive(context, intent);
            return;
        }

        L.l("Widget received " + action);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()));
        if (action.equals(MyIntents.BUTTON_PRESSED))
        {
            this.sendIntentToService(context);
            for (int i = 0; i < appWidgetIds.length; i++) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
                this.draw_buttonPressed(views);
                appWidgetManager.updateAppWidget(appWidgetIds[i], views);
            }
        }
        else if (action.equals(MyIntents.UPDATE_WIDGET)) {
            DcrStats stats = (DcrStats) intent.getExtras().get(IntentExtras.DCR_STATS);
            for (int i = 0; i < appWidgetIds.length; i++) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
                this.draw_stats(stats, views);
                appWidgetManager.updateAppWidget(appWidgetIds[i], views);
            }
        }
        else if (action.equals(MyIntents.UPDATE_WIDGET_ERROR)) {
            L.l("widget received error '" + intent.getStringExtra(IntentExtras.ERROR_MESSAGE) + "' from service");
            for (int i = 0; i < appWidgetIds.length; i++) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
                this.draw_error(views);
                appWidgetManager.updateAppWidget(appWidgetIds[i], views);
            }
        }
    }

    private void sendIntentToService(Context context) {
        Intent msgIntent = new Intent(context, DcrStatsService.class);
        msgIntent.setAction(MyIntents.GET_STATS);
        context.startService(msgIntent);
    }
}
