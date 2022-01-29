package com.example.radiologi.admin.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.admin.home.fragments.DataAdminPagerAdapter;
import com.example.radiologi.accountsManager.LoginActivity;
import com.example.radiologi.R;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataAdminActivity extends AppCompatActivity {

    String url_cek = "https://dbradiologi.000webhostapp.com/api/users/cektoken";

    ImageButton imageButton;
    String msg, tokenLama;
    String nip, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_admin);

        tokenLama = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "token");

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }
            token = task.getResult();
            msg = getString(R.string.msg_token_fmt, token);

            cekToken();
        });

        final TabLayout tabLayout = findViewById(R.id.tab);
        final ViewPager viewPager = findViewById(R.id.admin_pager);

        DataAdminPagerAdapter pagerAdapter = new DataAdminPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imageButton = findViewById(R.id.option);

        imageButton.setOnClickListener(view -> showDialog());
    }


    private void cekToken() {
        StringRequest request = new StringRequest(Request.Method.POST, url_cek, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("text").equals("sukses")) {
                    SharedPreferenceManager.savesStringPreferences(getApplicationContext(), "token", msg);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d("regina", error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nip", nip);
                param.put("token", token);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Keluar dari aplikasi?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ya", (dialogInterface, i) -> {
                    SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "islogin", false);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Tidak", (dialog, i) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}