package com.example.dssmv.json;


import com.example.dssmv.dto.AsteroidDateDTO;
import com.example.dssmv.dto.AsteroidInfoDTO;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonHandler {

    public static AsteroidDateDTO deSerializeJson2DateDTO(String resp) {
        AsteroidDateDTO date = new AsteroidDateDTO();
        try {
            JSONObject mResponseObject = new JSONObject(resp);
            String cod = mResponseObject.getString("cod");
            if(cod.contains("200")) {
                date.setDate(mResponseObject.getJSONObject("Date").getString("name") + " - " + mResponseObject.getJSONObject("Date").getString("country"));
                JSONArray array = mResponseObject.getJSONArray("list");
                for (int i = 0; i < array.length(); i++) {
                    AsteroidInfoDTO day = new AsteroidInfoDTO();
                    JSONObject obj1 = array.getJSONObject(i);
                    day.setName(obj1.getString("dt_txt"));
                    day.setDiameter((int) obj1.getJSONObject("main").getDouble("diameter"));
                    //day.setHazardous((boolean) obj1.getJSONObject("main").getDouble("Potentially Hazardous"));
                    JSONArray array2 = obj1.getJSONArray("asteroid");
                    date.addAsteroid(day);
                }
                return date;
            }
            else{
                return null;
            }
        }
        catch (Exception ee){
            // if we have a problem, simply return null
            return null;
        }

    }

}
