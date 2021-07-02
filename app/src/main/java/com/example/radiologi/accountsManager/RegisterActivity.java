package com.example.radiologi.accountsManager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity  {

    private static final String url = "https://dbradiologi.000webhostapp.com/api/users/add";

    Button btnDaftar;
    private EditText namaLengkap, noNIP, emailReg, passwordReg;
    String nama, NIP, email, katasandi, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        namaLengkap = findViewById(R.id.et_nama);
        noNIP = findViewById(R.id.et_NIP);
        emailReg = findViewById(R.id.et_email);
        passwordReg = findViewById(R.id.et_kata_sandi);

        RadioGroup radioRole = findViewById(R.id.radioRole);
        radioRole.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.dokter) {
                role = "dokter";
            } else if (i == R.id.admin) {
                role = "admin";
            }
        });

        btnDaftar = findViewById(R.id.btn_daftar);
        btnDaftar.setOnClickListener(view -> daftarUser());
    }

    private void daftarUser() {
        nama = namaLengkap.getText().toString().trim();
        NIP = noNIP.getText().toString().trim();
        email = emailReg.getText().toString().trim();
        katasandi = passwordReg.getText().toString().trim();

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