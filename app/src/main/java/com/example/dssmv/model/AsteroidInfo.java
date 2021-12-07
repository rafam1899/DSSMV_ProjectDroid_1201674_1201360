package com.example.dssmv.model;

public class AsteroidInfo {

    private String name;
    private double diameter;
    private boolean hazardous;
    private String link;
    private double velocity;
    private double distance;

    public AsteroidInfo() {
    }

    public AsteroidInfo(String name, double diameter, boolean hazardous, String link, double velocity, double distance) {
        this.name = name;
        this.diameter = diameter;
        this.hazardous = hazardous;
        this.link = link;
        this.distance= distance;
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getDistance() {
        return distance;
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
