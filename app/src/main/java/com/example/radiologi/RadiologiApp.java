package com.example.radiologi;

import android.app.Application;

import com.cloudinary.android.MediaManager;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;

import java.util.HashMap;

public class RadiologiApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HashMap<String, String> config = new HashMap<>();
        
        config.put("cloud_name", "droykx53s");
        config.put("api_key", "425598729726584");
        config.put("api_secret", "R2Ge0Xhwg_PiWyZkxKwuRz6v58o");

        MediaManager.init(getApplicationContext(), config);
        SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "init", true);
    }
}
