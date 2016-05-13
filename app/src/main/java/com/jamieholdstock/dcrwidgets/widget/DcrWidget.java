package com.jamieholdstock.dcrwidgets.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.MyStrings;
import com.jamieholdstock.dcrwidgets.R;
import com.jamieholdstock.dcrwidgets.intenthandlers.ButtonPressedHandler;
import com.jamieholdstock.dcrwidgets.intenthandlers.DrawErrorHandler;
import com.jamieholdstock.dcrwidgets.intenthandlers.IntentHandler;
import com.jamieholdstock.dcrwidgets.intenthandlers.DrawStatsHandler;
import com.jamieholdstock.dcrwidgets.intents.MyIntents;
import com.jamieholdstock.dcrwidgets.service.DcrStatsService;

public class DcrWidget extends AppWidgetProvider {

    private static boolean waitingForService = false;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyStrings.init(context);
        this.sendIntentToService(context);
        attachClickListeners(context, appWidgetManager, appWidgetIds);
    }

    private void attachClickListeners(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent intent = new Intent(context, getClass());
            intent.setAction(MyIntents.BUTTON_PRESSED);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.root_layout, pi);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!action.contains(MyIntents.INTENT_PREFIX)) {
            super.onReceive(context, intent);
            return;
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
        IntentHandler handler = null;
        switch (action) {
            case MyIntents.BUTTON_PRESSED:
                this.sendIntentToService(context);
                handler = new ButtonPressedHandler(intent, views);
                break;

            case MyIntents.DRAW_STATS:
                waitingForService = false;
                handler = new DrawStatsHandler(intent, views);
                break;

            case MyIntents.DRAW_ERROR:
                waitingForService = false;
                handler = new DrawErrorHandler(intent, views);
                break;
        }

        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        int[] ids = awm.getAppWidgetIds(new ComponentName(context, getClass()));

        for (int i = 0; i < ids.length; i++) {
            handler.handle();
            awm.updateAppWidget(ids[i], views);
        }
    }

    private void sendIntentToService(Context context) {
        if (waitingForService) return;
        Intent msgIntent = new Intent(context, DcrStatsService.class);
        msgIntent.setAction(MyIntents.GET_STATS);
        context.startService(msgIntent);
        waitingForService = true;
    }
}
