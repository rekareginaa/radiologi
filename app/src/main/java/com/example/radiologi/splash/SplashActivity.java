package com.example.radiologi.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.radiologi.R;
import com.example.radiologi.accountsManager.LoginActivity;
import com.example.radiologi.admin.home.DataAdminActivity;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.dokter.home.DataDokterActivity;

public class SplashActivity extends AppCompatActivity {

    int waktuLoading = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
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
        }, waktuLoading);
    }
}