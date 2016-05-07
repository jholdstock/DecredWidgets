package com.jamieholdstock.dcrwidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.intents.IntentExtras;
import com.jamieholdstock.dcrwidgets.intents.MyIntents;

import java.util.Random;

public class DcrWidget extends AppWidgetProvider {

    private String currentMessage = "No data yet";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        L.l("Starting widget");
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
            views.setTextViewText(R.id.textView, currentMessage);

            Intent intent = new Intent(context, getClass());
            intent.setAction(MyIntents.WIDGET_CLICKED);
            PendingIntent pi =  PendingIntent.getBroadcast(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.textView, pi);

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(MyIntents.WIDGET_CLICKED))
        {
            L.l("widget clicked");
            Intent msgIntent = new Intent(context, DcrStatsService.class);
            msgIntent.setAction(MyIntents.GET_STATS);
            context.startService(msgIntent);
        }
        else if (action.equals(MyIntents.UPDATE_WIDGET)) {

            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, this.getClass()));
            currentMessage = intent.getStringExtra(IntentExtras.DCR_STATS);
            L.l("widget received '" + currentMessage + "' from service");
            this.onUpdate(context, gm, ids);
        }

        super.onReceive(context, intent);
    }
}
