package com.example.oladipo.fyp.newmediacation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.database.sqlite.SQLiteDatabase;

import com.example.oladipo.fyp.database.MedicationLoader;
import com.example.oladipo.fyp.model.Medication;
import com.example.oladipo.fyp.utility.Utils;


public class NewMedicationPresenter implements NewMedicationContract.Presenter {

    private NewMedicationContract.View view;
    private static final String TAG = NewMedicationPresenter.class.getSimpleName();

    NewMedicationPresenter(NewMedicationContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
    }


    @Override
    public void addMedicationToDb(Medication medication, SQLiteDatabase db) {
        // Gets the data repository in write mode
        medication.dbRowId = MedicationLoader.addMedication(medication, db);
        view.onMedicationInsertedToDb(medication);
    }

    @Override
    public void scheduleNotificationJob(AlarmManager alarmManager, Medication medication, PendingIntent alarmIntent) {
        if (Utils.isKitKatAndAbove()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, medication.startTime, alarmIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, medication.startTime, alarmIntent);
        }
    }

    @Override
    public void updateMedication(Medication medication, SQLiteDatabase db) {
        MedicationLoader.updateMedication(medication, db);
        view.onMedicationUpdated();
    }
}
