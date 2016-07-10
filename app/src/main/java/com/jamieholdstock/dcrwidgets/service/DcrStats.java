package com.jamieholdstock.dcrwidgets.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.jamieholdstock.dcrwidgets.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class DcrStats implements Parcelable {
    private final String rawJson;
    private JSONObject jsonObject;
    public DcrStats(String rawJson) {
        this.rawJson = rawJson;
    }

    public String getUsdPrice() {
        double usd_price = getDouble("usd_price");
        double btc_last = getDouble("btc_last");
        double dUsdPrice = usd_price * btc_last;

        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(dUsdPrice);
    }

    public String getDifficulty() {
        DecimalFormat df = new DecimalFormat("###,###,###,###");
        return df.format(getDouble("difficulty"));
    }

    public double getNetworkHash() {
        return getDouble("networkhashps");
    }

    public String getTicketPrice() {
        DecimalFormat df = new DecimalFormat("#####0.00");
        return df.format(getDouble("sbits"));
    }

    public String getBtcPrice() {
        DecimalFormat df = new DecimalFormat("0.0000");
        return df.format(getDouble("btc_last"));
    }

    public String getEstNextPrice() {
        DecimalFormat df = new DecimalFormat("#####0.00");
        return df.format(getDouble("est_sbits"));
    }

    public double getPriceChangeInSeconds() {
        return (getBlocksToPriceChange() * getAverageBlockTime());
    }

    private double getAverageBlockTime() {
        return getDouble("average_time");
    }

    private double getBlocksToPriceChange() {
        double totalBlocks = getDouble("blocks");
        return 144 - (totalBlocks % 144);
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
