package com.example.radiologi.admin.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.radiologi.data.repository.Repository;
import com.example.radiologi.di.Injection;

public class AdminViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile AdminViewModelFactory instance;
    private final Repository.AdminRepository repository;

    public AdminViewModelFactory(Repository.AdminRepository repository) {
        this.repository = repository;
    }

    public static AdminViewModelFactory getInstance(Context context){
        if (instance == null){
            synchronized (AdminViewModelFactory.class){
                instance = new AdminViewModelFactory(
                        Injection.provideAdminRepository(context)
                );
            }
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AdminViewModel.class)){
            //noinspection unchecked
            return (T) new AdminViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
