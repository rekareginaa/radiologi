package com.example.radiologi.accountsManager;

import  androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity  {

    private static final String url = "https://dbradiologi.000webhostapp.com/api/users/add";

    Button btnDaftar;
    private EditText namaLengkap, noNIP, emailReg, passwordReg;
    private RadioGroup radioRole;
    String nama, NIP, email, katasandi, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        namaLengkap = findViewById(R.id.et_nama);
        noNIP = findViewById(R.id.et_NIP);
        emailReg = findViewById(R.id.et_email);
        passwordReg = findViewById(R.id.et_kata_sandi);

        radioRole = findViewById(R.id.radioRole);
        radioRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.dokter:
                        role = "dokter";
                        break;
                    case R.id.admin:
                        role = "admin";
                        break;
                }
            }
        });

        btnDaftar = findViewById(R.id.btn_daftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftarUser();
            }
        });
    }

    private void daftarUser() {
        nama = namaLengkap.getText().toString().trim();
        NIP = noNIP.getText().toString().trim();
        email = emailReg.getText().toString().trim();
        katasandi = passwordReg.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
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