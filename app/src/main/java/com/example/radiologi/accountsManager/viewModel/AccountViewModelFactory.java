package com.example.radiologi.accountsManager.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.radiologi.data.repository.AccountRepositoryImpl;
import com.example.radiologi.di.Injection;

public class AccountViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile AccountViewModelFactory instance;
    private final AccountRepositoryImpl repository;

    public AccountViewModelFactory(AccountRepositoryImpl repository) {
        this.repository = repository;
    }

    public static AccountViewModelFactory getInstance(){
        if (instance == null){
            synchronized (AccountViewModelFactory.class){
                instance = new AccountViewModelFactory(
                        (AccountRepositoryImpl) Injection.provideAccountRepository());
            }
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AccountViewModel.class)){
            //noinspection unchecked
            return (T) new AccountViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
