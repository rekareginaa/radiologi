package com.example.radiologi.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.radiologi.accountsManager.LoginActivity;
import com.example.radiologi.R;
import com.example.radiologi.accountsManager.RegisterActivity;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;

public class FragmentAdmin extends Fragment {

    Button btnRegister, btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), RegisterActivity.class);
            startActivity(intent);
        });
        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(view13 -> {
            SharedPreferenceManager.saveBooleanPreferences(getContext(), "islogin", false );
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        View view1 = view.findViewById(R.id.gelap);
        view1.setOnClickListener(view2 -> requireActivity().onBackPressed());

        return view;
    }
}