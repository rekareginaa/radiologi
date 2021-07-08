package com.example.radiologi.admin.home.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DataAdminPagerAdapter extends FragmentStatePagerAdapter {

    private int number_tabs;

    public DataAdminPagerAdapter(FragmentManager fragmentManager, int numberTabs) {
        super(fragmentManager);
        this.number_tabs = numberTabs;
    }

    //Mengembalikan fragment yang terkait dengan posisi tertentu
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DataAdminBaruFragment();
            case 1:
                return new DataAdminDiagnosaFragment();
            default:
                return null;
        }
    }

    //Mengembalikan jumlah tampilan yang tersedia

    @Override
    public int getCount() {
        return number_tabs;
    }
}
