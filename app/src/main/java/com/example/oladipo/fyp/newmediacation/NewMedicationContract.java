package com.example.oladipo.fyp.newmediacation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.database.sqlite.SQLiteDatabase;

import com.example.oladipo.fyp.model.Medication;



public interface NewMedicationContract {

    interface Presenter  {
        void start();
        void addMedicationToDb(Medication medication , SQLiteDatabase db);
        void scheduleNotificationJob(AlarmManager alarmManager, Medication medication , PendingIntent alarmIntent);
        void updateMedication(Medication medication, SQLiteDatabase db);
    }

    interface View  {
        void onMedicationInsertedToDb(Medication medication);
        void onMedicationUpdated();
        void moveToNextStep();
    }
}
