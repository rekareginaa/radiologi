package com.example.radiologi.accountsManager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.R;
import com.example.radiologi.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity  {

    private static final String url = "https://dbradiologi.000webhostapp.com/api/users/add";

    String nama, NIP, email, katasandi, role;

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.radioRole.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.dokter) {
                role = "dokter";
            } else if (i == R.id.admin) {
                role = "admin";
            }
        });

        binding.btnDaftar.setOnClickListener(view -> daftarUser());
    }

    private void daftarUser() {
        nama = binding.etNama.getText().toString().trim();
        NIP = binding.etNIP.getText().toString().trim();
        email = binding.etEmail.getText().toString().trim();
        katasandi = binding.etKataSandi.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> { },
                error -> { }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("nip", NIP);
                params.put("email", email);
                params.put("password", katasandi);
                params.put("role", role);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}