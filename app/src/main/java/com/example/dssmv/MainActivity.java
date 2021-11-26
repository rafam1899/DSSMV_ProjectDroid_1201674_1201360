package com.example.dssmv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.biometric.BiometricPrompt;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private NumberPicker n1, n2, n3, n4;
    private Button btnLoginPin, btnLoginBio, btnLogin;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private SharedPreferences sharedPref;
    private int p1, p2, p3, p4;
    private TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        p1 = sharedPref.getInt("P1", 0);
        p2 = sharedPref.getInt("P2", 0);
        p3 = sharedPref.getInt("P3", 0);
        p4 = sharedPref.getInt("P4", 0);

        textView = (TextView) findViewById(R.id.textNew);

        btnLoginPin = (Button) findViewById(R.id.btn_pin);
        btnLoginBio = (Button) findViewById(R.id.btn_bio);
        btnLogin = (Button) findViewById(R.id.btn_login);

        n1 = (NumberPicker) findViewById(R.id.n1);
        n2 = (NumberPicker) findViewById(R.id.n2);
        n3 = (NumberPicker) findViewById(R.id.n3);
        n4 = (NumberPicker) findViewById(R.id.n4);

        n1.setMaxValue(9);
        n1.setMinValue(0);
        n2.setMaxValue(9);
        n2.setMinValue(0);
        n3.setMaxValue(9);
        n3.setMinValue(0);
        n4.setMaxValue(9);
        n4.setMinValue(0);

        btnLoginPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p1 == 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Insert Pin and remember for next time");
                    btnLogin.setVisibility(View.VISIBLE);
                    btnLogin.setText("Save and Login");
                } else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Insert Pin");
                    btnLogin.setVisibility(View.VISIBLE);
                    btnLogin.setText("Login");
                }
                n1.setVisibility(View.VISIBLE);
                n2.setVisibility(View.VISIBLE);
                n3.setVisibility(View.VISIBLE);
                n4.setVisibility(View.VISIBLE);
            }
        });

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for Asteroids near us")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        btnLoginBio.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnLogin.getText() == "Login") {
                    if(n1.getValue() == p1 && n2.getValue() == p2 && n3.getValue() == p3 && n4.getValue() == p4) {

                        Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,ListAsteroid.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong pin", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("P1", n1.getValue());
                    editor.putInt("P2", n2.getValue());
                    editor.putInt("P3", n3.getValue());
                    editor.putInt("P4", n4.getValue());
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}