package com.jamieholdstock.dcrwidgets;

import android.app.IntentService;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.jamieholdstock.dcrwidgets.intents.IntentExtras;
import com.jamieholdstock.dcrwidgets.intents.MyIntents;

import java.util.Random;

public class DcrStatsService extends IntentService {
    public DcrStatsService() {
        super("DcrStatsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        L.l("service received " + action);
        if (action.equals(MyIntents.GET_STATS))
        {
            SystemClock.sleep(1000);

            Intent i = new Intent(this.getApplicationContext(), DcrWidget.class);
            i.setAction(MyIntents.UPDATE_WIDGET);
            String text = "Data : " + new Random().nextInt(1000);
            i.putExtra(IntentExtras.DCR_STATS, text);

            this.getApplicationContext().sendBroadcast(i);
        }
    }
}
