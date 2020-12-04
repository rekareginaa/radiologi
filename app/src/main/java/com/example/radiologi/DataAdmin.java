package com.example.radiologi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAdmin extends AppCompatActivity {

    String url_admin = "https://dbradiologi.000webhostapp.com/api/users/admindata";

    AdapterAdmin adapterAdmin;
    TextView kosong;
    RecyclerView recyclerView;
    ImageButton imageButton;

    private SwipeRefreshLayout SwipeRefreshAdmin;

    private List<ListitemAdmin> adminList;
    String nip;

    ArrayList listRegis;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_admin);

        listRegis = new ArrayList<>();

        SwipeRefreshAdmin = findViewById(R.id.swipe_admin);
        SwipeRefreshAdmin.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        SwipeRefreshAdmin.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SwipeRefreshAdmin.setRefreshing(false);

                        dataAdminReq();
                    }
                },4000);
            }
        });

        nip = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "nip");
        Log.i("regina", nip);
        adapterAdmin = new AdapterAdmin(getApplicationContext());

        kosong = findViewById(R.id.teks_kosong);
        imageButton = findViewById(R.id.option);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout relativeLayout = findViewById(R.id.relative_layout);
                relativeLayout.setVisibility(View.VISIBLE);
                FragmentAdmin fragmentAdmin = new FragmentAdmin();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.relative_layout, fragmentAdmin, FragmentAdmin.class.getSimpleName()).addToBackStack(null).commit();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterAdmin);
        adapterAdmin.setOnClickListener(new AdapterAdmin.OnItemClickListener() {
            @Override
            public void onItemClick(ListitemAdmin listitemAdmin) {
                Intent intent = new Intent(DataAdmin.this, TerimaAdmin.class);
                intent.putExtra("noregis", listitemAdmin.getNoRegis());
                intent.putExtra("norekam", listitemAdmin.getNoRekam());
                intent.putExtra("namalengkap", listitemAdmin.getNamaLengkap());
                intent.putExtra("tanggalahir", listitemAdmin.getTangLahir());
                intent.putExtra("gender", listitemAdmin.getGender());
                intent.putExtra("gambar", listitemAdmin.getGambar());
                intent.putExtra("untuk", "admin");
                intent.putExtra("diagnosa", listitemAdmin.getDiagnosa());
                intent.putExtra("tdt", listitemAdmin.getTdt());
                startActivity(intent);
            }
        });

        adminList = new ArrayList<>();
        dataAdminReq();

//        adminRequest();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataAdmin.this, RoomAdmin.class);
                intent.putExtra("regis", listRegis);
                startActivity(intent);
            }
        });
    }

    public void dataAdminReq() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        adminList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url_admin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("regina", response);
                        try {
                            JSONObject objectResponse = new JSONObject(response);
                            String objek = objectResponse.getString("status");
                            Log.i("regina", objek);
                            if (objectResponse.getString("status").equals("kosong")) {
                                Log.i("regina", "kosong");
                                kosong.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else if (objectResponse.getString("status").equals("sukses")){
                                Log.i("regina", "tidak kosong");
                                kosong.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                JSONArray array = objectResponse.getJSONArray("data");
                                adapterAdmin.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    ListitemAdmin modelAdmin = new ListitemAdmin();
                                    modelAdmin.setNoRegis(array.getJSONObject(i).optString("noregis"));
                                    modelAdmin.setNoRekam(array.getJSONObject(i).optString("norekam"));
                                    modelAdmin.setNamaLengkap(array.getJSONObject(i).optString("namapasien"));
                                    modelAdmin.setTangLahir(array.getJSONObject(i).optString("tanglahir"));
                                    modelAdmin.setGender(array.getJSONObject(i).optString("gender"));
                                    modelAdmin.setGambar(array.getJSONObject(i).optString("gambar"));
                                    modelAdmin.setStatus(array.getJSONObject(i).optString("status"));
                                    modelAdmin.setDiagnosa(array.getJSONObject(i).optString("diagnosa"));
                                    modelAdmin.setTdt(array.getJSONObject(i).optString("ttd"));
                                    adminList.add(modelAdmin);

//                                    ModelRegis modelRegis = new ModelRegis();
//                                    modelRegis.setNoRegis(array.getJSONObject(i).optString("noregis"));
                                    String regis = array.getJSONObject(i).optString("noregis");
                                    listRegis.add(regis);
                                }
                                adapterAdmin.addAll(adminList);
                                adapterAdmin.notifyDataSetChanged();

                                /*SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(listRegis);
                                editor.putString("list", json);
                                editor.apply();
                                Log.i("regina", listRegis.toString());*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("regina", e.getMessage());
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
    }
}