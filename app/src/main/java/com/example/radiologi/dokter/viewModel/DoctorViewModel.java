package com.example.radiologi.dokter.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.radiologi.data.dataSource.remote.response.SimpleResponse;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.data.repository.Repository;
import com.example.radiologi.utils.vo.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.radiologi.utils.Constants.NIP;
import static com.example.radiologi.utils.Constants.PAGE;
import static com.example.radiologi.utils.Constants.STATUS;

public class DoctorViewModel extends ViewModel {
    private Repository.DoctorRepository repository;

    public DoctorViewModel(Repository.DoctorRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<HashMap<String, String>> parameters = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> paramUpdate = new MutableLiveData<>();
    private final MutableLiveData<String> paramLocal = new MutableLiveData<>();

    public void setParameters(String...params){
        HashMap<String, String> param = new HashMap<>();
        param.put(NIP, params[0]);
        param.put(STATUS, params[1]);
        param.put(PAGE, params[2]);
        parameters.setValue(param);
    }

    public void requestLocal(String status) {
        this.paramLocal.setValue(status);
    }

    public void setParameters(Map<String, String> params){
        this.paramUpdate.setValue(params);
    }

    public LiveData<Resource<List<ItemDoctorEntity>>> getNewDoctorData = Transformations.switchMap(parameters, result ->
            repository.getNewDoctorData(result.get(NIP), result.get(PAGE)));

    public LiveData<Resource<List<ItemDoctorEntity>>> getDoctorData = Transformations.switchMap(parameters, result ->
            repository.getDoctorData(result.get(NIP), result.get(PAGE)));

    public LiveData<List<ItemDoctorEntity>> getNewDoctorLocal = Transformations.switchMap(paramLocal, result -> repository.getNewDoctorData(result));

    public LiveData<List<ItemDoctorEntity>> getDoctorDataLocal = Transformations.switchMap(paramLocal, result -> repository.getDoctorData(result));

    public LiveData<Resource<SimpleResponse>> getResponse = Transformations.switchMap(paramUpdate, params->
            repository.getResponseUpdate(params));



}
