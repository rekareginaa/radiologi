package com.example.radiologi.admin.home.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.radiologi.R;
import com.example.radiologi.admin.viewModel.AdminViewModel;
import com.example.radiologi.admin.viewModel.AdminViewModelFactory;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.databinding.FragmentDataAdminBaruBinding;

import java.util.ArrayList;
import java.util.List;

public class DataAdminBaruFragment extends Fragment {
    String nip;
    ArrayList<String> listRegis = new ArrayList<>();
    AdapterAdmin adapterAdmin;

    ProgressDialog progressDialog;
    private FragmentDataAdminBaruBinding binding;
    private AdminViewModel viewModel;

    public DataAdminBaruFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDataAdminBaruBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdminViewModelFactory factory = AdminViewModelFactory.getInstance(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(AdminViewModel.class);

        nip = SharedPreferenceManager.getStringPreferences(getContext(), "nip");

        if (nip != null){
            viewModel.setParameters(nip, "0");
        }

        binding.swipeAdminDataBaru.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        binding.swipeAdminDataBaru.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            binding.swipeAdminDataBaru.setRefreshing(false);
            viewModel.setParameters(nip, "0");
        },4000));

        adapterAdmin = new AdapterAdmin(getContext(), 0);
        binding.recyclerAdminDataBaru.setHasFixedSize(true);
        binding.recyclerAdminDataBaru.setLayoutManager(new LinearLayoutManager(getContext()));
        observeResult();
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
        binding.recyclerAdminDataBaru.setVisibility(View.GONE);
    }

    private void showData(List<ItemAdminEntity> adminEntities){
        if (adminEntities != null){
            binding.teksKosong.setVisibility(View.GONE);
            binding.recyclerAdminDataBaru.setVisibility(View.VISIBLE);
            adapterAdmin.clear();
            adapterAdmin.addAll(adminEntities);
            adapterAdmin.notifyDataSetChanged();

            for (int i = 0; i<adminEntities.size(); i++){
                listRegis.add(adminEntities.get(i).getNoregis());
            }
        }
    }
}