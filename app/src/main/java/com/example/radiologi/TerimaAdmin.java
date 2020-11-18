package com.example.radiologi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class TerimaAdmin extends AppCompatActivity {

    TextView noRekam, namaLengkap, tangLahir, gender;
    PhotoView foto;
    String img, norekaM, namaLengkaP, tangLahiR, gendeR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terima_admin);

        noRekam = findViewById(R.id.tv_norekmed);
        namaLengkap = findViewById(R.id.tv_nama);
        tangLahir = findViewById(R.id.tv_tglahir);
        gender = findViewById(R.id.tv_gender);
        foto = findViewById(R.id.photo_view);

        norekaM = getIntent().getStringExtra("norekam");
        namaLengkaP = getIntent().getStringExtra("namalengkap");
        tangLahiR = getIntent().getStringExtra("tanggalahir");
        gendeR = getIntent().getStringExtra("gender");
        img = getIntent().getStringExtra("gambar");

        noRekam.setText(norekaM);
        namaLengkap.setText(namaLengkaP);
        tangLahir.setText(tangLahiR);
        gender.setText(gendeR);

        Log.i("regina", img);

        Button createPdf = findViewById(R.id.btn_create);

        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(img).into(foto);

        createPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TerimaAdmin.this, TampilkanDataPasienActivity.class);
                intent.putExtra("norekam", norekaM);
                intent.putExtra("namalengkap", namaLengkaP);
                intent.putExtra("tanggalahir", tangLahiR);
                intent.putExtra("gender", gendeR);
                startActivity(intent);
            }
        });
    }
}