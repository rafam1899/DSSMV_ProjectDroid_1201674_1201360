package com.example.dssmv.dto;

import com.example.dssmv.model.AsteroidInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AsteroidDateDTO {

    private String date;
    private List<AsteroidInfoDTO> asteroids;

    public AsteroidDateDTO(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date();
        String formatedDate = dateFormat.format(data);

        date = formatedDate;
        asteroids = new ArrayList<AsteroidInfoDTO>();
    }
    public void setDate(String date){
        this.date =  date;
    }
    public void addAsteroid(AsteroidInfoDTO asteroid) {
        this.asteroids.add(asteroid);
    }
    public String getDate() {
        return date;
    }

    public List<AsteroidInfoDTO> getAsteroids()
    {
        return asteroids;
    }

}

