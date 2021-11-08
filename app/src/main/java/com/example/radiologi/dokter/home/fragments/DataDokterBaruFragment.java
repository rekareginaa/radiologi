package com.example.radiologi.dokter.home.fragments;

import static com.example.radiologi.utils.rv.PaginationListener.PAGE_START;

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
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.databinding.FragmentDataDokterBaruBinding;
import com.example.radiologi.dokter.formResponseData.FormResponseDataDokterActivity;
import com.example.radiologi.dokter.viewModel.DoctorViewModel;
import com.example.radiologi.dokter.viewModel.DoctorViewModelFactory;
import com.example.radiologi.utils.NetworkUtil;
import com.example.radiologi.utils.rv.PaginationListener;

import java.util.ArrayList;
import java.util.List;

public class DataDokterBaruFragment extends Fragment {

    String nip;

    private FragmentDataDokterBaruBinding binding;
    private DoctorViewModel viewModel;

    private NewAdapterDokter adapterDokter;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;

    private int currentPage = PAGE_START;
    private int totalPage = 4;
    private boolean isLastPage = false;
    private boolean isLoading = false;

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
        progressDialog = new ProgressDialog(requireContext());

        DoctorViewModelFactory factory = DoctorViewModelFactory.getInstance(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(DoctorViewModel.class);

        if (nip != null){
            requestIfNetworkAvailable();
        }

        initRecyclerView();

        binding.swipeDokterDataBaru.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        binding.swipeDokterDataBaru.setOnRefreshListener(() -> {
            binding.swipeDokterDataBaru.setRefreshing(false);
            currentPage = PAGE_START;
            isLastPage = false;
            adapterDokter.clear();
            requestIfNetworkAvailable();
        });

        if (!NetworkUtil.isNetworkAvailable(requireContext())){
            observeResultLocal();
        }else{
            observeResult();
        }

        observeResult();
    }

    private void requestIfNetworkAvailable(){
        if (NetworkUtil.isNetworkAvailable(requireContext())){
            viewModel.setParameters(nip, "0", "1");
        }else{
            viewModel.requestLocal("0");
        }
    }

    private void initRecyclerView(){
        adapterDokter = new NewAdapterDokter(new ArrayList<>(), 0);
        layoutManager = new LinearLayoutManager(requireContext());

        binding.recyclerDokterDataBaru.setHasFixedSize(true);
        binding.recyclerDokterDataBaru.setLayoutManager(layoutManager);
        binding.recyclerDokterDataBaru.setAdapter(adapterDokter);
        adapterDokter.setOnClickListener(listitemDokter -> {
            Intent intent = new Intent(getContext(), FormResponseDataDokterActivity.class);
            intent.putExtra(FormResponseDataDokterActivity.EXTRA_DATA, listitemDokter);
            startActivity(intent);
        });

        binding.recyclerDokterDataBaru.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                viewModel.setParameters(nip, "0", String.valueOf(currentPage));
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void observeResult(){
        viewModel.getNewDoctorData.observe(getViewLifecycleOwner(), result ->{
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

    private void observeResultLocal(){
        viewModel.getNewDoctorLocal.observe(getViewLifecycleOwner(), this::populateDataLocal);
    }

    private void populateData(List<ItemDoctorEntity> doctorEntities){
        if (doctorEntities != null){
            if (currentPage != PAGE_START) adapterDokter.removeLoading();
            adapterDokter.addItems(doctorEntities);
            binding.swipeDokterDataBaru.setRefreshing(false);

            if (currentPage < totalPage){
                adapterDokter.addLoading();
            }else{
                isLastPage = true;
            }
            isLoading = false;
        }
    }

    private void populateDataLocal(List<ItemDoctorEntity> doctorEntities){
        if (doctorEntities != null){
            if (currentPage != PAGE_START) adapterDokter.removeLoading();
            adapterDokter.addItems(doctorEntities);
            binding.swipeDokterDataBaru.setRefreshing(false);
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