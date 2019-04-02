package com.example.oladipo.fyp.model;

public class Diagnosis {
    private int accuracy;
    private String name;

    public Diagnosis(int accuracy, String name) {
        this.accuracy = accuracy;
        this.name = name;
    }


    public Diagnosis(String name) {
        this.name = name;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public String getName() {
        return name;
    }
}
