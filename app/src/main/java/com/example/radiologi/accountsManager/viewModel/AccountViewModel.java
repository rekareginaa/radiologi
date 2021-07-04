package com.example.radiologi.accountsManager.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.radiologi.data.dataSource.remote.response.DataItemLogin;
import com.example.radiologi.data.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class AccountViewModel extends ViewModel {

    private Repository.AccountRepository repository;

    private MutableLiveData<Map<String, String>> parameter = new MutableLiveData<>();

    public AccountViewModel(Repository.AccountRepository repository) {
        this.repository = repository;
    }

    public void setParameter(String...params){
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("nip", params[0]);
        mapParam.put("password", params[1]);

        this.parameter.setValue(mapParam);
    }

    public LiveData<DataItemLogin> getUserData = Transformations.switchMap(parameter, mParams ->
            repository.loginUser(mParams)
    );
}
