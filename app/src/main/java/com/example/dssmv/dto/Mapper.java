package com.example.dssmv.dto;

import com.example.dssmv.model.AsteroidDate;
import com.example.dssmv.model.AsteroidInfo;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public static AsteroidInfo asteroidInfoDTO2AsteroidInfo(AsteroidInfoDTO AsteroidInfoDTO) throws NullPointerException {
        AsteroidInfo asteroid = null;
        asteroid = new AsteroidInfo(AsteroidInfoDTO.getName(), AsteroidInfoDTO.getDiameter(), AsteroidInfoDTO.isHazardous(), AsteroidInfoDTO.getLink());
        return asteroid;
    }

    public static AsteroidDate asteroidDateDTO2AsteroidDate(AsteroidDateDTO AsteroidDateDTO) throws NullPointerException {
        AsteroidDate asteroid = null;
        if (AsteroidDateDTO == null) {
            asteroid = new AsteroidDate();

        } else {
            List<AsteroidInfoDTO> asteroidInfoDTO = AsteroidDateDTO.getAsteroids();
            List<AsteroidInfo> asteroids = new ArrayList<AsteroidInfo>();
            for (AsteroidInfoDTO dayDTO : asteroidInfoDTO) {
                AsteroidInfo day = asteroidInfoDTO2AsteroidInfo(dayDTO);
                asteroids.add(day);
            }
            asteroid = new AsteroidDate(asteroids);
        }
        return asteroid;
    }

}

