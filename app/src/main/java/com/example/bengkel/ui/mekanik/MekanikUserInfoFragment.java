package com.example.bengkel.ui.mekanik;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bengkel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MekanikUserInfoFragment extends Fragment {
    TextView  tvEditAbout, tvAbout, tvEditKontak, tvNoHp, tvEmail, tvAlamat;

    public MekanikUserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_mekanik_user_info, container, false);

        /*about */
        tvEditAbout = view.findViewById(R.id.tv_edit_about);
        tvAbout = view.findViewById(R.id.tv_about);

        /*kontak */
        tvEditKontak = view.findViewById(R.id.tv_edit_kontak);
        tvNoHp = view.findViewById(R.id.tv_no_hp);
        tvEmail = view.findViewById(R.id.tv_email);
        tvAlamat = view.findViewById(R.id.tv_alamat);

        return view;
    }

}
