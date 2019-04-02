package com.example.oladipo.fyp.activemedication;

public class ActiveMedicationContract {

    interface Presenter  {
        void start();
        void addMedication(String args);
    }

    interface View  {
        void moveToNextStep();
    }
}
