package com.example.radiologi.accountsManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.R;
import com.example.radiologi.admin.home.DataAdminActivity;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.databinding.ActivityLoginBinding;
import com.example.radiologi.dokter.home.DataDokterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String urlLogin="https://dbradiologi.000webhostapp.com/api/users/login";

    String nip, password, role, token;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnMasuk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_masuk) {
            login();
        }
        
    }

    private void login() {
        nip = binding.etNIP.getText().toString().trim();
        password = binding.etKataSandi.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, urlLogin, response -> {
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
                    }
                    else if (role.equals("dokter")) {
                        Intent intent = new Intent(getApplicationContext(), DataDokterActivity.class);
                        startActivity(intent);
                        finish();
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
        }, error -> {}) {
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