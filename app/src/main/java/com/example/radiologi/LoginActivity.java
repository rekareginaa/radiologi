package com.example.radiologi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.home.DataAdminActivity;
import com.example.radiologi.home.DataDokterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String urlLogin="https://dbradiologi.000webhostapp.com/api/users/login";

    Button btnMasuk;
    EditText etNip, etPass;
    String nip, password, role, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intView();
    }

    private void intView() {
        btnMasuk = findViewById(R.id.btn_masuk);
        etNip = findViewById(R.id.et_NIP);
        etPass = findViewById(R.id.et_kata_sandi);

        btnMasuk.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_masuk:
                login();
                break;
        }
        
    }

    private void login() {
        nip = etNip.getText().toString().trim();
        password = etPass.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, urlLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("iloveyou", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("sukses")) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i=0; i < array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            role = object1.getString("role");
                            token = object1.getString("token");
                        }
                        SharedPreferenceManager.savesStringPreferences(getApplicationContext(), "nip", nip );
                        SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "islogin", true );
                        SharedPreferenceManager.savesStringPreferences(getApplicationContext(), "role", role);
                        SharedPreferenceManager.savesStringPreferences(getApplicationContext(), "token", token);
                        if (role.equals("admin")) {
                            Intent intent = new Intent(getApplicationContext(), DataAdminActivity.class);
                            startActivity(intent);
                            finish();
                            Log.i("regina", "role admin");
                        }
                        else if (role.equals("dokter")) {
                            Intent intent = new Intent(getApplicationContext(), DataDokterActivity.class);
                            startActivity(intent);
                            finish();
                            Log.i("regina", "role dokter");
                        }
                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                    }
                    else if (object.getString("status").equals("username")) {
                        Toast.makeText(getApplicationContext(), "Username Salah", Toast.LENGTH_LONG).show();
                    }
                    else if (object.getString("status").equals("password")) {
                        Toast.makeText(getApplicationContext(), "Password Salah", Toast.LENGTH_SHORT).show();
                    }
                    else if (object.getString("status").equals("gagal")) {
                        Toast.makeText(getApplicationContext(),"Login Gagal", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String>getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nip", nip);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}