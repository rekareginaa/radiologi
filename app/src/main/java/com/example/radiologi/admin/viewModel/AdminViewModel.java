package com.example.radiologi.admin.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.data.repository.Repository;
import com.example.radiologi.utils.vo.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.radiologi.utils.Constants.NIP;
import static com.example.radiologi.utils.Constants.STATUS;

public class AdminViewModel extends ViewModel {
    private Repository.AdminRepository repository;

    public AdminViewModel(Repository.AdminRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<HashMap<String, String>> parameters = new MutableLiveData<>();

    public void setParameters(String...params){
        HashMap<String, String> param = new HashMap<>();
        param.put(NIP, params[0]);
        param.put(STATUS, params[1]);

        parameters.setValue(param);
    }

    public LiveData<Resource<List<ItemAdminEntity>>> getAdminData = Transformations.switchMap(parameters, result ->
        repository.getAdminData(result.get(NIP), result.get(STATUS))
    );
}
