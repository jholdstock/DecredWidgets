package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.R;

abstract public class IntentHandler {

    protected final Intent intent;
    protected final RemoteViews views;

    public IntentHandler(Intent intent, RemoteViews views) {
        this.intent = intent;
        this.views = views;
    }

    abstract public void handle();

    protected void showProgressBar(boolean show) {
        if (show) {
            views.setViewVisibility(R.id.progressBar, View.VISIBLE);
        }
        else {
            views.setViewVisibility(R.id.progressBar, View.GONE);
        }
    }
}
