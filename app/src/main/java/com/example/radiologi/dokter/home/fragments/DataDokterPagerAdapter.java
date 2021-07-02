package com.example.radiologi.dokter.home.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DataDokterPagerAdapter extends FragmentStatePagerAdapter {

    private int number_tabs_dokter;

    public DataDokterPagerAdapter(FragmentManager fragmentManager, int number_tab) {
        super(fragmentManager);
        this.number_tabs_dokter = number_tab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DataDokterBaruFragment();
            case 1:
                return new DataDokterDiagnosaFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number_tabs_dokter;
    }
}
