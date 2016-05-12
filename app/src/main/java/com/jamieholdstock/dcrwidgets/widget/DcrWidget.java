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
import com.jamieholdstock.dcrwidgets.MyStrings;
import com.jamieholdstock.dcrwidgets.R;
import com.jamieholdstock.dcrwidgets.intenthandlers.ButtonPressedHandler;
import com.jamieholdstock.dcrwidgets.intenthandlers.ErrorHandler;
import com.jamieholdstock.dcrwidgets.intenthandlers.IntentHandler;
import com.jamieholdstock.dcrwidgets.intenthandlers.UpdateWidgetHandler;
import com.jamieholdstock.dcrwidgets.intents.IntentExtras;
import com.jamieholdstock.dcrwidgets.intents.MyIntents;
import com.jamieholdstock.dcrwidgets.service.DcrStats;
import com.jamieholdstock.dcrwidgets.service.DcrStatsService;

public class DcrWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyStrings.init(context);
        this.sendIntentToService(context);
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
            addClickIntent(R.id.root_layout, views, context);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    private void addClickIntent(int id, RemoteViews views, Context context) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(MyIntents.BUTTON_PRESSED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        views.setOnClickPendingIntent(id, pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!action.contains(MyIntents.INTENT_PREFIX)) {
            super.onReceive(context, intent);
            return;
        }

        L.l("Widget received " + action);

        IntentHandler handler = null;
        switch (action) {
            case MyIntents.BUTTON_PRESSED:
                this.sendIntentToService(context);
                handler = new ButtonPressedHandler();
            break;

            case MyIntents.UPDATE_WIDGET:
                handler = new UpdateWidgetHandler();
            break;

            case MyIntents.UPDATE_WIDGET_ERROR:
                L.l("widget received error '" + intent.getStringExtra(IntentExtras.ERROR_MESSAGE) + "' from service");
                handler = new ErrorHandler();
            break;
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);

        for (int i = 0; i < appWidgetIds.length; i++) {
                handler.handle(intent, views);
                appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    private void sendIntentToService(Context context) {
        Intent msgIntent = new Intent(context, DcrStatsService.class);
        msgIntent.setAction(MyIntents.GET_STATS);
        context.startService(msgIntent);
    }
}
