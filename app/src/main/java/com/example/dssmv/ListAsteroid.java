package com.example.dssmv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ListAsteroid extends AppCompatActivity {

    AsteroidDate asteroidDate = null;
    ListView asteroid_list;
    ListViewAdapterAsteroidInfo adapter;
    TextView nAsteroid;
    ImageView qrcodeImage;
    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        asteroid_list = (ListView) findViewById(R.id.weather_list);
        nAsteroid = (TextView) findViewById(R.id.nAsteroids);
        qrcodeImage = (ImageView) findViewById(R.id.qrcode);

        asteroidDate = new AsteroidDate();
        adapter = new ListViewAdapterAsteroidInfo(getApplicationContext(),R.layout.list_asteroid,asteroidDate.getAsteroids());
        asteroid_list.setAdapter(adapter);

        getAsteroidsInfo(Utils.URL_SERVICE_PREFIX + "2021-12-06" + Utils.URL_SERVICE_MIDDLE + "2021-12-06" + Utils.URL_SERVICE_SUFFIX);

        registerForContextMenu(asteroid_list);
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
                Toast.makeText(getApplicationContext(),
                        "QRCODE ", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = (MenuInflater) getMenuInflater();
        menuInflater.inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = info.position;
        long id = info.id;
        AsteroidInfo str = asteroidDate.getAsteroids().get(pos);
        switch (item.getItemId()) {
            case R.id.partilhar:

                AlertDialog alertDialog = new AlertDialog.Builder(ListAsteroid.this).create();
                alertDialog.setTitle("QRCode Generated");
                alertDialog.setIcon(R.drawable.asteroid);

                Bitmap bm = null;
                try {
                    bm = encodeAsBitmap(str.getLink(), BarcodeFormat.QR_CODE, 250, 250);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                if(bm != null) {
                    qrcodeImage.setImageBitmap(bm);
                }

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    Bitmap encodeAsBitmap(String str, BarcodeFormat barcodeFormat, int width, int heigth) throws WriterException {
        BitMatrix result;
        Bitmap bitmap=null;
        try
        {
            result = new MultiFormatWriter().encode(str, barcodeFormat, width, heigth, null);

            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? black:white;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        } catch (Exception iae) {
            iae.printStackTrace();
            return null;
        }
        return bitmap;
    }
}