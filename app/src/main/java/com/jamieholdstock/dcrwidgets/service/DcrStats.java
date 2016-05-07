package com.jamieholdstock.dcrwidgets.service;

import android.os.Parcel;
import android.os.Parcelable;

public class DcrStats implements Parcelable {
    private String rawJson;

    public DcrStats(String rawJson) {
        this.rawJson = rawJson;
    }

    public String getRawJson() {
        return rawJson;
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
