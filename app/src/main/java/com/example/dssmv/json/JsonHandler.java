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
            String cod = mResponseObject.getString("element_count");

            if(!cod.isEmpty()) {

                JSONObject object = mResponseObject.getJSONObject("near_earth_objects");
                JSONArray array2 = object.getJSONArray(date.getDate());

                for (int i = 0; i < array2.length(); i++) {
                    AsteroidInfoDTO asteroid = new AsteroidInfoDTO();
                    JSONObject obj1 = array2.getJSONObject(i);
                    asteroid.setName(obj1.getString("name"));
                    asteroid.setDiameter(Math.round(obj1.getJSONObject("estimated_diameter").getJSONObject("kilometers").getDouble("estimated_diameter_max")*100.0)/100.0);
                    asteroid.setHazardous(obj1.getBoolean("is_potentially_hazardous_asteroid"));
                    asteroid.setLink(obj1.getJSONObject("links").getString("self"));
                    asteroid.setVelocity(Math.round(obj1.getJSONArray("close_approach_data").getJSONObject(0).getJSONObject("relative_velocity").getDouble("kilometers_per_hour")*100.0)/100.0);
                    asteroid.setDistance(Math.round(obj1.getJSONArray("close_approach_data").getJSONObject(0).getJSONObject("miss_distance").getDouble("kilometers")*100.0)/100.0);
                    //JSONArray array3 = obj1.getJSONArray("asteroid");
                    date.addAsteroid(asteroid);
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
