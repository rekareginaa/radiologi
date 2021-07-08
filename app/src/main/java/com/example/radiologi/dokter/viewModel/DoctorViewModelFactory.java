package com.example.radiologi.dokter.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.radiologi.data.repository.Repository;
import com.example.radiologi.di.Injection;

public class DoctorViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile DoctorViewModelFactory instance;
    private final Repository.DoctorRepository repository;

    public DoctorViewModelFactory(Repository.DoctorRepository repository) {
        this.repository = repository;
    }

    public static DoctorViewModelFactory getInstance(Context context){
        if (instance == null){
            synchronized (DoctorViewModelFactory.class){
                instance = new DoctorViewModelFactory(
                        Injection.provideDoctorRepository(context)
                );
            }
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DoctorViewModel.class)){
            //noinspection unchecked
            return (T) new DoctorViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
