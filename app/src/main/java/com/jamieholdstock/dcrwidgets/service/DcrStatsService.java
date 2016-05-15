package com.jamieholdstock.dcrwidgets.service;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jamieholdstock.dcrwidgets.widget.DcrWidget;
import com.jamieholdstock.dcrwidgets.L;
import com.jamieholdstock.dcrwidgets.intents.IntentExtras;
import com.jamieholdstock.dcrwidgets.intents.MyIntents;

public class DcrStatsService extends IntentService {
    //private final static String DCR_STATS_URL = "https://dcrstats.com";
    private final static String DCR_STATS_URL = "http://10.0.2.2:8090";

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

    public void sendErrorToWidget() {
        Intent i = new Intent(this.getApplicationContext(), DcrWidget.class);
        i.setAction(MyIntents.DRAW_ERROR);
        this.getApplicationContext().sendBroadcast(i);
    }

    public void sendStatsToWidget(DcrStats stats) {
        Intent i = new Intent(this.getApplicationContext(), DcrWidget.class);
        i.setAction(MyIntents.DRAW_STATS);
        i.putExtra(IntentExtras.DCR_STATS, stats);

        this.getApplicationContext().sendBroadcast(i);
    }

    public void getStats() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = DCR_STATS_URL + "/api/v1/get_stats";

        L.l("Service sending GET to " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    L.l("Service received non-error response\n" + response);
                    DcrStats stats = new DcrStats(response);
                    sendStatsToWidget(stats);
                }
            },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendErrorToWidget();
                L.l("Service received error response");
            }
        });
        queue.add(stringRequest);
    }
}
