package com.jamieholdstock.dcrwidgets.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.jamieholdstock.dcrwidgets.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DcrStats implements Parcelable {

    private JSONObject jsonObject;
    private final String rawJson;

    private Double usd_price;
    private Double btc_last;
    private Double difficulty;
    private Double sbits;
    private Double est_sbits;

    private Long average_time;
    private Long networkhashps;
    private Long blocks;

    public DcrStats(String rawJson) {
        this.rawJson = rawJson;

        usd_price = getDouble("usd_price");
        btc_last = getDouble("btc_last");
        difficulty = getDouble("difficulty");
        sbits = getDouble("sbits");
        est_sbits = getDouble("est_sbits");

        average_time = getLong("average_time");
        networkhashps = getLong("networkhashps");
        blocks = getLong("blocks");
    }

    public String getUsdPrice() {
        Double dUsdPrice = usd_price * btc_last;

        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(dUsdPrice);
    }

    public String getDifficulty() {
        DecimalFormat df = new DecimalFormat("###,###,###,###");
        return df.format(difficulty);
    }

    public Long getNetworkHash() {
        return networkhashps;
    }

    public String getTicketPrice() {
        BigDecimal bd = new BigDecimal(sbits);
        BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_CEILING);
        return rounded.toPlainString();
    }

    public String getBtcPrice() {
        DecimalFormat df = new DecimalFormat("0.0000");
        return df.format(btc_last);
    }

    public String getEstNextPrice() {
        DecimalFormat df = new DecimalFormat("#####0.00");
        return df.format(est_sbits);
    }

    public Long getPriceChangeInSeconds() {
        return (getBlocksToPriceChange() * getAverageBlockTime());
    }

    private Long getAverageBlockTime() {
        return average_time;
    }

    private Long getBlocksToPriceChange() {
        return 144 - (blocks % 144);
    }

    private Long getLong(String id) {
        try {
            return getJsonObject().getLong(id);
        } catch (JSONException e) {
            L.l(e.getLocalizedMessage());
            throw new RuntimeException("JSON PARSE ERROR! Looking for ID '" + id + "'\nRaw Json: " + rawJson);
        }
    }

    private Double getDouble(String id) {
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
        this(in.readString());
    }
}
