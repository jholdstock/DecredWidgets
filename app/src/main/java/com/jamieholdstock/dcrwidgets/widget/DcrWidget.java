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

import static com.jamieholdstock.dcrwidgets.widget.WidgetStatus.*;

public class DcrWidget extends AppWidgetProvider {
    private static String usdPrice = "";
    private static String btcPrice = "";
    private static WidgetStatus status;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
            views.setTextViewText(R.id.text_btc_price, btcPrice);
            views.setTextViewText(R.id.text_usd_price, usdPrice);

            views.setTextViewText(R.id.text_update_time, new TimeStamp().toString());

            attachClickIntent(context, views, R.id.refreshButton);

            switch (status) {
                case DISPLAY_STATS:
                    views.setViewVisibility(R.id.refreshButton, View.GONE);
                    views.setViewVisibility(R.id.refreshButton_disabled, View.VISIBLE);
                    break;

                default:
                    views.setViewVisibility(R.id.refreshButton, View.VISIBLE);
                    views.setViewVisibility(R.id.refreshButton_disabled, View.GONE);
                    break;
            }

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    private void attachClickIntent(Context context, RemoteViews views, int id) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(MyIntents.BUTTON_PRESSED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        views.setOnClickPendingIntent(id, pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(MyIntents.BUTTON_PRESSED))
        {
            usdPrice = "...";
            btcPrice = "...";
            status = REFRESHING;
            Intent msgIntent = new Intent(context, DcrStatsService.class);
            msgIntent.setAction(MyIntents.GET_STATS);
            context.startService(msgIntent);
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, this.getClass()));
            this.onUpdate(context, gm, ids);
        }
        else if (action.equals(MyIntents.UPDATE_WIDGET)) {
            status = DISPLAY_STATS;
            DcrStats stats = (DcrStats) intent.getExtras().get(IntentExtras.DCR_STATS);

            double dUsdPrice = stats.getUsdPrice();
            usdPrice = String.format("%.2f", dUsdPrice);

            double dBtcPrice = stats.getBtcPrice();
            btcPrice = String.format("%.4f", dBtcPrice);

            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, this.getClass()));
            this.onUpdate(context, gm, ids);
        }
        else if (action.equals(MyIntents.UPDATE_WIDGET_ERROR)) {
            status = ERROR;
            btcPrice = "err";
            usdPrice = "err";

            L.l("widget received error '" + intent.getStringExtra(IntentExtras.ERROR_MESSAGE) + "' from service");

            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, this.getClass()));
            this.onUpdate(context, gm, ids);
        }

        super.onReceive(context, intent);
    }
}
