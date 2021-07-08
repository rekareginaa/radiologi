package com.example.radiologi.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.R;
import com.example.radiologi.databinding.ActivityTerimaAdminBinding;
import com.example.radiologi.pdfManager.TampilkanDataPasienActivity;
import com.example.radiologi.utils.BitmapConverter;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class DetailPasienActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "extra_data";

    String urlupdatestatus = "https://dbradiologi.000webhostapp.com/api/users/updatestatus";

    String img, noregiS, norekaM, namaLengkaP, tangLahiR, gendeR, diagnosA;
    String untuk, tdT, status;
    Bitmap gambarradiologi, tandatangannyadokterkah;
    byte[] gambarradiologiloini, tandatangannyadokterloini;

    ProgressDialog progressDialog;
    private ActivityTerimaAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTerimaAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);

        binding.ivBack.setOnClickListener(view -> onBackPressed());

        noregiS = getIntent().getStringExtra("noregis");
        norekaM = getIntent().getStringExtra("norekam");
        namaLengkaP = getIntent().getStringExtra("namalengkap");
        tangLahiR = getIntent().getStringExtra("tanggalahir");
        gendeR = getIntent().getStringExtra("gender");
        img = getIntent().getStringExtra("gambar");
        untuk = getIntent().getStringExtra("untuk");
        diagnosA = getIntent().getStringExtra("diagnosa");
        tdT = getIntent().getStringExtra("tdt");
        status = getIntent().getStringExtra("status");

        loadImage1();
        loadImage2();

        binding.tvNoregis.setText(noregiS);
        binding.tvNorekmed.setText(norekaM);
        binding.tvNama.setText(namaLengkaP);
        binding.tvTglahir.setText(tangLahiR);
        binding.tvGender.setText(gendeR);

        if (diagnosA.equals("")) {
            binding.tvDiagnosa.setText("belum ada diagnosa dari dokter");
        } else {
            binding.tvDiagnosa.setText(diagnosA);
        }

        Log.i("regina", status);
        Log.i("regina", tdT);

        Picasso.get().setLoggingEnabled(true);
        Picasso.get()
                .load(img)
                .into(binding.photoView);


        switch (untuk) {
            case "admin":
                if (status.equals("0")) {
                    binding.btnCreate.setVisibility(View.GONE);
                } else {
                    binding.btnCreate.setVisibility(View.VISIBLE);
                }
                break;
            case "dokter":
                binding.btnCreate.setVisibility(View.GONE);
                break;
        }

        binding.btnCreate.setOnClickListener(view -> updateStatus());
    }

    private void updateStatus() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdatestatus,
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

                        progressDialog.dismiss();

                        Intent intent = new Intent(DetailPasienActivity.this, TampilkanDataPasienActivity.class);
                        intent.putExtra("noregis", noregiS);
                        intent.putExtra("norekam", norekaM);
                        intent.putExtra("namalengkap", namaLengkaP);
                        intent.putExtra("tanggalahir", tangLahiR);
                        intent.putExtra("gender", gendeR);
                        intent.putExtra("gambar", BitmapConverter.bitmapToString(gambarradiologi));
                        intent.putExtra("diagnosa", diagnosA);
                        intent.putExtra("tandatangan", BitmapConverter.bitmapToString(tandatangannyadokterkah));
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
                params.put("status", "2");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailPasienActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showDialog(){
        progressDialog.setTitle("Mohon Tunggu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideDialog(){
        if (progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }

    void loadImage1() {
        Picasso.get().load(img).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                gambarradiologi = bitmap;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    void loadImage2() {
        Picasso.get().load(tdT).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                tandatangannyadokterkah = bitmap;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

}