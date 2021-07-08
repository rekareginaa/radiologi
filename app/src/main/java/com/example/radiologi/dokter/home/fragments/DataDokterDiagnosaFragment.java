package com.example.radiologi.dokter.home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.radiologi.R;
import com.example.radiologi.admin.DetailPasienActivity;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.databinding.FragmentDataDokterDiagnosaBinding;
import com.example.radiologi.dokter.viewModel.DoctorViewModel;
import com.example.radiologi.dokter.viewModel.DoctorViewModelFactory;

import java.util.List;

public class DataDokterDiagnosaFragment extends Fragment {
    ProgressDialog progressDialog;
    AdapterDokter adapterDokter;
    String nip;

    private FragmentDataDokterDiagnosaBinding binding;
    private DoctorViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDataDokterDiagnosaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        adapterDokter = new AdapterDokter(getContext());

        nip = SharedPreferenceManager.getStringPreferences(getContext(), "nip");

        DoctorViewModelFactory factory = DoctorViewModelFactory.getInstance(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(DoctorViewModel.class);

        if (nip != null){
            viewModel.setParameters(nip, "1");
        }

        binding.swipeDokterDiagnosa.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        binding.swipeDokterDiagnosa.setOnRefreshListener(() -> {
            binding.swipeDokterDiagnosa.setRefreshing(false);
            viewModel.setParameters(nip, "1");
        });

        binding.recyclerDokterDiagnosa.setHasFixedSize(true);
        binding.recyclerDokterDiagnosa.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerDokterDiagnosa.setAdapter(adapterDokter);
        adapterDokter.setOnClickListener(listitemDokter -> {
            Intent intentSudahBaca = new Intent(getContext(), DetailPasienActivity.class);
            intentSudahBaca.putExtra(DetailPasienActivity.EXTRA_DATA, listitemDokter);
            startActivity(intentSudahBaca);
        });

        observeResult();
    }

    private void observeResult(){
        viewModel.getDoctorData.observe(getViewLifecycleOwner(), result -> {
            switch (result.status){
                case LOADING:
                    showDialog();
                    break;
                case ERROR:
                    hideDialog();
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    hideDialog();
                    populateData(result.data);
                    break;
            }
        });
    }

    private void showDialog(){
        progressDialog.setTitle("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideDialog(){
        if (progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }

    private void populateData(List<ItemDoctorEntity> doctorEntities){
        if (doctorEntities != null){
            adapterDokter.clear();
            adapterDokter.addAll(doctorEntities);
            adapterDokter.notifyDataSetChanged();
        }
    }
}