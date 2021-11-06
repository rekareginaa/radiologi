package com.example.radiologi.accountsManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.radiologi.R;
import com.example.radiologi.accountsManager.viewModel.AccountViewModel;
import com.example.radiologi.accountsManager.viewModel.AccountViewModelFactory;
import com.example.radiologi.admin.home.DataAdminActivity;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.data.dataSource.remote.response.DataItemLogin;
import com.example.radiologi.data.dataSource.remote.response.LoginResponse;
import com.example.radiologi.databinding.ActivityLoginBinding;
import com.example.radiologi.dokter.home.DataDokterActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String nip, password;

    private ActivityLoginBinding binding;
    private AccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnMasuk.setOnClickListener(this);

        AccountViewModelFactory factory = AccountViewModelFactory.get();
        viewModel = new ViewModelProvider(this, factory).get(AccountViewModel.class);

        observeResult();
    }

    private void observeResult() {
        viewModel.getUserData.observe(this, result -> {
            switch (result.getContentIfNotHandled().status){
                case LOADING:
                    Log.d("REQ", "LOADING");
                    break;
                case ERROR:
                    Log.d("REQ", "ERROR");
                    break;
                case SUCCESS:
                    final LoginResponse response = result.peekContent().data;
                    if (response != null){
                        final DataItemLogin user = response.getData().get(0);
                        SharedPreferenceManager.savesStringPreferences(this, "nip", user.getNip());
                        SharedPreferenceManager.saveBooleanPreferences(this, "islogin", true);
                        SharedPreferenceManager.savesStringPreferences(this, "role", user.getRole());
                        SharedPreferenceManager.savesStringPreferences(this, "token", user.getToken());

                        if (user.getRole().equals("admin")) {
                            Intent intent = new Intent(getApplicationContext(), DataAdminActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (user.getRole().equals("dokter")) {
                            Intent intent = new Intent(getApplicationContext(), DataDokterActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    break;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_masuk) {
            loginUser();
        }
    }

    private void loginUser() {
        nip = binding.etNIP.getText().toString().trim();
        password = binding.etKataSandi.getText().toString().trim();

        viewModel.setParameter(nip, password);
    }
}