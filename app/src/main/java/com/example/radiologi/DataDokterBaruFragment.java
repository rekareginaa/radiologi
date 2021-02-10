package com.example.radiologi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class DataDokterBaruFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshDokter;
    private List<ListitemDokter> dokterList;
    ProgressDialog progressDialog;

    AdapterDokter adapterDokter;

    String url_dokter = "https://dbradiologi.000webhostapp.com/api/users/dokterdata";
    String nip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewDataDokterBaru = inflater.inflate(R.layout.fragment_data_dokter_baru, container, false);

        nip = SharedPreferenceManager.getStringPreferences(getContext(), "nip");
        adapterDokter = new AdapterDokter(getContext());
        dokterList = new ArrayList<>();

        swipeRefreshDokter = viewDataDokterBaru.findViewById(R.id.swipe_dokter_data_baru);
        swipeRefreshDokter.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshDokter.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshDokter.setRefreshing(false);
                        dataDokterReq();
                    }
                },4000);
            }
        });

        RecyclerView recyclerView = (RecyclerView) viewDataDokterBaru.findViewById(R.id.recycler_dokter_data_baru);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterDokter);
        adapterDokter.setOnClickListener(new AdapterDokter.OnItemClickListener() {
            @Override
            public void onItemClick(ListitemDokter listitemDokter) {
                Intent intent = new Intent(getContext(), RoomDokter.class);
                intent.putExtra("norekam", listitemDokter.getNoRekam());
                intent.putExtra("namalengkap", listitemDokter.getNamaLengkap());
                intent.putExtra("tanggalahir", listitemDokter.getTangLahir());
                Log.i("regina", listitemDokter.getTangLahir());
                intent.putExtra("gender", listitemDokter.getGender());
                intent.putExtra("gambar", listitemDokter.getGambar());
                startActivity(intent);
            }
        });

        dataDokterReq();

        return viewDataDokterBaru;
    }

    public void dataDokterReq() {

        progressDialog = new ProgressDialog(getContext());
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
                                if (modelDokter.getStatus().equals("0")){
                                    adapterDokter.add(modelDokter);
                                }
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}