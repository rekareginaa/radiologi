package com.example.radiologi;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

    private SwipeRefreshLayout SwipeRefreshAdmin;

    private List<ListitemAdmin> adminList;
    String nip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_admin);

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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterAdmin);
        adapterAdmin.setOnClickListener(new AdapterAdmin.OnItemClickListener() {
            @Override
            public void onItemClick(ListitemAdmin listitemAdmin) {
                Intent intent = new Intent(DataAdmin.this, TerimaAdmin.class);
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
                startActivity(intent);
            }
        });
    }

    public void dataAdminReq() {
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
                                    modelAdmin.setNoRekam(array.getJSONObject(i).optString("norekam"));
                                    modelAdmin.setNamaLengkap(array.getJSONObject(i).optString("namapasien"));
                                    modelAdmin.setTangLahir(array.getJSONObject(i).optString("tanglahir"));
                                    modelAdmin.setGender(array.getJSONObject(i).optString("gender"));
                                    modelAdmin.setGambar(array.getJSONObject(i).optString("gambar"));
                                    modelAdmin.setStatus(array.getJSONObject(i).optString("status"));
                                    modelAdmin.setDiagnosa(array.getJSONObject(i).optString("diagnosa"));
                                    modelAdmin.setTdt(array.getJSONObject(i).optString("ttd"));
                                    adminList.add(modelAdmin);
                                }
                                adapterAdmin.addAll(adminList);
                                adapterAdmin.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("regina", e.getMessage());
                        }
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