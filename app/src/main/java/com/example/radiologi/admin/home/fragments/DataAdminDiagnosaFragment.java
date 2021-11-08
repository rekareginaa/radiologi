package com.example.radiologi.admin.home.fragments;

import static com.example.radiologi.utils.rv.PaginationListener.PAGE_START;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.radiologi.utils.NetworkUtil;
import com.example.radiologi.utils.rv.PaginationListener;

import java.util.ArrayList;
import java.util.List;

public class DataAdminDiagnosaFragment extends Fragment {

    String nip;
    ArrayList<String> listRegis = new ArrayList<>();
    private ProgressDialog progressDialog;
    private FragmentDataAdminDiagnosaBinding binding;
    private AdminViewModel viewModel;
    private NewAdminAdapter adapterAdmin;
    private LinearLayoutManager linearLayoutManager;

    private int pagePosition = 0;
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
        // Inflate the layout for this fragment
        binding = FragmentDataAdminDiagnosaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nip = SharedPreferenceManager.getStringPreferences(getContext(), "nip");

        AdminViewModelFactory factory = AdminViewModelFactory.getInstance(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(AdminViewModel.class);

        initRecyclerView();

        if (nip != null){
            requestIfNetworkAvailable();

            binding.swipeAdminTerdiagnosa.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
            binding.swipeAdminTerdiagnosa.setOnRefreshListener(() -> {
                binding.swipeAdminTerdiagnosa.setRefreshing(false);
                currentPage = PAGE_START;
                isLastPage = false;
                pagePosition = 0;
                adapterAdmin.clear();
                requestIfNetworkAvailable();
            });
            observeLocalResult();
            /*if (!NetworkUtil.isNetworkAvailable(requireContext())){

            }else{
                observeResult();
            }*/
        }
    }

    private void initRecyclerView(){
        adapterAdmin = new NewAdminAdapter(new ArrayList<>(), 0);
        linearLayoutManager = new LinearLayoutManager(requireContext());

        binding.recyclerAdminTerdiagnosa.setHasFixedSize(true);
        binding.recyclerAdminTerdiagnosa.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerAdminTerdiagnosa.setAdapter(adapterAdmin);

        adapterAdmin.setOnClickListener(listitemAdmin -> {
            Intent intent = new Intent(getContext(), DetailPasienActivity.class);
            intent.putExtra(DetailPasienActivity.EXTRA_DATA, listitemAdmin);
            startActivity(intent);
        });

        binding.recyclerAdminTerdiagnosa.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                pagePosition = pagePosition+10;
                viewModel.setParameters(nip, "1", String.valueOf(currentPage), String.valueOf(pagePosition));
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

    private void requestIfNetworkAvailable(){
        viewModel.requestLocal("1");
        /*if (NetworkUtil.isNetworkAvailable(requireContext())){
            viewModel.setParameters(nip, "1", "1", "0");
        }else{
            viewModel.requestLocal("1");
        }*/
    }

    private void observeLocalResult(){
        viewModel.getDataAdminLocal.observe(getViewLifecycleOwner(), this::showDataLocal);
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

            binding.teksKosong.setVisibility(View.GONE);
            binding.recyclerAdminTerdiagnosa.setVisibility(View.VISIBLE);
            if (currentPage != PAGE_START) adapterAdmin.removeLoading();
            binding.swipeAdminTerdiagnosa.setRefreshing(false);
            adapterAdmin.addItems(adminEntities);

            if (currentPage < totalPage){
                adapterAdmin.addLoading();
            }else{
                isLastPage = true;
            }
            isLoading = true;
        }
    }

    private void showDataLocal(List<ItemAdminEntity> adminEntities){
        if (adminEntities != null){
            binding.teksKosong.setVisibility(View.GONE);
            binding.recyclerAdminTerdiagnosa.setVisibility(View.VISIBLE);
            if (currentPage != PAGE_START) adapterAdmin.removeLoading();
            adapterAdmin.addItems(adminEntities);
            binding.swipeAdminTerdiagnosa.setRefreshing(false);
        }
    }
}