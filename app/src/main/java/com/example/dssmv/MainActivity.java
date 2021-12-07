package com.example.dssmv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.*;
import androidx.biometric.BiometricPrompt;
import android.os.Build;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private EditText pin;
    private Button btnLoginPin, btnLoginBio, btnLogin;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private SharedPreferences sharedPref;
    private String sharedPin;
    private TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        sharedPin = sharedPref.getString("PIN", "");

        textView = (TextView) findViewById(R.id.textNew);

        btnLoginPin = (Button) findViewById(R.id.btn_pin);
        btnLoginBio = (Button) findViewById(R.id.btn_bio);
        btnLogin = (Button) findViewById(R.id.btn_login);

        pin = (EditText) findViewById(R.id.pin);

        btnLoginPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPin.equals("")) {
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
                pin.setVisibility(View.VISIBLE);
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
                textView.setVisibility(View.INVISIBLE);
                pin.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MainActivity.this,ListAsteroid.class);
                startActivity(intent);
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
                    if(pin.getText().toString().equals(sharedPin)) {

                        Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                        textView.setVisibility(View.INVISIBLE);
                        pin.setVisibility(View.INVISIBLE);
                        btnLogin.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(MainActivity.this,ListAsteroid.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong pin", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("PIN", pin.getText().toString());
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                    textView.setVisibility(View.INVISIBLE);
                    pin.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(MainActivity.this,ListAsteroid.class);
                    startActivity(intent);

                }
            }
        });

    }
}