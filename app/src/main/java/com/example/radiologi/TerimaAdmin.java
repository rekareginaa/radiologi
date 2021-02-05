package com.example.radiologi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TerimaAdmin extends AppCompatActivity {

    TextView noRegis, noRekam, namaLengkap, tangLahir, gender, diagnosa;
    PhotoView foto;
    ImageView tanda, ivBack;
    String img, noregiS, norekaM, namaLengkaP, tangLahiR, gendeR, diagnosA;
    String untuk, tdT, status;
    Bitmap gambarradiologi, tandatangannyadokterkah;
    byte[] gambarradiologiloini, tandatangannyadokterloini;
    ImageButton createPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terima_admin);

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        noRegis = findViewById(R.id.tv_noregis);
        noRekam = findViewById(R.id.tv_norekmed);
        namaLengkap = findViewById(R.id.tv_nama);
        tangLahir = findViewById(R.id.tv_tglahir);
        gender = findViewById(R.id.tv_gender);
        foto = findViewById(R.id.photo_view);
        diagnosa = findViewById(R.id.tv_diagnosa);
//        tanda = findViewById(R.id.photo_view_tdt);
        createPdf = findViewById(R.id.btn_create);

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

        /*if (status.contains("0")) {
            createPdf.setVisibility(View.GONE);
        }
        else {
            createPdf.setVisibility(View.VISIBLE);
        }*/

        noRegis.setText(noregiS);
        noRekam.setText(norekaM);
        namaLengkap.setText(namaLengkaP);
        tangLahir.setText(tangLahiR);
        gender.setText(gendeR);

        if (diagnosA.equals("")) {
            diagnosa.setText("belum ada diagnosa dari dokter");
        } else {
            diagnosa.setText(diagnosA);
        }

        Log.i("regina", status);
        Log.i("regina", tdT);



        Picasso.get().setLoggingEnabled(true);
        Picasso.get()
                .load(img)
                .into(foto);

//        Picasso.get().load(tdT).into(tanda);

        switch (untuk) {
            case "admin":
                if (status.equals("0")) {
                    createPdf.setVisibility(View.GONE);
                } else {
                    createPdf.setVisibility(View.VISIBLE);
                }
                break;
            case "dokter":
                createPdf.setVisibility(View.GONE);
                break;
        }

        /*Picasso.get().load(img).into(new Target() {
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


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //gambarradiologi.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        gambarradiologiloini = stream.toByteArray();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        tandatangannyadokterloini = byteArrayOutputStream.toByteArray();*/

        createPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TerimaAdmin.this, TampilkanDataPasienActivity.class);
                intent.putExtra("noregis", noregiS);
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