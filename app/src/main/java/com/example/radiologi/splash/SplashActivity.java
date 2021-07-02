package com.example.radiologi.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cloudinary.android.MediaManager;
import com.example.radiologi.R;
import com.example.radiologi.accountsManager.LoginActivity;
import com.example.radiologi.admin.home.DataAdminActivity;
import com.example.radiologi.data.SharedPreferenceManager;
import com.example.radiologi.dokter.home.DataDokterActivity;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    int waktuLoading = 4000;

    Map config = new HashMap();

    public void configCloudinary() {

        config.put("cloud_name", "droykx53s");
        config.put("api_key", "425598729726584");
        config.put("api_secret", "R2Ge0Xhwg_PiWyZkxKwuRz6v58o");
        MediaManager.init(getApplicationContext(), config);
        SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "init", true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        if (!SharedPreferenceManager.getBooleanPreferences(getApplicationContext(), "init")) {
//        }
        //configCloudinary();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferenceManager.getBooleanPreferences(getApplicationContext(), "islogin")) {
                    if (SharedPreferenceManager.getStringPreferences(getApplicationContext(), "role").equals("admin")) {
                        Intent intent = new Intent(SplashActivity.this, DataAdminActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (SharedPreferenceManager.getStringPreferences(getApplicationContext(), "role").equals("dokter")) {
                        Intent intent = new Intent(SplashActivity.this, DataDokterActivity.class);
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