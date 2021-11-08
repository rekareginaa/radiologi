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
import com.example.radiologi.admin.DetailPasienActivity;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.databinding.FragmentDataDokterDiagnosaBinding;
import com.example.radiologi.dokter.viewModel.DoctorViewModel;
import com.example.radiologi.dokter.viewModel.DoctorViewModelFactory;
import com.example.radiologi.utils.NetworkUtil;
import com.example.radiologi.utils.rv.PaginationListener;

import java.util.ArrayList;
import java.util.List;

public class DataDokterDiagnosaFragment extends Fragment {

    String nip;

    private ProgressDialog progressDialog;
    private FragmentDataDokterDiagnosaBinding binding;
    private DoctorViewModel viewModel;
    private NewAdapterDokter adapterDokter;
    private LinearLayoutManager layoutManager;

    private int currentPage = PAGE_START;
    private int totalPage = 2;
    private boolean isLastPage = false;
    private boolean isLoading = false;

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

        nip = SharedPreferenceManager.getStringPreferences(getContext(), "nip");

        DoctorViewModelFactory factory = DoctorViewModelFactory.getInstance(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(DoctorViewModel.class);

        if (nip != null){
            requestIfNetworkAvailable();
        }

        initRecyclerView();

        binding.swipeDokterDiagnosa.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        binding.swipeDokterDiagnosa.setOnRefreshListener(() -> {
            binding.swipeDokterDiagnosa.setRefreshing(false);
            currentPage = PAGE_START;
            isLastPage = false;
            adapterDokter.clear();
            requestIfNetworkAvailable();
        });

        observeLocalData();
        /*if (!NetworkUtil.isNetworkAvailable(requireContext())){
            observeLocalData();
        }else{
            observeResult();
        }*/

        observeResult();
    }

    private void requestIfNetworkAvailable(){
        viewModel.requestLocal("1");
        /*if (NetworkUtil.isNetworkAvailable(requireContext())){
            viewModel.setParameters(nip, "1", "1");
        }else{
            viewModel.requestLocal("1");
        }*/
    }

    private void initRecyclerView() {
        adapterDokter = new NewAdapterDokter(new ArrayList<>(), 1);
        layoutManager = new LinearLayoutManager(requireContext());

        binding.recyclerDokterDiagnosa.setHasFixedSize(true);
        binding.recyclerDokterDiagnosa.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerDokterDiagnosa.setAdapter(adapterDokter);
        adapterDokter.setOnClickListener(listitemDokter -> {
            Intent intentSudahBaca = new Intent(getContext(), DetailPasienActivity.class);
            intentSudahBaca.putExtra(DetailPasienActivity.EXTRA_DATA, listitemDokter);
            startActivity(intentSudahBaca);
        });

        binding.recyclerDokterDiagnosa.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = false;
                currentPage++;
                viewModel.setParameters(nip, "1", String.valueOf(currentPage));
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

    private void observeLocalData(){
        viewModel.getDoctorDataLocal.observe(getViewLifecycleOwner(), this::populateDataLocal);
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
            if (currentPage != PAGE_START) adapterDokter.removeLoading();
            adapterDokter.addItems(doctorEntities);
            binding.swipeDokterDiagnosa.setRefreshing(false);

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
            binding.swipeDokterDiagnosa.setRefreshing(false);
        }
    }
}