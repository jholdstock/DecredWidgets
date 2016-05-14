package com.jamieholdstock.dcrwidgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class WidgetSettings extends AppCompatActivity {

    private static final String PREFS_NAME = "com.jamieholdstock.dcrwidgets.WidgetSettings";
    private static final String PREF_PREFIX_KEY = "widget_type";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public WidgetSettings() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);

        // Set the view layout resource to use.
        setContentView(R.layout.dcr_settings_layout);

        // Bind the action for the save button.
        findViewById(R.id.save_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetSettings.this;

            RadioButton saleRadio = (RadioButton)findViewById(R.id.salePriceBtn);

            String widgetType;
            if (saleRadio.isChecked()) {
                widgetType = "price";
            }
            else {
                widgetType = "stake";
            }

            saveWidgetType(context, mAppWidgetId, widgetType);

            // Push widget update to surface with newly set prefix
//          AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            DcrWidget.update(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public static void saveWidgetType(Context context, int appWidgetId, String widgetType) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, widgetType);
        prefs.commit();
    }

    public static String loadWidgetType(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return  prefs.getString(PREF_PREFIX_KEY + appWidgetId, "price");
    }
}
