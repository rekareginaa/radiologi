package com.example.radiologi.dokter.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.data.repository.Repository;
import com.example.radiologi.utils.vo.Resource;

import java.util.HashMap;
import java.util.List;

import static com.example.radiologi.utils.Constants.NIP;
import static com.example.radiologi.utils.Constants.STATUS;

public class DoctorViewModel extends ViewModel {
    private Repository.DoctorRepository repository;

    public DoctorViewModel(Repository.DoctorRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<HashMap<String, String>> parameters = new MutableLiveData<>();

    public void setParameters(String...params){
        HashMap<String, String> param = new HashMap<>();
        param.put(NIP, params[0]);
        param.put(STATUS, params[1]);

        parameters.setValue(param);
    }

    public LiveData<Resource<List<ItemDoctorEntity>>> getDoctorData = Transformations.switchMap(parameters, result ->
            repository.getDoctorData(result.get(NIP), result.get(STATUS)));
}
