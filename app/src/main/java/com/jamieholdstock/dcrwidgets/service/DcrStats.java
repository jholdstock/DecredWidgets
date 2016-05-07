package com.jamieholdstock.dcrwidgets.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.jamieholdstock.dcrwidgets.L;

import org.json.JSONException;
import org.json.JSONObject;

public class DcrStats implements Parcelable {
    private final String rawJson;

    public DcrStats(String rawJson) {
        this.rawJson = rawJson;
    }

    public double getUsdPrice() {
        try {
            JSONObject json = new JSONObject(rawJson);
            return json.getDouble("usd_price");
        } catch (JSONException e) {
            L.l(e.getLocalizedMessage());
        }

        return 0;
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
