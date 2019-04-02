package com.example.oladipo.fyp.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.oladipo.fyp.database.MedicationDBHelper;
import com.example.oladipo.fyp.database.MedicationLoader;
import com.example.oladipo.fyp.model.Medication;
import com.example.oladipo.fyp.utility.Utils;

import java.util.ArrayList;

public class MedBootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("BOOTMEDMANAGERRECEIEVER", "onBoot recieved");
            MedicationDBHelper medicationDBHelper = new MedicationDBHelper(context);
            SQLiteDatabase db = medicationDBHelper.getReadableDatabase();
            ArrayList<Medication> activeMedications = MedicationLoader.getMedicationsList(db, MedicationLoader.ACTIVE_MEDICATION_SELECTION);
            for (Medication medication : activeMedications){
                long scheduleTime  = medication.nextRingTime;
                long currentTime = System.currentTimeMillis();
                while (scheduleTime < currentTime){
                    scheduleTime += medication.interval;
                }
                if (scheduleTime < medication.endTime && scheduleTime > currentTime){
                    Intent alarmIntent = new Intent(context, MedJobBroadcastReceiver.class);
                    intent.putExtra("medication_db_row_id" , medication.dbRowId);
                    Utils.scheduleAlarm(medication , context  , alarmIntent , scheduleTime);
                    Log.d("BOOTMEDMANAGERRECEIEVER", "Med Rescheduled for " + medication.name);
                }
            }
            db.close();
        }

    }
}
