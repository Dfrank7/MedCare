package com.example.oladipo.fyp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Medications implements Parcelable {

    public String name, days, interval;

    public Medications() {
    }

    public Medications(String name, String days, String interval) {
        this.name = name;
        this.days = days;
        this.interval = interval;
    }

    protected Medications(Parcel in) {
        name = in.readString();
        days = in.readString();
        interval = in.readString();
    }

    public static final Creator<Medications> CREATOR = new Creator<Medications>() {
        @Override
        public Medications createFromParcel(Parcel in) {
            return new Medications(in);
        }

        @Override
        public Medications[] newArray(int size) {
            return new Medications[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(days);
        dest.writeString(interval);
    }
}
