package com.example.dssmv;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class AsteroidActivity extends AppCompatActivity {

    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asteroid);

        Intent intent = getIntent();

        TextView name = (TextView) findViewById(R.id.name);
        TextView hazardous = (TextView) findViewById(R.id.hazardous);
        TextView diameter = (TextView) findViewById(R.id.diameter);
        TextView velocity = (TextView) findViewById(R.id.velocity);
        TextView distance = (TextView) findViewById(R.id.distance);

        Button button = (Button) findViewById(R.id.share);

        name.setText(intent.getStringExtra("name"));
        diameter.setText("Estimated diameter: " + intent.getDoubleExtra("diameter",0.0) + " KM");
        hazardous.setText("Is dangerous : " + intent.getBooleanExtra("hazardous",false));
        velocity.setText("Velocity : " + intent.getDoubleExtra("velocity",0.0) + " KM/H");
        distance.setText("Distance to earth : " + intent.getDoubleExtra("distance",0.0) + " KM");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.qrcode_layout,null);
                ImageView qrcodeImage = (ImageView) layout.findViewById(R.id.qrcodeimg);
                Button btn = (Button) layout.findViewById(R.id.btn_dismiss);

                Bitmap bm = null;
                try {
                    bm = encodeAsBitmap("Name: " + intent.getStringExtra("name"), BarcodeFormat.QR_CODE, 500, 500);
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
            }
        });

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
