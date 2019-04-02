package com.example.oladipo.fyp.model;

public class Symptoms {
    int id;
    String name;

    public Symptoms(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Symptoms(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
