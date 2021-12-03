package com.example.dssmv;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dssmv.adapter.ListViewAdapterAsteroidInfo;
import com.example.dssmv.helper.Utils;
import com.example.dssmv.model.AsteroidDate;
import com.example.dssmv.service.RequestService;

public class ListAsteroid extends AppCompatActivity {

    AsteroidDate asteroidDate = null;
    ListView asteroid_list;
    ListViewAdapterAsteroidInfo adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        asteroidDate = new AsteroidDate();
        adapter = new ListViewAdapterAsteroidInfo(getApplicationContext(),R.layout.activity_list,asteroidDate.getAsteroids());
        asteroid_list.setAdapter(adapter);

        getCityWeatherConditions(Utils.URL_SERVICE_PREFIX + "2021-12-03" + Utils.URL_SERVICE_MIDDLE + "2021-12-03" + Utils.URL_SERVICE_SUFFIX);
    }

    private void getCityWeatherConditions(String urlStr){

        new Thread() {
            public void run() {
                try {
                    asteroidDate = RequestService.getDateAsteroidInfo(urlStr);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(asteroidDate != null) {
                                adapter.setItems(asteroidDate.getAsteroids());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}