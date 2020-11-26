package com.example.radiologi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TerimaAdmin extends AppCompatActivity {

    TextView noRekam, namaLengkap, tangLahir, gender, diagnosa;
    PhotoView foto;
    ImageView tanda;
    String img, norekaM, namaLengkaP, tangLahiR, gendeR, diagnosA;
    String untuk, tdT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terima_admin);

        noRekam = findViewById(R.id.tv_norekmed);
        namaLengkap = findViewById(R.id.tv_nama);
        tangLahir = findViewById(R.id.tv_tglahir);
        gender = findViewById(R.id.tv_gender);
        foto = findViewById(R.id.photo_view);
        diagnosa = findViewById(R.id.tv_diagnosa);
//        tanda = findViewById(R.id.photo_view_tdt);

        norekaM = getIntent().getStringExtra("norekam");
        namaLengkaP = getIntent().getStringExtra("namalengkap");
        tangLahiR = getIntent().getStringExtra("tanggalahir");
        gendeR = getIntent().getStringExtra("gender");
        img = getIntent().getStringExtra("gambar");
        untuk = getIntent().getStringExtra("untuk");
        diagnosA = getIntent().getStringExtra("diagnosa");
        tdT = getIntent().getStringExtra("tdt");

        noRekam.setText(norekaM);
        namaLengkap.setText(namaLengkaP);
        tangLahir.setText(tangLahiR);
        gender.setText(gendeR);

        if (diagnosA.equals("")) {
            diagnosa.setText("belum ada diagnosa dari dokter");
        } else {
            diagnosa.setText(diagnosA);
        }

        Log.i("regina", img);
        Log.i("regina", tdT);

        Button createPdf = findViewById(R.id.btn_create);

        Picasso.get().setLoggingEnabled(true);
        Picasso.get()
                .load(img)
                .into(foto);

//        Picasso.get().load(tdT).into(tanda);

        switch (untuk) {
            case "admin":
                createPdf.setVisibility(View.VISIBLE);
                break;
            case "dokter":
                createPdf.setVisibility(View.GONE);
                break;
        }

        createPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TerimaAdmin.this, TampilkanDataPasienActivity.class);
                intent.putExtra("norekam", norekaM);
                intent.putExtra("namalengkap", namaLengkaP);
                intent.putExtra("tanggalahir", tangLahiR);
                intent.putExtra("gender", gendeR);
                intent.putExtra("gambar", img);
                intent.putExtra("diagnosa", diagnosA);
                intent.putExtra("tandatangan", tdT);
                startActivity(intent);
            }
        });
    }
}