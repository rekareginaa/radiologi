package com.example.radiologi.dokter.home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.databinding.FragmentDataDokterBaruBinding;
import com.example.radiologi.dokter.formResponseData.FormResponseDataDokterActivity;
import com.example.radiologi.dokter.viewModel.DoctorViewModel;
import com.example.radiologi.dokter.viewModel.DoctorViewModelFactory;

import java.util.List;

public class DataDokterBaruFragment extends Fragment {
    ProgressDialog progressDialog;

    AdapterDokter adapterDokter;

    String nip;

    private FragmentDataDokterBaruBinding binding;
    private DoctorViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDataDokterBaruBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nip = SharedPreferenceManager.getStringPreferences(getContext(), "nip");
        adapterDokter = new AdapterDokter(requireContext());
        progressDialog = new ProgressDialog(requireContext());

        DoctorViewModelFactory factory = DoctorViewModelFactory.getInstance(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(DoctorViewModel.class);

        if (nip != null){
            viewModel.setParameters(nip, "0");
        }

        binding.swipeDokterDataBaru.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        binding.swipeDokterDataBaru.setOnRefreshListener(() -> {
            binding.swipeDokterDataBaru.setRefreshing(false);
            viewModel.setParameters(nip, "0");
        });

        binding.recyclerDokterDataBaru.setHasFixedSize(true);
        binding.recyclerDokterDataBaru.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerDokterDataBaru.setAdapter(adapterDokter);
        adapterDokter.setOnClickListener(listitemDokter -> {
            Log.d("DATA_", listitemDokter.toString());
            Intent intent = new Intent(getContext(), FormResponseDataDokterActivity.class);
            intent.putExtra(FormResponseDataDokterActivity.EXTRA_DATA, listitemDokter);
            startActivity(intent);
        });
        observeResult();
    }

    private void observeResult(){
        viewModel.getDoctorData.observe(getViewLifecycleOwner(), result ->{
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

    private void populateData(List<ItemDoctorEntity> doctorEntities){
        if (doctorEntities != null){
            adapterDokter.clear();
            adapterDokter.addAll(doctorEntities);
            adapterDokter.notifyDataSetChanged();
        }
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
}