package com.example.radiologi.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.DataDokterPagerAdapter;
import com.example.radiologi.LoginActivity;
import com.example.radiologi.R;
import com.example.radiologi.SharedPreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataDokterActivity extends AppCompatActivity {

    ImageView btnLogout;

    String url_cek = "https://dbradiologi.000webhostapp.com/api/users/cektoken";
    String nip, msg, tokenLama, token;


    //untuk cloudinary
    /*Map config = new HashMap();

    private void configCloudinary() {
        config.put("cloud_name", "droykx53s");
        config.put("api_key", "425598729726584");
        config.put("api_secret", "R2Ge0Xhwg_PiWyZkxKwuRz6v58o");
        MediaManager.init(getApplicationContext(), config);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_dokter);

        tokenLama = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "token");
        nip = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "nip");
        btnLogout = findViewById(R.id.iv_logout);
        final TabLayout tabLayout = findViewById(R.id.tab_dokter);
        final ViewPager viewPager = findViewById(R.id.dokter_pager);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("regina", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                token = task.getResult();
                msg = getString(R.string.msg_token_fmt, token);
                Log.d("regina", token);

                cekToken();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        DataDokterPagerAdapter pagerAdapter = new DataDokterPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*SwipeRefresh = findViewById(R.id.swipe);
        SwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SwipeRefresh.setRefreshing(false);

                        dataDokterReq();
                    }
                },4000);
            }
        });

        nip = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "nip");
        adapterDokter = new AdapterDokter(getApplicationContext());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdokter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterDokter);
        adapterDokter.setOnClickListener(new AdapterDokter.OnItemClickListener() {
            @Override
            public void onItemClick(ListitemDokter listitemDokter) {
                if (listitemDokter.getStatus().equals("0")) {
                    Intent intent = new Intent(DataDokter.this, RoomDokter.class);
                    intent.putExtra("norekam", listitemDokter.getNoRekam());
                    intent.putExtra("namalengkap", listitemDokter.getNamaLengkap());
                    intent.putExtra("tanggalahir", listitemDokter.getTangLahir());
                    Log.i("regina", listitemDokter.getTangLahir());
                    intent.putExtra("gender", listitemDokter.getGender());
                    intent.putExtra("gambar", listitemDokter.getGambar());
                    startActivity(intent);
                } else {
                    Intent intentSudahBaca = new Intent(getApplicationContext(), TerimaAdmin.class);
                    intentSudahBaca.putExtra("noregis", listitemDokter.getNoRegis());
                    intentSudahBaca.putExtra("norekam", listitemDokter.getNoRekam());
                    intentSudahBaca.putExtra("namalengkap", listitemDokter.getNamaLengkap());
                    intentSudahBaca.putExtra("tanggalahir", listitemDokter.getTangLahir());
                    intentSudahBaca.putExtra("gender", listitemDokter.getGender());
                    intentSudahBaca.putExtra("gambar", listitemDokter.getGambar());
                    intentSudahBaca.putExtra("untuk", "dokter");
                    intentSudahBaca.putExtra("diagnosa", listitemDokter.getDiagnosa());
                    intentSudahBaca.putExtra("tdt", listitemDokter.getTdt());
                    intentSudahBaca.putExtra("status", listitemDokter.getStatus());
                    startActivity(intentSudahBaca);
                }
            }
        });

        dokterList = new ArrayList<>();

        dataDokterReq();*/
    }

    /*public void dataDokterReq() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_dokter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("regina", response);
                        try {
                            JSONObject objectResponse = new JSONObject(response);
                            JSONArray array = objectResponse.getJSONArray("data");
                            adapterDokter.clear();
                            for (int i = 0; i < array.length(); i++) {
                                ListitemDokter modelDokter = new ListitemDokter();
                                modelDokter.setNoRegis(array.getJSONObject(i).optString("noregis"));
                                modelDokter.setNoRekam(array.getJSONObject(i).optString("norekam"));
                                modelDokter.setNamaLengkap(array.getJSONObject(i).optString("namapasien"));
                                modelDokter.setTangLahir(array.getJSONObject(i).optString("tanglahir"));
                                modelDokter.setGender(array.getJSONObject(i).optString("gender"));
                                modelDokter.setGambar(array.getJSONObject(i).optString("gambar"));
                                modelDokter.setStatus(array.getJSONObject(i).optString("status"));
                                modelDokter.setDiagnosa(array.getJSONObject(i).optString("diagnosa"));
                                modelDokter.setTdt(array.getJSONObject(i).optString("ttd"));
                                adapterDokter.add(modelDokter);
                            }
                            adapterDokter.addAll(dokterList);
                            adapterDokter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Regina", String.valueOf(error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nip", nip);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }*/

    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Keluar dari aplikasi?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "islogin", false );
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void cekToken() {
        StringRequest request = new StringRequest(Request.Method.POST, url_cek, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("regina token", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("text").equals("sukses")) {
                        SharedPreferenceManager.savesStringPreferences(getApplicationContext(), "token", msg);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("regina", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nip", nip);
                param.put("token", token);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}