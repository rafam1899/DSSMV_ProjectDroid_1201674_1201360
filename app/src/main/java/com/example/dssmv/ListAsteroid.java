package com.example.dssmv;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dssmv.adapter.ListViewAdapterAsteroidInfo;
import com.example.dssmv.helper.Utils;
import com.example.dssmv.model.AsteroidDate;
import com.example.dssmv.model.AsteroidInfo;
import com.example.dssmv.service.RequestService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListAsteroid extends AppCompatActivity {

    AsteroidDate asteroidDate = null;
    ListView asteroid_list;
    ListViewAdapterAsteroidInfo adapter;
    TextView nAsteroid;
    ImageView qrcodeImage;
    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        asteroid_list = (ListView) findViewById(R.id.weather_list);
        nAsteroid = (TextView) findViewById(R.id.nAsteroids);


        asteroidDate = new AsteroidDate();
        adapter = new ListViewAdapterAsteroidInfo(getApplicationContext(),R.layout.list_asteroid,asteroidDate.getAsteroids());
        asteroid_list.setAdapter(adapter);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date();
        String formatedDate = dateFormat.format(data);

        getAsteroidsInfo(Utils.URL_SERVICE_PREFIX + formatedDate + Utils.URL_SERVICE_MIDDLE + formatedDate + Utils.URL_SERVICE_SUFFIX);


        asteroid_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListAsteroid.this,AsteroidActivity.class);
                intent.putExtra("name", asteroidDate.getAsteroids().get(i).getName());
                intent.putExtra("diameter",asteroidDate.getAsteroids().get(i).getDiameter());
                intent.putExtra("hazardous", asteroidDate.getAsteroids().get(i).getHazardous());
                intent.putExtra("velocity", asteroidDate.getAsteroids().get(i).getVelocity());
                intent.putExtra("distance", asteroidDate.getAsteroids().get(i).getDistance());
                intent.putExtra("origem", "App");
                startActivity(intent);


            }
        });

        //registerForContextMenu(asteroid_list);
    }

    private void getAsteroidsInfo(String urlStr){

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
                    Thread.sleep(300);
                    nAsteroid.setText("Number of asteroids: " + asteroidDate.getAsteroids().size());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = (MenuInflater) getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qrcode:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                String contents = data.getStringExtra("SCAN_RESULT");
                String[] tokens = contents.split("\n");
                String name = tokens[0].substring(5);
                String diameter = tokens[1].substring(19);
                String hazardous = tokens[2].substring(14);
                String velocity = tokens[3].substring(10);
                String distance = tokens[4].substring(19);

                Intent intent = new Intent(ListAsteroid.this,AsteroidActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("diameter",diameter);
                intent.putExtra("hazardous", hazardous);
                intent.putExtra("velocity", velocity);
                intent.putExtra("distance", distance);
                intent.putExtra("origem", "QRCode");
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}