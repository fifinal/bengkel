package com.example.bengkel.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                return new ProdukFragment();
//            case 1:
//                return new PesananFragment();
//            case 2:
//                return new MekanikFragment();
//            default:
                    return null;
//
//        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
