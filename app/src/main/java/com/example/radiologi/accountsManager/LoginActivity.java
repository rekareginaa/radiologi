package com.example.radiologi.accountsManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.radiologi.R;
import com.example.radiologi.accountsManager.viewModel.AccountViewModel;
import com.example.radiologi.accountsManager.viewModel.AccountViewModelFactory;
import com.example.radiologi.databinding.ActivityLoginBinding;

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

        AccountViewModelFactory factory = AccountViewModelFactory.getInstance(this);
        viewModel = new ViewModelProvider(this, factory).get(AccountViewModel.class);

        observeResult();
    }

    private void observeResult() {
        viewModel.getUserData.observe(this, result -> {
            switch (result.getContentIfNotHandled().status){
                case LOADING:
                    Log.d("REQ", "LOADING");
                case ERROR:
                    Log.d("REQ", "ERROR");
                case SUCCESS:
                    Log.d("REQ", "SUCCESS");

            }

            /*final DataItemLogin user = response.getData().get(0);
            Log.d("DATA_", user.toString());
            switch (response.getStatus()) {
                case "sukses":
                    SharedPreferenceManager.savesStringPreferences(getApplicationContext(), "nip", user.getNip());
                    SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "islogin", true);
                    SharedPreferenceManager.savesStringPreferences(getApplicationContext(), "role", user.getRole());
                    SharedPreferenceManager.savesStringPreferences(getApplicationContext(), "token", user.getToken());

                    if (user.getRole().equals("admin")) {
                        Intent intent = new Intent(getApplicationContext(), DataAdminActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (user.getRole().equals("dokter")) {
                        Intent intent = new Intent(getApplicationContext(), DataDokterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                    break;
                case "username":
                    Toast.makeText(getApplicationContext(), "Username Salah", Toast.LENGTH_LONG).show();
                    break;
                case "password":
                    Toast.makeText(getApplicationContext(), "Password Salah", Toast.LENGTH_SHORT).show();
                    break;
                case "gagal":
                    Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                    break;
            }*/

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