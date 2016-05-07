package com.jamieholdstock.dcrwidgets.service;

import android.app.IntentService;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jamieholdstock.dcrwidgets.DcrWidget;
import com.jamieholdstock.dcrwidgets.L;
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
        if (action.equals(MyIntents.GET_STATS))
        {
            getStats();
        }
    }

    public void sendErrorToWidget(String message) {
        Intent i = new Intent(this.getApplicationContext(), DcrWidget.class);
        i.setAction(MyIntents.UPDATE_WIDGET_ERROR);
        i.putExtra(IntentExtras.ERROR_MESSAGE, message);

        this.getApplicationContext().sendBroadcast(i);
    }

    public void sendStatsToWidget(DcrStats stats) {
        Intent i = new Intent(this.getApplicationContext(), DcrWidget.class);
        i.setAction(MyIntents.UPDATE_WIDGET);
        i.putExtra(IntentExtras.DCR_STATS, stats);

        this.getApplicationContext().sendBroadcast(i);
    }
    public void getStats() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://dcrstats.com/api/v1/get_stats";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DcrStats stats = new DcrStats(response);
                        sendStatsToWidget(stats);
                        L.l("received from dcrstats.com");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendErrorToWidget(error.getLocalizedMessage());
                L.l(error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);
    }
}
