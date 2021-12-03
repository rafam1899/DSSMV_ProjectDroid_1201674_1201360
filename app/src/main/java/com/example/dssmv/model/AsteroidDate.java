package com.example.dssmv.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AsteroidDate {
    private String date;
    private List<AsteroidInfo> asteroids;

    public AsteroidDate(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date();
        String formatedDate = dateFormat.format(data);

        this.date = formatedDate;
        this.asteroids =  new ArrayList<AsteroidInfo>();
    }

    public AsteroidDate(List<AsteroidInfo> asteroids){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date();
        String formatedDate = dateFormat.format(data);

        this.date = formatedDate;
        this.asteroids =  asteroids;
    }

    public String getDate() {
        return date;
    }
    public List<AsteroidInfo> getAsteroids()
    {
        return asteroids;
    }
}
