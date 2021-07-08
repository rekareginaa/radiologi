package com.example.radiologi.dokter.formResponseData;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.radiologi.R;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.databinding.ActivityRoomDokterBinding;
import com.example.radiologi.dokter.viewModel.DoctorViewModel;
import com.example.radiologi.dokter.viewModel.DoctorViewModelFactory;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FormResponseDataDokterActivity extends AppCompatActivity {

    String norekaM;
    String namaLengkaP;
    String tangLahiR;
    String gendeR;
    String gambaR;
    String ttd;

    //untuk tanda tangan
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    String filePath;

    public static final String EXTRA_DATA = "extra_detail";

    private ActivityRoomDokterBinding binding;
    private DoctorViewModel viewModel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        binding = ActivityRoomDokterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(view -> onBackPressed());

        progressDialog = new ProgressDialog(this);

        DoctorViewModelFactory factory = DoctorViewModelFactory.getInstance(this);
        viewModel = new ViewModelProvider(this, factory).get(DoctorViewModel.class);

        final ItemDoctorEntity itemsData = getIntent().getParcelableExtra(EXTRA_DATA);

        if (itemsData != null) {
            norekaM = itemsData.getNorekam();
            namaLengkaP = itemsData.getNamaPasien();
            tangLahiR = itemsData.getTanggalLahir();
            gendeR = itemsData.getGender();
            gambaR = itemsData.getGambar();
        }

        binding.tvNorekmed.setText(norekaM);
        binding.tvNama.setText(namaLengkaP);
        binding.tvTgllahir.setText(tangLahiR);
        binding.tvGender.setText(gendeR);

        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(gambaR).into(binding.photoViiew);

        //untuk tanda tangan
        binding.signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //Toast.makeText(Ttd.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                binding.saveButton.setEnabled(true);
                binding.saveButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rounded_button, null));
                binding.clearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                binding.saveButton.setEnabled(false);
                binding.saveButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rounded_button_false, null));
                binding.clearButton.setEnabled(false);
            }
        });

        binding.clearButton.setOnClickListener(view -> binding.signaturePad.clear());

        binding.saveButton.setOnClickListener(view -> {
            showDialog();
            Bitmap signatureBitmap = binding.signaturePad.getSignatureBitmap();

            if (addJpgSignatureToGallery(signatureBitmap)) {
                Toast.makeText(FormResponseDataDokterActivity.this, "Signature saved into teh Gallery", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FormResponseDataDokterActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
            }
            if (addSvgSignatureToGallery(binding.signaturePad.getSignatureSvg())) {
                Toast.makeText(FormResponseDataDokterActivity.this, "SVG Signature save into the Gallery", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FormResponseDataDokterActivity.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
            }
        });

        observeStatusUpdate();

    }

    private void requestUpdate() {
        final String diagnosa = binding.etDiagnosa.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("norekam", norekaM);
        params.put("diagnosa", diagnosa);
        params.put("ttd", ttd);
        params.put("status", "1");
        params.put("title", "Data Pasien Baru");
        params.put("message", "Dokter mengirimkan data yang telah dilengkapi");

        viewModel.setParameters(params);
    }

    //untuk tanda tangan
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(FormResponseDataDokterActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("Signatured", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("Tdt"), String.format(Locale.ROOT, "Signature_%d.jpg", System.currentTimeMillis()));
            Log.i("regina", String.valueOf(photo));
            filePath = String.valueOf(photo);   //filepathnya
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);

            uploadToCloudinary(filePath);

            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        FormResponseDataDokterActivity.this.sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        Log.i("regina", signatureSvg);
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format(Locale.ROOT, "Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void verifyStoragePermissions(FormResponseDataDokterActivity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    //untuk cloudinary
    private void uploadToCloudinary(String filePath) {
        Log.d("regina", "sign up uploadToCloudinary- ");
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                //mText.setText("start");
                Log.i("regina", "start");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                //mText.setText("Uploading... ");
                Log.i("regina", "uploading...");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                final Object url = resultData.get("url");
                if (url != null) {
                    Log.i("regina", "sukses");
                    Log.i("regina", Objects.requireNonNull(resultData.get("url")).toString());
                    ttd = Objects.requireNonNull(resultData.get("url")).toString();
                    requestUpdate();
                }
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.i("regina", error.getDescription());
                //mText.setText("error "+ error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.i("regina", error.getDescription());
                // mText.setText("Reshedule "+error.getDescription());
            }
        }).dispatch();
    }

    private void observeStatusUpdate() {
        viewModel.getResponse.observe(this, result -> {
            switch (result.status) {
                case LOADING:
                    Toast.makeText(this, "mohon tunggu", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    hideDialog();
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    hideDialog();
                    Toast.makeText(this, "berhasil upload", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void showDialog(){
        progressDialog.setTitle("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideDialog(){
        if (progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }
}