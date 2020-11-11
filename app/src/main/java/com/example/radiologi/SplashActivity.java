package com.example.radiologi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    int waktuLoading = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferenceManager.getBooleanPreferences(getApplicationContext(), "islogin")) {
                    if (SharedPreferenceManager.getStringPreferences(getApplicationContext(), "role").equals("admin")) {
                        Intent intent = new Intent(SplashActivity.this, DataAdmin.class);
                        startActivity(intent);
                        finish();
                    } else if (SharedPreferenceManager.getStringPreferences(getApplicationContext(), "role").equals("dokter")) {
                        Intent intent = new Intent(SplashActivity.this, DataDokter.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent home = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(home);
                    finish();
                }
            }
        }, waktuLoading);
    }
}