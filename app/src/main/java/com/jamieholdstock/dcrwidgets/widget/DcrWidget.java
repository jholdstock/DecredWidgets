package com.jamieholdstock.dcrwidgets.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.R;
import com.jamieholdstock.dcrwidgets.activity.WidgetSettings;
import com.jamieholdstock.dcrwidgets.intenthandlers.ButtonPressedHandler;
import com.jamieholdstock.dcrwidgets.intenthandlers.DrawErrorHandler;
import com.jamieholdstock.dcrwidgets.intenthandlers.DrawStatsHandler;
import com.jamieholdstock.dcrwidgets.intents.MyIntents;
import com.jamieholdstock.dcrwidgets.service.DcrStatsService;

public class DcrWidget extends AppWidgetProvider {

    private static boolean waitingForService = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!action.contains(MyIntents.INTENT_PREFIX)) {
            super.onReceive(context, intent);
            return;
        }

        RemoteViews views = getViews(context);
        switch (action) {
            case MyIntents.BUTTON_PRESSED:
                this.sendIntentToService(context);
                new ButtonPressedHandler(intent, views).handle(context);
                break;

            case MyIntents.DRAW_STATS:
                waitingForService = false;
                new DrawStatsHandler(intent, views).handle(context);
                break;

            case MyIntents.DRAW_ERROR:
                waitingForService = false;
                new DrawErrorHandler(intent, views).handle(context);
                break;
        }

        pushUpdatedWidget(views, context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] widgetIds) {
        this.sendIntentToService(context);
        RemoteViews views = getViews(context);
        pushUpdatedWidget(views, context);
    }

    private RemoteViews getViews(Context context) {
        return new RemoteViews(context.getPackageName(), R.layout.dcr_widget_layout);
    }

    private void pushUpdatedWidget(RemoteViews views, Context context) {
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        attachClickListener(context, views);
        int[] widgetIds = awm.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < widgetIds.length; i++) {
            WidgetType widgetType = WidgetSettings.loadWidgetType(context, widgetIds[i]);

            views.setViewVisibility(R.id.price_panel, View.GONE);
            views.setViewVisibility(R.id.stake_panel, View.GONE);
            views.setViewVisibility(R.id.work_panel, View.GONE);

            switch(widgetType) {
                case ALL:
                    views.setViewVisibility(R.id.price_panel, View.VISIBLE);
                    views.setViewVisibility(R.id.stake_panel, View.VISIBLE);
                    views.setViewVisibility(R.id.work_panel, View.VISIBLE);
                    break;

                case PRICE:
                    views.setViewVisibility(R.id.price_panel, View.VISIBLE);
                    break;

                case STAKE:
                    views.setViewVisibility(R.id.stake_panel, View.VISIBLE);
                    break;

                case WORK:
                    views.setViewVisibility(R.id.work_panel, View.VISIBLE);
                    break;
            }

            awm.updateAppWidget(widgetIds[i], views);
        }
    }

    private void attachClickListener(Context context, RemoteViews views) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(MyIntents.BUTTON_PRESSED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.root_layout, pi);
    }

    private void sendIntentToService(Context context) {
        if (waitingForService) return;
        Intent msgIntent = new Intent(context, DcrStatsService.class);
        msgIntent.setAction(MyIntents.GET_STATS);
        context.startService(msgIntent);
        waitingForService = true;
    }
}
