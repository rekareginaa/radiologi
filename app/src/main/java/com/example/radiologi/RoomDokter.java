package com.example.radiologi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class RoomDokter extends AppCompatActivity {

    EditText etDiagnosa;
    TextView noRekam, namaLengkap, tangLahir, gender;
    String norekaM, namaLengkaP, tangLahiR, gendeR, gambaR, diagnosa, ttd, token;
    PhotoView foto;

    //untuk tanda tangan
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;

    String filePath;

    private static final String urlupdate = "https://dbradiologi.000webhostapp.com/api/users/updatedokter";
    private static final String url_token =  "https://dbradiologi.000webhostapp.com/api/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_room_dokter);

        tokenAdmin();

        //CloudinaryConfig.configCloudinary(getApplicationContext());

        etDiagnosa=findViewById(R.id.et_diagnosa);
        noRekam = findViewById(R.id.tv_norekmed);
        namaLengkap = findViewById(R.id.tv_nama);
        tangLahir = findViewById(R.id.tv_tgllahir);
        gender = findViewById(R.id.tv_gender);
        foto = findViewById(R.id.photo_viiew);

        norekaM = getIntent().getStringExtra("norekam");
        namaLengkaP = getIntent().getStringExtra("namalengkap");
        tangLahiR = getIntent().getStringExtra("tanggalahir");
        gendeR = getIntent().getStringExtra("gender");
        gambaR = getIntent().getStringExtra("gambar");

        noRekam.setText(norekaM);
        namaLengkap.setText(namaLengkaP);
        tangLahir.setText(tangLahiR);
        gender.setText(gendeR);

        Log.i("regina", gambaR);

        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(gambaR).into(foto);

        //untuk tanda tangan
        mSignaturePad= (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //Toast.makeText(Ttd.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton=(Button) findViewById(R.id.clear_button);
        mSaveButton=(Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

                if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(RoomDokter.this, "Signature saved into teh Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RoomDokter.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
                if (addSvgSignatureToGallery(mSignaturePad.getSignatureSvg())) {
                    Toast.makeText(RoomDokter.this, "SVG Signature save into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RoomDokter.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //untuk tanda tangan
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(RoomDokter.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir (String albumName) {
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
            File photo = new File(getAlbumStorageDir("Tdt"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
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
        RoomDokter.this.sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        Log.i("regina", signatureSvg);
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
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

    public static void verifyStoragePermissions(RoomDokter activity) {
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
                //mText.setText("image URL: "+resultData.get("url").toString());
                Log.i("regina", "sukses");
                Log.i("regina", resultData.get("url").toString());
                ttd = resultData.get("url").toString();
                updateData();
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

    private void updateData() {
        diagnosa = etDiagnosa.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdate,
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
                        Intent intent = new Intent(RoomDokter.this, DataDokter.class);
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
                params.put("norekam", norekaM);
                params.put("diagnosa", diagnosa);
                params.put("ttd", ttd);
                params.put("status", "1");
                params.put("token_tujuan", token);
                params.put("title", "Data Pasien Baru");
                params.put("message", "Dokter mengirimkan data yang telah dilengkapi");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RoomDokter.this);
        requestQueue.add(stringRequest);
    }

    private void tokenAdmin() {
        StringRequest request = new StringRequest(Request.Method.GET, url_token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        if (array.getJSONObject(i).getString("role").equals("admin")) {
                            token = array.getJSONObject(i).getString("token");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("regina", error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}