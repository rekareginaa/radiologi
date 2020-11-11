package com.example.radiologi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceManager {
    public Context context;
    private static  SharedPreferenceManager instance;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public SharedPreferenceManager(Context context) {
        super();
        this.context = context;
        this.preferences = context.getSharedPreferences("Radiologi", Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }
    
    public static SharedPreferenceManager getInstance(Context context) {
        if (instance==null) {
            synchronized (SharedPreferenceManager.class) {
                if (instance==null) {
                    instance = new SharedPreferenceManager(context);
                }
            }
        }
        return instance;
    }
    public static void savesStringPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    
    public static String getStringPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }
    
    public static void saveIntPreferences(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    
    public static int getIntPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);
    }
    
    public static void saveBooleanPreferences(Context context, String key, boolean b) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, b);
        editor.apply();
    }


    public static boolean getBooleanPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, false);
    }
    
     public static  void  clearPrefs (Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
     }
     
     private void setStringValue(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
     }
     
     private String getStringValue(String key) { return this.preferences.getString(key, "");}
}
