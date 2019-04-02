package com.example.oladipo.fyp.activemedication;

public class ActiveMedicationPresenter implements ActiveMedicationContract.Presenter{

    private ActiveMedicationContract.View view;


    ActiveMedicationPresenter(ActiveMedicationContract.View view) {
        this.view = view;
    }
    @Override
    public void start() {}

    @Override
    public void addMedication(String args) {}
}
