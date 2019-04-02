package com.example.oladipo.fyp.fragments;

import android.content.Context;

public class BaseContract {
    interface view{
        void showloading(String message);
        void showToast(String message);
        void hideLoading();
        boolean isConnectionAvailable(Context context);
        String getToken();
    }
}
