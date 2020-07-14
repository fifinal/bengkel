package com.example.bengkel.ui.bengkel;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bengkel.R;
import com.example.bengkel.adapter.MekanikRecyclerAdapter;
import com.example.bengkel.model.Mekanik;
import com.example.bengkel.util.SpaceItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MekanikListFragment extends Fragment implements MekanikRecyclerAdapter.OnItemClickListener {


    private static final int NUM_COLUMNS = 2;

    //widgets
    private RecyclerView mRecyclerView;


    //vars
    private IMekanik iMekanik;
    private ArrayList<Mekanik> mekaniks=new ArrayList<>();
    private MekanikRecyclerAdapter mAdapter;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ListenerRegistration listenerRegistrationMekanik;
    public MekanikListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mekanik_list, container, false);
        mRecyclerView = view.findViewById(R.id.mekanik_list_recyclerview);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        initRecyclerview();
        getMekanik();
        return view;
    }

    private void getMekanik(){
        listenerRegistrationMekanik=firebaseFirestore.collection("Mekanik").whereEqualTo("idBengkel",user.getUid()).addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mekaniks.clear();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        if (doc!=null){
                            Mekanik mekanik = doc.toObject(Mekanik.class);
                            mekaniks.add(mekanik);
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }

    private void initRecyclerview(){
        mAdapter = new MekanikRecyclerAdapter(mekaniks, getContext());
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.addItemDecoration(new SpaceItem(20));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iMekanik = (IMekanik) getActivity();
    }


    @Override
    public void onItemClick(Mekanik mekanik) {
        iMekanik.onMekanikSelected(mekanik);
    }

    @Override
    public void onItemLongClick(Mekanik mekanik) {

    }
}
