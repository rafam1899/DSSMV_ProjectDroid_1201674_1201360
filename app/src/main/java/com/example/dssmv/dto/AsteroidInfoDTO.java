package com.example.dssmv.dto;

public class AsteroidInfoDTO {

    private String name;
    private double diameter;
    private boolean hazardous;

    public AsteroidInfoDTO() {

    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public boolean isHazardous() {
        return hazardous;
    }

    public void setHazardous(boolean hazardous) {
        this.hazardous = hazardous;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
