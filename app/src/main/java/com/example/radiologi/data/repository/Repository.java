package com.example.radiologi.data.repository;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.dataSource.remote.response.LoginResponse;
import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.utils.Event;
import com.example.radiologi.utils.vo.Resource;

import java.util.List;
import java.util.Map;

public interface Repository {
    interface AccountRepository{
        LiveData<Event<Resource<LoginResponse>>> loginUser(Map< String, String> params);
    }

    interface AdminRepository{
        LiveData<Resource<List<ItemAdminEntity>>> getAdminData(String nip, String status);
    }

    interface DoctorRepository{
        LiveData<Resource<List<ItemDoctorEntity>>> getDoctorData(String nip, String status);
    }
}
