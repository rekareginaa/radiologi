package com.example.radiologi;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RoomDokter extends AppCompatActivity {

    EditText etDiagnosa;
    TextView noRekam, namaLengkap, tangLahir, gender;
    String norekaM, namaLengkaP, tangLahiR, gendeR, gambaR, diagnosa, ttd;
    PhotoView foto;

    //untuk tanda tangan
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;

    private static final String urlupdate = "https://dbradiologi.000webhostapp.com/api/users/updatedokter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_dokter);

        etDiagnosa=findViewById(R.id.et_diagnosa);
        noRekam = findViewById(R.id.tv_norekmed);
        namaLengkap = findViewById(R.id.tv_nama);
        tangLahir = findViewById(R.id.tv_tgllahir);
        gender = findViewById(R.id.tv_gender);
        foto = findViewById(R.id.photo_viiew);

        norekaM = getIntent().getStringExtra("norekam");
        namaLengkaP = getIntent().getStringExtra("namalengkap");
        tangLahiR = getIntent().getStringExtra("tanggalahir");
        gendeR = getIntent().getStringExtra("gender");
        gambaR = getIntent().getStringExtra("gambar");

        noRekam.setText(norekaM);
        namaLengkap.setText(namaLengkaP);
        tangLahir.setText(tangLahiR);
        gender.setText(gendeR);

        Log.i("regina", gambaR);

        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(gambaR).into(foto);
    }

    /*private void updateData () {
        diagnosa = etDiagnosa.getText().toString();
        ttd =
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(getApplicationContext(), Response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(RoomDokter.this, DataDokter.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error"+ error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("norekam", norekaM);
                params.put("diagnosa", "");
                params.put("ttd", "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RoomDokter.this);
        requestQueue.add(stringRequest);
    }*/
}