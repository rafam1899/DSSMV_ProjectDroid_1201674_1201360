package com.example.dssmv.model;

public class AsteroidInfo {

    private String name;
    private double diameter;
    private boolean hazardous;
    private String link;

    public AsteroidInfo() {
    }

    public AsteroidInfo(String name, double diameter, boolean hazardous, String link) {
        this.name = name;
        this.diameter = diameter;
        this.hazardous = hazardous;
        this.link = link;
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

    public String getLink() {
        return link;
    }
}
