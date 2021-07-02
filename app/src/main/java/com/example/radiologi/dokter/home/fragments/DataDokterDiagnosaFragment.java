package com.example.radiologi.dokter.home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.ListitemDokter;
import com.example.radiologi.R;
import com.example.radiologi.SharedPreferenceManager;
import com.example.radiologi.TerimaAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDokterDiagnosaFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshDokter;
    ProgressDialog progressDialog;
    private List<ListitemDokter> dokterDiagnosaList;

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
        View viewDokterDiagnosa = inflater.inflate(R.layout.fragment_data_dokter_diagnosa, container, false);

        adapterDokter = new AdapterDokter(getContext());
        nip = SharedPreferenceManager.getStringPreferences(getContext(), "nip");
        dokterDiagnosaList = new ArrayList<>();

        swipeRefreshDokter = viewDokterDiagnosa.findViewById(R.id.swipe_dokter_diagnosa);
        swipeRefreshDokter.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshDokter.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            swipeRefreshDokter.setRefreshing(false);
            dataDokterReq();
        },4000));

        RecyclerView recyclerView = viewDokterDiagnosa.findViewById(R.id.recycler_dokter_diagnosa);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterDokter);
        adapterDokter.setOnClickListener(listitemDokter -> {
            Intent intentSudahBaca = new Intent(getContext(), TerimaAdmin.class);
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
        });

        dataDokterReq();

        return viewDokterDiagnosa;
    }

    public void dataDokterReq() { //TODO move all request server to presenter layer

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_dokter,
                response -> {
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
                            if (modelDokter.getStatus().equals("1") || modelDokter.getStatus().equals("2")) {
                                adapterDokter.add(modelDokter);
                            }
                        }
                        adapterDokter.addAll(dokterDiagnosaList);
                        adapterDokter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }, error -> Log.i("Regina", String.valueOf(error))) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nip", nip);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}