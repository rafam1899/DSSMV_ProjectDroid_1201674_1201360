package com.example.dssmv;

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
    private Button btnLoginPin, btnLoginBio;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @RequiresApi(api = Build.VERSION_CODES.P)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoginPin = (Button) findViewById(R.id.btn_pin);
        btnLoginBio = (Button) findViewById(R.id.btn_bio);
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
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
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

    }
}