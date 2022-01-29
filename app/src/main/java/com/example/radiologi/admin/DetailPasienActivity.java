package com.example.radiologi.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.databinding.ActivityTerimaAdminBinding;
import com.example.radiologi.pdfManager.TampilkanDataPasienActivity;
import com.example.radiologi.utils.BitmapConverter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailPasienActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "extra_data";

    String urlupdatestatus = "https://dbradiologi.000webhostapp.com/api/users/updatestatus";

    String img, noregiS, norekaM, namaLengkaP, tangLahiR, gendeR, diagnosA;
    String untuk, tdT, status;
    Bitmap gambarradiologi, tandatangannyadokterkah;

    ProgressDialog progressDialog;
    private ActivityTerimaAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTerimaAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);

        binding.ivBack.setOnClickListener(view -> onBackPressed());

        if (getIntent().getParcelableExtra(EXTRA_DATA) instanceof ItemAdminEntity) {
            ItemAdminEntity items = getIntent().getParcelableExtra(EXTRA_DATA);
            populateData(
                    items.getNoregis(),
                    items.getNorekam(),
                    items.getNamaPasien(),
                    items.getTanggalLahir(),
                    items.getGender(),
                    items.getGambar(),
                    items.getPenerima(),
                    items.getDiagnos(),
                    items.getTtd(),
                    items.getStatus()
            );
        } else {
            ItemDoctorEntity items = getIntent().getParcelableExtra(EXTRA_DATA);
            populateData(
                    items.getNoregis(),
                    items.getNorekam(),
                    items.getNamaPasien(),
                    items.getTanggalLahir(),
                    items.getGender(),
                    items.getGambar(),
                    items.getPenerima(),
                    items.getDiagnos(),
                    items.getTtd(),
                    items.getStatus()
            );
        }


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

    private void populateData(
            String noregiS,
            String norekaM,
            String namaLengkaP,
            String tanggalLahir,
            String gender,
            String img,
            String untuk,
            String diagnosa,
            String ttd,
            String status

    ) {
        this.noregiS = noregiS;
        this.norekaM = norekaM;
        this.namaLengkaP = namaLengkaP;
        this.tangLahiR = tanggalLahir;
        this.gendeR = gender;
        this.img = img;
        this.untuk = untuk;
        this.diagnosA = diagnosa;
        this.tdT = ttd;
        this.status = status;
    }

    private void updateStatus() {
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdatestatus,
                response -> {
                    hideDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject resp = jsonObject.getJSONObject("notice");
                        String msg = resp.getString("text");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
            hideDialog();
            Toast.makeText(getApplicationContext(), "error" + error.toString(), Toast.LENGTH_LONG).show();
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

    private void showDialog() {
        progressDialog.setTitle("Mohon Tunggu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    private void loadImage1() {
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

    private void loadImage2() {
        if (!status.equals("0")) {
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
        } else {
            binding.btnCreate.setVisibility(View.GONE);
        }
    }

}