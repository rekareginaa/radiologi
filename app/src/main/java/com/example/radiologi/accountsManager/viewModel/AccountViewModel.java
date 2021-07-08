package com.example.radiologi.accountsManager.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.radiologi.data.dataSource.remote.response.LoginResponse;
import com.example.radiologi.data.repository.Repository;
import com.example.radiologi.utils.Event;
import com.example.radiologi.utils.vo.Resource;

import java.util.HashMap;
import java.util.Map;

import static com.example.radiologi.utils.Constants.NIP;
import static com.example.radiologi.utils.Constants.PASSWORD;

public class AccountViewModel extends ViewModel {

    private Repository.AccountRepository repository;

    private final MutableLiveData<Map<String, String>> parameter = new MutableLiveData<>();

    public AccountViewModel(Repository.AccountRepository repository) {
        this.repository = repository;
    }

    public void setParameter(String...params){
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put(NIP, params[0]);
        mapParam.put(PASSWORD, params[1]);

        this.parameter.setValue(mapParam);
    }

    public LiveData<Event<Resource<LoginResponse>>> getUserData = Transformations.switchMap(parameter, mParams ->
            repository.loginUser(mParams)
    );
}
