package com.example.oladipo.fyp.utility;

import android.util.Log;

import com.example.oladipo.fyp.model.Interval;

import java.util.ArrayList;

public class LocalData {
    private static final String TAG = LocalData.class.getSimpleName();
    public static ArrayList<Interval> intervalArrayList;

    static {
        intervalArrayList = new ArrayList<>();
        Log.d(TAG , "The interval is " + (0.5/60.0f));
        intervalArrayList.add(new Interval("Every one 30 seconds", (0.5/60.0f)));
        for (double i = 0.5 ; i <= 24 ; i += 0.5 ){
            intervalArrayList.add(new Interval("Every "  + i + " hours ", i ));
        }
    }
}
