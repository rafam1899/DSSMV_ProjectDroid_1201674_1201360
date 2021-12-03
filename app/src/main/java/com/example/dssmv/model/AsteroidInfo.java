package com.example.dssmv.model;

public class AsteroidInfo {

    private String name;
    private double diameter;
    private boolean hazardous;

    public AsteroidInfo() {
    }

    public AsteroidInfo(String name, double diameter, boolean hazardous) {
        this.name = name;
        this.diameter = diameter;
        this.hazardous = hazardous;
    }
    public String getName() {
        return name;
    }
    public double getDiameter() {
        return diameter;
    }
    public boolean getHazardous(){
        return hazardous;
    }

}
