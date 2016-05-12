package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.R;

abstract public class IntentHandler {
    abstract public void handle(Intent intent, RemoteViews views);

    protected void showRefresh(boolean show, RemoteViews views) {
        if (show) {
            views.setViewVisibility(R.id.refreshButton, View.VISIBLE);
        }
        else {
            views.setViewVisibility(R.id.refreshButton, View.GONE);
        }
    }
}
