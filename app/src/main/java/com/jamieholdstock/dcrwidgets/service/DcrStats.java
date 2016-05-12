package com.jamieholdstock.dcrwidgets.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.jamieholdstock.dcrwidgets.L;

import org.json.JSONException;
import org.json.JSONObject;

public class DcrStats implements Parcelable {
    private final String rawJson;
    private JSONObject jsonObject;
    public DcrStats(String rawJson) {
        this.rawJson = rawJson;
    }

    public double getUsdPrice() {
        double usd_price = getDouble("usd_price");
        double btc_last = getDouble("btc_last");
        return usd_price * btc_last;
    }

    public double getBtcPrice() {
        return getDouble("btc_last");
    }

    private double getDouble(String id) {
        try {
            return getJsonObject().getDouble(id);
        } catch (JSONException e) {
            L.l(e.getLocalizedMessage());
            throw new RuntimeException("JSON PARSE ERROR! Looking for ID '" + id + "'\nRaw Json: " + rawJson);
        }
    }

    private JSONObject getJsonObject() {
        if (jsonObject == null) {
            try {
                jsonObject = new JSONObject(rawJson);
            } catch (JSONException e) {
                throw new RuntimeException("JSON PARSE ERROR!\nRaw Json: " + rawJson);
            }
        }

        return jsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(rawJson);
    }

    public static final Parcelable.Creator<DcrStats> CREATOR = new Parcelable.Creator<DcrStats>() {
        public DcrStats createFromParcel(Parcel in) {
            return new DcrStats(in);
        }

        public DcrStats[] newArray(int size) {
            return new DcrStats[size];
        }
    };

    private DcrStats(Parcel in) {
        rawJson = in.readString();
    }
}
