package com.example.radiologi.admin.home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.radiologi.R;
import com.example.radiologi.admin.DetailPasienActivity;
import com.example.radiologi.admin.viewModel.AdminViewModel;
import com.example.radiologi.admin.viewModel.AdminViewModelFactory;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.databinding.FragmentDataAdminDiagnosaBinding;

import java.util.ArrayList;
import java.util.List;

public class DataAdminDiagnosaFragment extends Fragment {

    String nip;
    ArrayList<String> listRegis;

    AdapterAdmin adapterAdmin;
    ProgressDialog progressDialog;
    private FragmentDataAdminDiagnosaBinding binding;
    private AdminViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDataAdminDiagnosaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listRegis = new ArrayList<>();
        nip = SharedPreferenceManager.getStringPreferences(getContext(), "nip");
        Log.i("regina", nip);

        AdminViewModelFactory factory = AdminViewModelFactory.getInstance(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(AdminViewModel.class);

        adapterAdmin = new AdapterAdmin(requireContext(), 1);
        binding.recyclerAdminTerdiagnosa.setHasFixedSize(true);
        binding.recyclerAdminTerdiagnosa.setLayoutManager(new LinearLayoutManager(getContext()));

        if (nip != null){
            viewModel.setParameters(nip, "1");
            binding.swipeAdminTerdiagnosa.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
            binding.swipeAdminTerdiagnosa.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
                binding.swipeAdminTerdiagnosa.setRefreshing(false);
                viewModel.setParameters(nip, "1");
            },4000));

            binding.recyclerAdminTerdiagnosa.setAdapter(adapterAdmin);
            adapterAdmin.setOnClickListener(listitemAdmin -> {
                Intent intent = new Intent(getContext(), DetailPasienActivity.class);
                intent.putExtra(DetailPasienActivity.EXTRA_DATA, listitemAdmin);
                startActivity(intent);
            });

            observeResult();
        }
    }

    private void observeResult(){
        viewModel.getAdminData.observe(getViewLifecycleOwner(), result -> {
            switch (result.status){
                case LOADING:
                    showProgressDialog();
                case ERROR:
                    hideProgressDialog();
                    final String msg = result.message;
                    if (msg != null){
                        if (msg.equals("kosong")){
                            emptyData();
                        }
                    }
                case SUCCESS:
                    showData(result.data);

            }
        });
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Mohon Tunggu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog(){
        progressDialog.dismiss();
    }

    private void emptyData(){
        binding.teksKosong.setVisibility(View.VISIBLE);
        binding.recyclerAdminTerdiagnosa.setVisibility(View.GONE);
    }

    private void showData(List<ItemAdminEntity> adminEntities){
        if (adminEntities != null){
            Log.d("DATA_", adminEntities.toString());
            binding.teksKosong.setVisibility(View.GONE);
            binding.recyclerAdminTerdiagnosa.setVisibility(View.VISIBLE);
            adapterAdmin.clear();
            adapterAdmin.addAll(adminEntities);
            adapterAdmin.notifyDataSetChanged();

            for (int i = 0; i<adminEntities.size(); i++){
                listRegis.add(adminEntities.get(i).getNoregis());
            }
        }
    }
}