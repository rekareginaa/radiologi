package com.example.radiologi;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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

    private List<ListitemAdmin> adminList;
    String nip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_admin);

        nip = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "nip");
        adapterAdmin = new AdapterAdmin(getApplicationContext());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterAdmin);
        adapterAdmin.setOnClickListener(new AdapterAdmin.OnItemClickListener() {
            @Override
            public void onItemClick(ListitemAdmin listitemAdmin) {
                Intent intent = new Intent(DataAdmin.this, TerimaAdmin.class);
                startActivity(intent);
            }
        });

        adminList = new ArrayList<>();

//        adminRequest();
        dataAdminReq();


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
        StringRequest request = new StringRequest(Request.Method.POST, url_admin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objectResponse = new JSONObject(response);
                            JSONArray array = objectResponse.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                ListitemAdmin modelAdmin = new ListitemAdmin();
                                modelAdmin.setNoRekam(array.getJSONObject(i).optString("norekam"));
                                modelAdmin.setNamaLengkap(array.getJSONObject(i).optString("namapasien"));
                                adminList.add(modelAdmin);
                            }
                            adapterAdmin.addAll(adminList);
                            adapterAdmin.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.daftar) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (item.getItemId() == R.id.keluar) {
            startActivity(new Intent(this, LogoutActivity.class));
        }

        return true;
    }
}