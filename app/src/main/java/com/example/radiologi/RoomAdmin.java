package com.example.radiologi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    Bitmap bitmap;
    private RadioGroup radioGender;

    String tanggallahir, noRekam, namaPasien, gender, nipsaya;


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
                                eText.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                                tanggallahir = year + "-" + monthOfYear + "-" + dayOfMonth;
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        ivFoto = findViewById(R.id.iv_foto);
        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(RoomAdmin.this, new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALERI_REQUEST);
            }
        });

        btnUnggah= findViewById(R.id.btn_unggah);
        btnUnggah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    @Override
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
        if (requestCode == CODE_GALERI_REQUEST && resultCode == RESULT_OK && data !=null) {
            Uri path = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                ivFoto.setImageBitmap(bitmap);
                btnUnggah.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(RoomAdmin.this, "o", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                String gambar = imagetoString(bitmap);
                params.put("norekam", noRekam);
                params.put("namapasien", namaPasien);
                params.put("gender", gender);
                params.put("tanglahir", tanggallahir);
                params.put("gambar", "namagambar");
                params.put("pengirim", nipsaya);
                params.put("penerima", "");
                params.put("diagnosa", "");
                params.put("ttd", "");
                params.put("gambars", gambar);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RoomAdmin.this);
        requestQueue.add(stringRequest);
    }

    private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageType = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageType, Base64.DEFAULT);
    }
}