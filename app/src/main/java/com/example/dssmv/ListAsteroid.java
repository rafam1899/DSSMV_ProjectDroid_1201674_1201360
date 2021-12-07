package com.example.dssmv;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
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
            case R.id.share:

                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.qrcode_layout,null);
                qrcodeImage = (ImageView) layout.findViewById(R.id.qrcodeimg);
                Button btn = (Button) layout.findViewById(R.id.btn_dismiss);

                Bitmap bm = null;
                try {
                    bm = encodeAsBitmap(str.getLink(), BarcodeFormat.QR_CODE, 500, 500);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                if(bm != null) {
                    qrcodeImage.setImageBitmap(bm);
                }

                qrcodeImage.requestLayout();
                dialog.setContentView(layout);
                dialog.show();

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                return true;
            case R.id.view:
                Intent intent = new Intent(ListAsteroid.this,AsteroidActivity.class);
                intent.putExtra("name", str.getName());
                intent.putExtra("diameter",str.getDiameter());
                intent.putExtra("hazardous", str.getHazardous());
                startActivity(intent);
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