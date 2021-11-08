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
import com.example.radiologi.admin.formAddData.FormAddDataActivity;
import com.example.radiologi.admin.viewModel.AdminViewModel;
import com.example.radiologi.admin.viewModel.AdminViewModelFactory;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.databinding.FragmentDataAdminBaruBinding;
import com.example.radiologi.utils.NetworkUtil;
import com.example.radiologi.utils.rv.PaginationListener;

import java.util.ArrayList;
import java.util.List;

public class DataAdminBaruFragment extends Fragment {
    String nip;
    ArrayList<String> listRegis = new ArrayList<>();

    private NewAdminAdapter newAdminAdapter;
    private ProgressDialog progressDialog;
    private FragmentDataAdminBaruBinding binding;
    private AdminViewModel viewModel;
    private LinearLayoutManager linearLayoutManager;

    private int pagePosition = 0;
    private int currentPage = PAGE_START;
    private int totalPage = 4;
    private boolean isLastPage = false;
    private boolean isLoading = false;

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
            requestIfNetworkAvailable();
        }

        binding.swipeAdminDataBaru.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        binding.swipeAdminDataBaru.setOnRefreshListener(() -> {
            binding.swipeAdminDataBaru.setRefreshing(false);
            currentPage = PAGE_START;
            isLastPage = false;
            pagePosition = 0;
            newAdminAdapter.clear();
            requestIfNetworkAvailable();
        });

        initRecyclerView();

        if (!NetworkUtil.isNetworkAvailable(requireContext())){
            observeLocalResult();
        }else{
            observeResult();
        }

        binding.fab.setOnClickListener(views ->{
            Intent intent = new Intent(requireContext(), FormAddDataActivity.class);
            intent.putExtra(FormAddDataActivity.EXTRA_LIST_REGIS, listRegis);
            startActivity(intent);
        });
    }

    private void requestIfNetworkAvailable(){
        if (NetworkUtil.isNetworkAvailable(requireContext())){
            viewModel.setParameters(nip, "0", "1", "0");
        }else{
            viewModel.requestLocal("0");
        }
    }

    private void initRecyclerView(){
        newAdminAdapter = new NewAdminAdapter(new ArrayList<>(), 0);
        linearLayoutManager = new LinearLayoutManager(getContext());

        binding.recyclerAdminDataBaru.setAdapter(newAdminAdapter);
        binding.recyclerAdminDataBaru.setHasFixedSize(true);
        binding.recyclerAdminDataBaru.setLayoutManager(linearLayoutManager);

        newAdminAdapter.setOnClickListener(itemAdmin -> {
            Intent intent = new Intent(requireContext(), DetailPasienActivity.class);
            intent.putExtra(DetailPasienActivity.EXTRA_DATA, itemAdmin);
            startActivity(intent);
        });

        binding.recyclerAdminDataBaru.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                pagePosition = pagePosition+10;
                viewModel.setParameters(nip, "0", String.valueOf(currentPage), String.valueOf(pagePosition));
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
        viewModel.getNewAdminData.observe(getViewLifecycleOwner(), result -> {
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

    private void observeLocalResult(){
        viewModel.getDataLocal.observe(getViewLifecycleOwner(), this::showDataLocal);
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
            if (currentPage != PAGE_START) newAdminAdapter.removeLoading();
            newAdminAdapter.addItems(adminEntities);
            binding.swipeAdminDataBaru.setRefreshing(false);

            //check weather is last page or not
            if (currentPage < totalPage){
                newAdminAdapter.addLoading();
            }else{
                isLastPage = true;
            }
            isLoading = false;
        }
    }

    private void showDataLocal(List<ItemAdminEntity> adminEntities){
        if (adminEntities != null){
            binding.teksKosong.setVisibility(View.GONE);
            binding.recyclerAdminDataBaru.setVisibility(View.VISIBLE);
            if (currentPage != PAGE_START) newAdminAdapter.removeLoading();
            newAdminAdapter.addItems(adminEntities);
            binding.swipeAdminDataBaru.setRefreshing(false);
        }
    }

}