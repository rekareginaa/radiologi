package com.example.radiologi.admin.formAddData;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.radiologi.R;
import com.example.radiologi.admin.home.DataAdminActivity;
import com.example.radiologi.admin.viewModel.AdminViewModel;
import com.example.radiologi.admin.viewModel.AdminViewModelFactory;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.databinding.ActivityRoomAdminBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormAddDataActivity extends AppCompatActivity implements ConfirmDialogFragment.ConfirmDialogListener {
    DatePickerDialog picker;
    String token;

    String tanggallahir, noRekam, namaPasien, gender, nipsaya, namagambar;

    private static final int PERMISSION_CODE =1;
    private static final int PICK_IMAGE=1;

    ArrayList<String> listRegis;
    String noRegisNew;

    String filePath;

    private AdminViewModel viewModel;
    private ActivityRoomAdminBinding binding;
    private ProgressDialog progressDialog;

    public static final String EXTRA_LIST_REGIS = "regis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listRegis = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        AdminViewModelFactory factory = AdminViewModelFactory.getInstance(this);
        viewModel = new ViewModelProvider(this, factory).get(AdminViewModel.class);

        binding.getRoot().setOnClickListener(view -> onBackPressed());

        nipsaya = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "nip");

        listRegis = getIntent().getStringArrayListExtra(EXTRA_LIST_REGIS);

        binding.radioGender.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.pria){
                gender = "Pria";
            } else if (i == R.id.wanita){
                gender = "Wanita";
            }
        });

        binding.buttonDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            final int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            picker = new DatePickerDialog(FormAddDataActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        binding.tvTanggalLahir.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year1);
                        tanggallahir = dayOfMonth + "-" + (monthOfYear+1) + "-" + year1;
                    }, year, month, day);
            picker.show();
        });
        binding.ivFoto.setOnClickListener(view -> {
            requestPermission();
        });
        binding.btnUnggah.setOnClickListener(view -> {

            noRegisNew = binding.etNoRegis.getText().toString();
            noRekam = binding.etNoRekam.getText().toString().trim();
            namaPasien = binding.etNama.getText().toString().trim();

            if (listRegis.contains(noRegisNew)) {
                Toast.makeText(getApplicationContext(), "Nomor Registrasi telah digunakan", Toast.LENGTH_SHORT).show();
            } else {
                dialogs();
            }
        });

        observeStatusUpload();
    }

    private void requestPermission(){
        if(ContextCompat.checkSelfPermission
                (FormAddDataActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ){
            accessTheGallery();
        } else {
            ActivityCompat.requestPermissions(
                    FormAddDataActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheGallery();
            } else {
                Toast.makeText(FormAddDataActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void accessTheGallery(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get the image's file location
        assert data != null;
        filePath = getRealPathFromUri(data.getData(), FormAddDataActivity.this);
        Log.i("regina", filePath);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            try {
                //set picked image to the mProfile
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                binding.ivFoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
                //mText.setText("image URL: "+resultData.get("url").toString());
                Log.i("regina", "sukses");
                namagambar = resultData.get("url").toString();
                uploadImage();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.i("regina",  error.getDescription());
                //mText.setText("error "+ error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.i("regina",  error.getDescription());
               // mText.setText("Reshedule "+error.getDescription());
            }
        }).dispatch();
    }

    private String getRealPathFromUri(Uri imageUri, Activity activity){
        Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);

        if(cursor==null) {
            return imageUri.getPath();
        }else{
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void uploadImage() {

        Map<String, String> params = new HashMap<>();
        //String gambar = imagetoString(bitmap);
        params.put("noregis", noRegisNew);
        params.put("norekam", noRekam);
        params.put("namapasien", namaPasien);
        params.put("gender", gender);
        params.put("tanglahir", tanggallahir);
        params.put("gambar", namagambar);
        params.put("pengirim", nipsaya);
        params.put("penerima", "");
        params.put("diagnosa", "");
        params.put("ttd", "");
        params.put("token_tujuan", token);
        params.put("title", "Data Pasien Baru");
        params.put("message", "Admin mengirimkan data untuk dilengkapi");
        viewModel.setParameters(params);
    }

    private void observeStatusUpload(){
        viewModel.getResponse.observe(this, result -> {
            switch (result.status){
                case LOADING:
                    break;
                case ERROR:
                    hideDialog();
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    hideDialog();
                    Toast.makeText(this, "Sukses menambahkan data", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormAddDataActivity.this, DataAdminActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        });
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

    private void dialogs(){
        ConfirmDialogFragment dialog = new ConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO, filePath);
        bundle.putString(NO_REG, noRegisNew);
        bundle.putString(NO_RECORD, noRekam);
        bundle.putString(NAME, namaPasien);
        bundle.putString(DATE, tanggallahir);
        bundle.putString(GENDER, gender);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "my_fragment");
    }

    public static final String PHOTO = "photo_path";
    public static final String NO_REG = "no_registration";
    public static final String NO_RECORD = "no_medical_record";
    public static final String NAME = "name";
    public static final String DATE = "born_date";
    public static final String GENDER = "gender";

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        showDialog();
        uploadToCloudinary(filePath);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d("CLICK", "NO");
    }

    /*private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageType = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageType, Base64.DEFAULT);
    }*/
}