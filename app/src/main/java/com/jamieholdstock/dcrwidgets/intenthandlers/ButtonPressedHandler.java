package com.jamieholdstock.dcrwidgets.intenthandlers;

import android.content.Intent;
import android.widget.RemoteViews;

import com.jamieholdstock.dcrwidgets.MyStrings;
import com.jamieholdstock.dcrwidgets.R;

public class ButtonPressedHandler extends IntentHandler {

    public ButtonPressedHandler(Intent intent, RemoteViews views) {
        super(intent, views);
    }

    @Override
    public void handle() {
        views.setTextViewText(R.id.text_btc_price, MyStrings.dots);
        views.setTextViewText(R.id.text_usd_price, MyStrings.dots);

        views.setTextViewText(R.id.update_status, "");

        showProgressBar(true);
    }
}
