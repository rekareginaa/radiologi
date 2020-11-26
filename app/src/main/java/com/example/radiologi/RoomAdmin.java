package com.example.radiologi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RoomAdmin extends AppCompatActivity {
    DatePickerDialog picker;
    EditText eText, etNoRekam, etNamaPasien;
    Button btnGet;
    Button btnUnggah;
    ImageView ivFoto;
    final int CODE_GALERI_REQUEST = 999;

    private static final String url = "https://dbradiologi.000webhostapp.com/api/users/addimage";
    private static final String urlSimpanNama = "https://dbradiologi.000webhostapp.com/api/users/simpannama";
    Bitmap bitmap;
    private RadioGroup radioGender;

    String tanggallahir, noRekam, namaPasien, gender, nipsaya, namagambar, norekambaru;

    private static final int PERMISSION_CODE =1;
    private static final int PICK_IMAGE=1;

    //
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_admin);

        nipsaya = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "nip");
        etNoRekam = findViewById(R.id.et_noRekam);
        etNamaPasien = findViewById(R.id.et_nama);
        radioGender = findViewById(R.id.radioGender);
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.pria:
                        gender = "Pria";
                        break;
                    case R.id.wanita:
                        gender = "Wanita";
                        break;
                }
            }
        });

        eText = findViewById(R.id.et_date);
        eText.setInputType(InputType.TYPE_NULL);
        btnGet=findViewById(R.id.buttonDate);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(RoomAdmin.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                                tanggallahir = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        ivFoto = findViewById(R.id.iv_foto);
        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ActivityCompat.requestPermissions(RoomAdmin.this, new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALERI_REQUEST);*/
                requestPermission();
            }
        });

        btnUnggah= findViewById(R.id.btn_unggah);
        btnUnggah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToCloudinary(filePath);
            }
        });
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALERI_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), CODE_GALERI_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "Tidak dapat diakses", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        filePath = getRealPathFromUri(data.getData(), RoomAdmin.this);

        if (requestCode == CODE_GALERI_REQUEST && resultCode == RESULT_OK && data !=null) {
            //Uri path = data.getData();
            try {
                *//*InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);*//*
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ivFoto.setImageBitmap(bitmap);
                btnUnggah.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(RoomAdmin.this, "o", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    }*/

    private void requestPermission(){
        if(ContextCompat.checkSelfPermission
                (RoomAdmin.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ){
            accessTheGallery();
        } else {
            ActivityCompat.requestPermissions(
                    RoomAdmin.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheGallery();
            }else {
                Toast.makeText(RoomAdmin.this, "permission denied", Toast.LENGTH_SHORT).show();
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
        filePath = getRealPathFromUri(data.getData(), RoomAdmin.this);
        Log.i("regina", filePath);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            try {
                //set picked image to the mProfile
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ivFoto.setImageBitmap(bitmap);
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

        noRekam = etNoRekam.getText().toString().trim();
        namaPasien = etNamaPasien.getText().toString().trim();

        Toast.makeText(getApplicationContext(), "Mengirim", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            //norekambaru = jsonObject.getString("data");
                            Toast.makeText(getApplicationContext(), Response, Toast.LENGTH_LONG).show();

                            //saveNama();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(RoomAdmin.this, DataAdmin.class);
                        startActivity(intent);
                        finish();
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
                //String gambar = imagetoString(bitmap);
                params.put("norekam", noRekam);
                params.put("namapasien", namaPasien);
                params.put("gender", gender);
                params.put("tanglahir", tanggallahir);
                params.put("gambar", namagambar);
                params.put("pengirim", nipsaya);
                params.put("penerima", "");
                params.put("diagnosa", "");
                params.put("ttd", "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RoomAdmin.this);
        requestQueue.add(stringRequest);
    }

    private void saveNama() {

        Toast.makeText(getApplicationContext(), "Mengirim", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSimpanNama,
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
                params.put("norekam", norekambaru);
                params.put("gambar", namagambar);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RoomAdmin.this);
        requestQueue.add(stringRequest);
    }

    /*private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageType = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageType, Base64.DEFAULT);
    }*/
}