package com.example.radiologi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDokter extends AppCompatActivity {

    String url_dokter = "https://dbradiologi.000webhostapp.com/api/users/dokterdata";

    AdapterDokter adapterDokter;

    private SwipeRefreshLayout SwipeRefresh;
    private List<ListitemDokter> dokterList;
    String nip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_dokter);

        SwipeRefresh = findViewById(R.id.swipe);
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
                Intent intent = new Intent(DataDokter.this, RoomDokter.class);
                intent.putExtra("norekam", listitemDokter.getNoRekam());
                intent.putExtra("namalengkap", listitemDokter.getNamaLengkap());
                intent.putExtra("tanggalahir", listitemDokter.getTangLahir());
                Log.i("regina", listitemDokter.getTangLahir());
                intent.putExtra("gender", listitemDokter.getGender());
                intent.putExtra("gambar", listitemDokter.getGambar());
                startActivity(intent);
            }
        });

        dokterList = new ArrayList<>();

        dataDokterReq();
    }

    public void dataDokterReq() {
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
                                modelDokter.setNoRekam(array.getJSONObject(i).optString("norekam"));
                                modelDokter.setNamaLengkap(array.getJSONObject(i).optString("namapasien"));
                                modelDokter.setTangLahir(array.getJSONObject(i).optString("tanglahir"));
                                modelDokter.setGender(array.getJSONObject(i).optString("gender"));
                                modelDokter.setGambar(array.getJSONObject(i).optString("gambar"));
                                adapterDokter.add(modelDokter);
                            }
                            adapterDokter.addAll(dokterList);
                            adapterDokter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
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