package com.example.radiologi.admin.formAddData;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.radiologi.data.dataSource.remote.response.DataItemDoctor;
import com.example.radiologi.data.dataSource.remote.response.DoctorListResponse;
import com.example.radiologi.databinding.ActivityRoomAdminBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormAddDataActivity extends AppCompatActivity implements ConfirmDialogFragment.ConfirmDialogListener {

    String tanggallahir, noRekam, namaPasien, gender, nipsaya, namagambar, token, noRegisNew, filePath, idDoctor;
    ArrayList<String> listRegis;

    private DatePickerDialog picker;
    private AdminViewModel viewModel;
    private ActivityRoomAdminBinding binding;
    private ProgressDialog progressDialog;
    private Uri uriPhotos;

    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE = 1;

    public static final String PHOTO = "photo_path";
    public static final String NO_REG = "no_registration";
    public static final String NO_RECORD = "no_medical_record";
    public static final String NAME = "name";
    public static final String DATE = "born_date";
    public static final String GENDER = "gender";
    public static final String EXTRA_LIST_REGIS = "regis";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listRegis = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        initViewModel();

        binding.getRoot().setOnClickListener(view -> onBackPressed());

        nipsaya = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "nip");

        listRegis = getIntent().getStringArrayListExtra(EXTRA_LIST_REGIS);

        binding.radioGender.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.pria) {
                gender = "Pria";
            } else if (i == R.id.wanita) {
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
                        binding.tvTanggalLahir.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                        tanggallahir = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    }, year, month, day);
            picker.show();
        });
        binding.ivFoto.setOnClickListener(view -> requestPermission());
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
        observeListDoctor();
    }

    private void initViewModel() {
        AdminViewModelFactory factory = AdminViewModelFactory.getInstance(this);
        viewModel = new ViewModelProvider(this, factory).get(AdminViewModel.class);
    }

    private void populateSpinner(ArrayList<DataItemDoctor> doctors){
        ArrayList<String> names = new ArrayList<>();

        for (DataItemDoctor doctor: doctors){
            names.add(doctor.getNama());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, names);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.spnListDoctor.setAdapter(arrayAdapter);
        binding.spnListDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                token = doctors.get(position).getToken();
                idDoctor = doctors.get(position).getNip();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void observeListDoctor() {
        viewModel.getGetDoctor().observe(this, result -> {
            switch (result.status) {
                case LOADING:
                case ERROR:
                    break;
                case SUCCESS:
                    DoctorListResponse data = result.data;
                    if (data != null){
                        ArrayList<DataItemDoctor> dataItemDoctors = new ArrayList<>(data.getData());
                        populateSpinner(dataItemDoctors);
                    }
            }
        });
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (FormAddDataActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
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

    public void accessTheGallery() {
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
        uriPhotos = data.getData();

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
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
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                //no op
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                //no op
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                namagambar = resultData.get("url").toString();
                uploadImage();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                //no op
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                //no op
            }
        }).dispatch();
    }

    private String getRealPathFromUri(Uri imageUri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);

        if (cursor == null) {
            return imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void uploadImage() {

        Map<String, String> params = new HashMap<>();
        params.put("noregis", noRegisNew);
        params.put("norekam", noRekam);
        params.put("namapasien", namaPasien);
        params.put("gender", gender);
        params.put("tanglahir", tanggallahir);
        params.put("gambar", namagambar);
        params.put("pengirim", nipsaya);
        params.put("penerima", idDoctor);
        params.put("diagnosa", "");
        params.put("ttd", "");
        params.put("token_tujuan", token);
        params.put("title", "Data Pasien Baru");
        params.put("message", "Admin mengirimkan data untuk dilengkapi");
        viewModel.setParameters(params);
    }

    private void observeStatusUpload() {
        viewModel.getResponse.observe(this, result -> {
            switch (result.status) {
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

    private void dialogs() {
        String photos = uriPhotos.toString();
        ConfirmDialogFragment dialog = new ConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO, photos);
        bundle.putString(NO_REG, noRegisNew);
        bundle.putString(NO_RECORD, noRekam);
        bundle.putString(NAME, namaPasien);
        bundle.putString(DATE, tanggallahir);
        bundle.putString(GENDER, gender);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "my_fragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        showDialog();
        uploadToCloudinary(filePath);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

}