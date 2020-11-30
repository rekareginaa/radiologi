package com.example.radiologi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tejpratapsingh.pdfcreator.activity.PDFCreatorActivity;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFBody;
import com.tejpratapsingh.pdfcreator.views.PDFHeaderView;
import com.tejpratapsingh.pdfcreator.views.PDFTableView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFHorizontalView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFImageView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFLineSeparatorView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFVerticalView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class TampilkanDataPasienActivity extends PDFCreatorActivity {

    String noregiS, norekaM, namaLengkaP, tangLahiR, gendeR, gambaR, diagnosA, tdT;
    Bitmap gambarradiologi, tandatangannyadokterkah;
    byte[] gambarradiologiloini, tandatangannyadokterloini;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noregiS = getIntent().getStringExtra("noregis");
        norekaM = getIntent().getStringExtra("norekam");
        namaLengkaP = getIntent().getStringExtra("namalengkap");
        tangLahiR = getIntent().getStringExtra("tanggalahir");
        gendeR = getIntent().getStringExtra("gender");
        gambaR = getIntent().getStringExtra("gambar");
        diagnosA = getIntent().getStringExtra("diagnosa");
        tdT = getIntent().getStringExtra("tandatangan");

        Picasso.get().load(gambaR).into(new Target() {
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        createPDF("Hasil " + namaLengkaP, new PDFUtil.PDFUtilListener() {
            @Override
            public void pdfGenerationSuccess(File savedPDFFile) {
                Toast.makeText(TampilkanDataPasienActivity.this, "PDF Created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pdfGenerationFailure(Exception exception) {
                Toast.makeText(TampilkanDataPasienActivity.this, "PDF NOT Created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected PDFHeaderView getHeaderView(int pageIndex) {
        PDFHeaderView headerView = new PDFHeaderView(getApplicationContext());
/*

        PDFTextView pdfTextViewPage = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfTextViewPage.setText(String.format(Locale.getDefault(), "Page: %d", pageIndex + 1));
        pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        pdfTextViewPage.getView().setGravity(Gravity.CENTER_HORIZONTAL);

        headerView.addView(pdfTextViewPage);
*/

        PDFHorizontalView horizontalView = new PDFHorizontalView(getApplicationContext());

        PDFImageView logoUnhas = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams imageLayoutParam2 = new LinearLayout.LayoutParams(
                50,
                50, 0);
        logoUnhas.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
        logoUnhas.setImageResource(R.drawable.logounhas);
        imageLayoutParam2.setMargins(0, 0, 10, 0);
        logoUnhas.setLayout(imageLayoutParam2);
        horizontalView.addView(logoUnhas);

        PDFVerticalView verticalView = new PDFVerticalView(getApplicationContext());
        verticalView.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1));

        PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        SpannableString word = new SpannableString("RUMAH SAKIT UNIVERSITAS HASANUDDIN");
        pdfTextView.setText(word);
        pdfTextView.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        pdfTextView.getView().setGravity(Gravity.CENTER);
        pdfTextView.getView().setTypeface(pdfTextView.getView().getTypeface(), Typeface.BOLD);
        verticalView.addView(pdfTextView);

        PDFTextView pdfCompanyNameView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfCompanyNameView.setText("Sekretariat : Jl. Perintis Kemerdekaan Km. 10. Tamalanrea, Makassar 90245");
        pdfCompanyNameView.getView().setGravity(Gravity.CENTER);
        pdfCompanyNameView.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        verticalView.addView(pdfCompanyNameView);

        PDFTextView pdfCompanyNameView2 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfCompanyNameView2.setText("Website: www.rs.unhas.ac.id Email : info@rs.unhas.ac.id");
        pdfCompanyNameView2.getView().setGravity(Gravity.CENTER);
        pdfCompanyNameView2.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        verticalView.addView(pdfCompanyNameView2);

        horizontalView.addView(verticalView);

        PDFImageView logoRSUnhas = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(
                50,
                50, 0);
        logoRSUnhas.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
        logoRSUnhas.setImageResource(R.drawable.rsunhas);
        imageLayoutParam.setMargins(0, 0, 10, 0);
        logoRSUnhas.setLayout(imageLayoutParam);

        horizontalView.addView(logoRSUnhas);
        headerView.addView(horizontalView);
        return headerView;
    }

    @Override
    protected PDFBody getBodyViews() {
        PDFBody pdfBody = new PDFBody();

        /*PDFTextView pdfCompanyNameView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfCompanyNameView.setText("Sekretariat : Jl. Perintis Kemerdekaan Km. 10. Tamalanrea, Makassar 90245");
        pdfCompanyNameView.getView().setGravity(Gravity.CENTER);
        pdfBody.addView(pdfCompanyNameView);

        PDFTextView pdfAddressView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfAddressView.setText("Website: www.rs.unhas.ac.id Email : info@rs.unhas.ac.id ");
        pdfCompanyNameView.getView().setGravity(Gravity.CENTER);
        pdfBody.addView(pdfAddressView);*/

        PDFLineSeparatorView separatorLineView = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParamsForSeparatorLine = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2, 0);
        layoutParamsForSeparatorLine.setMargins(0, 10, 0, 0);
        separatorLineView.setLayout(layoutParamsForSeparatorLine);

        pdfBody.addView(separatorLineView);

        PDFVerticalView verticalView1 = new PDFVerticalView(getApplicationContext());
        verticalView1.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        PDFTextView textSurat = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        textSurat.setText("HASIL PEMERIKSAAN RADIOLOGI");
        textSurat.getView().setGravity(Gravity.CENTER);
        textSurat.getView().setPadding(0, 16, 0, 0);
        textSurat.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0));
        verticalView1.addView(textSurat);

        /*PDFLineSeparatorView separatorLineView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParamsForSeparatorLine1 = new LinearLayout.LayoutParams(
                180,
                1, 0);
        layoutParamsForSeparatorLine1.gravity = 17;
        layoutParamsForSeparatorLine1.setMargins(0, 3, 0, 0);
        separatorLineView1.setLayout(layoutParamsForSeparatorLine1);
        verticalView1.addView(separatorLineView1);

        PDFTextView textNomor = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        textNomor.setText("No.");
        textNomor.getView().setGravity(Gravity.CENTER);
        textNomor.getView().setPadding(10, 3, 0, 0);
        textNomor.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0));
        verticalView1.addView(textNomor);
*/
        PDFTextView text1 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        text1.setText("Yang bertanda tangan di bawah ini, dokter Rumah Sakit Universitas Hasanuddin dengan ini menerangkan bahwa :");
        text1.getView().setGravity(Gravity.LEFT);
        text1.getView().setPadding(10, 10, 0, 0);
        text1.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0));
        verticalView1.addView(text1);

        PDFHorizontalView horizontalView = new PDFHorizontalView(getApplicationContext());
        horizontalView.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 5));

        PDFTextView text2 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        text2.setText("No. Registrasi");
        text2.getView().setGravity(Gravity.LEFT);
        text2.getView().setPadding(10, 5, 0, 0);
        text2.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        horizontalView.addView(text2);

        PDFTextView noRegistrasi = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        noRegistrasi.setText(":  " + noregiS);
        noRegistrasi.getView().setGravity(Gravity.LEFT);
        noRegistrasi.getView().setPadding(0, 5, 0, 0);
        noRegistrasi.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 4));
        horizontalView.addView(noRegistrasi);

        verticalView1.addView(horizontalView);

        PDFHorizontalView horizontalView1 = new PDFHorizontalView(getApplicationContext());
        horizontalView1.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 5));

        PDFTextView text3 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        text3.setText("No. Rekam Medik");
        text3.getView().setGravity(Gravity.LEFT);
        text3.getView().setPadding(10, 5, 0, 0);
        text3.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        horizontalView1.addView(text3);

        PDFTextView noRekamMedik = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        noRekamMedik.setText(":  " + norekaM);
        noRekamMedik.getView().setGravity(Gravity.LEFT);
        noRekamMedik.getView().setPadding(0, 5, 0, 0);
        noRekamMedik.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 4));
        horizontalView1.addView(noRekamMedik);

        verticalView1.addView(horizontalView1);

        PDFHorizontalView horizontalView2 = new PDFHorizontalView(getApplicationContext());
        horizontalView2.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 5));

        PDFTextView text4 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        text4.setText("Nama Pasien");
        text4.getView().setGravity(Gravity.LEFT);
        text4.getView().setPadding(10, 5, 0, 0);
        text4.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        horizontalView2.addView(text4);

        PDFTextView namaPasien = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        namaPasien.setText(":  " + namaLengkaP);
        namaPasien.getView().setGravity(Gravity.LEFT);
        namaPasien.getView().setPadding(0, 5, 0, 0);
        namaPasien.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 4));
        horizontalView2.addView(namaPasien);

        verticalView1.addView(horizontalView2);

        PDFHorizontalView horizontalView3 = new PDFHorizontalView(getApplicationContext());
        horizontalView3.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 5));

        PDFTextView text5 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        text5.setText("Tanggal Lahir");
        text5.getView().setGravity(Gravity.LEFT);
        text5.getView().setPadding(10, 5, 0, 0);
        text5.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        horizontalView3.addView(text5);

        PDFTextView tanggalLahir = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        tanggalLahir.setText(":  " + tangLahiR);
        tanggalLahir.getView().setGravity(Gravity.LEFT);
        tanggalLahir.getView().setPadding(0, 5, 0, 0);
        tanggalLahir.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 4));
        horizontalView3.addView(tanggalLahir);

        verticalView1.addView(horizontalView3);

        PDFHorizontalView horizontalView4 = new PDFHorizontalView(getApplicationContext());
        horizontalView4.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 5));

        PDFTextView text6 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        text6.setText("Jenis Kelamin");
        text6.getView().setGravity(Gravity.LEFT);
        text6.getView().setPadding(10, 5, 0, 0);
        text6.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 2));
        horizontalView4.addView(text6);

        PDFTextView jenisKelamin = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        jenisKelamin.setText(":  " + gendeR);
        jenisKelamin.getView().setGravity(Gravity.LEFT);
        jenisKelamin.getView().setPadding(0, 5, 0, 0);
        jenisKelamin.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 3));
        horizontalView4.addView(jenisKelamin);

        verticalView1.addView(horizontalView4);

        PDFHorizontalView horizontalView5 = new PDFHorizontalView(getApplicationContext());
        horizontalView5.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 5));

        PDFTextView text7 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        text7.setText("Gambar");
        text7.getView().setGravity(Gravity.LEFT);
        text7.getView().setPadding(10, 5, 0, 0);
        text7.setLayout(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        horizontalView5.addView(text7);

        PDFTextView gambarPemeriksaan = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        gambarPemeriksaan.setText(":  ");
        gambarPemeriksaan.getView().setGravity(Gravity.LEFT);
        gambarPemeriksaan.getView().setPadding(0, 5, 0, 0);
        gambarPemeriksaan.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 4));
        horizontalView4.addView(gambarPemeriksaan);

        verticalView1.addView(horizontalView5);

//        PDFHorizontalView horizontalView5 = new PDFHorizontalView(getApplicationContext());
        PDFImageView fotoRadiologi = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams imageLayoutParam2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                300, 0);
        fotoRadiologi.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
        fotoRadiologi.setImageBitmap(gambarradiologi);
        imageLayoutParam2.setMargins(10, 0, 10, 0);
        fotoRadiologi.setLayout(imageLayoutParam2);
        verticalView1.addView(fotoRadiologi);

        PDFHorizontalView horizontalView6 = new PDFHorizontalView(getApplicationContext());
        horizontalView6.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 5));

        PDFTextView text8 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        text8.setText("Hasil Pemeriksaan");
        text8.getView().setPadding(10, 5, 0, 0);
        text8.setLayout(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        horizontalView6.addView(text8);

        PDFTextView hasilPemeriksaan = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        hasilPemeriksaan.setText(":  " + diagnosA);
        hasilPemeriksaan.getView().setPadding(0, 5, 0, 0);
        hasilPemeriksaan.setLayout(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 4));
        horizontalView6.addView(hasilPemeriksaan);

        verticalView1.addView(horizontalView6);

        PDFTextView tandaTangan = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        tandaTangan.setText("Dokter Penanggung Jawab");
        tandaTangan.getView().setGravity(Gravity.RIGHT);
        tandaTangan.getView().setPadding(10, 0, 0, 0);
        tandaTangan.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0));
        verticalView1.addView(tandaTangan);

        PDFImageView tandatangan = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams imageLayoutParam3 = new LinearLayout.LayoutParams(
                100,
                100, 0);
        imageLayoutParam3.gravity = Gravity.RIGHT;
        tandatangan.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
        tandatangan.setImageBitmap(tandatangannyadokterkah);
        tandatangan.setLayout(imageLayoutParam3);
        verticalView1.addView(tandatangan);

        PDFTextView namaDokter = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        namaDokter.setText("dr. ______________, Sp.Rad.M.Kes");
        namaDokter.getView().setGravity(Gravity.RIGHT);
        namaDokter.getView().setPadding(10, 0, 0, 0);
        namaDokter.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0));
        verticalView1.addView(namaDokter);

        pdfBody.addView(verticalView1);
//        pdfBody.addView(separatorLineView1);
//        pdfBody.addView(verticalView2);

        return pdfBody;
    }

    @Override
    protected void onNextClicked(final File savedPDFFile) {
        Uri pdfUri = Uri.fromFile(savedPDFFile);

        Intent intentPdfViewer = new Intent(TampilkanDataPasienActivity.this, PdfViewerActivity.class);
        intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);

        startActivity(intentPdfViewer);
    }
}
