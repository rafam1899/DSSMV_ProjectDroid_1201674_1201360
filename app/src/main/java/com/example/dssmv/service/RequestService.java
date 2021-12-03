package com.example.dssmv.service;

import com.example.dssmv.dto.Mapper;
import com.example.dssmv.dto.AsteroidDateDTO;
import com.example.dssmv.json.JsonHandler;
import com.example.dssmv.model.AsteroidDate;
import com.example.dssmv.network.HttpOperation;

public class RequestService {

    private static AsteroidDateDTO _getDateAsteroidInfo(String urlStr) {
        AsteroidDateDTO date = null;
        try {
            String jsonString = HttpOperation.get(urlStr);
            date = JsonHandler.deSerializeJson2DateDTO(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static AsteroidDate getDateAsteroidInfo(String urlStr) {
        AsteroidDateDTO asteroidDateDTO = _getDateAsteroidInfo(urlStr);
        AsteroidDate asteroidDate = Mapper.asteroidDateDTO2AsteroidDate(asteroidDateDTO);
        return asteroidDate;

    }
}