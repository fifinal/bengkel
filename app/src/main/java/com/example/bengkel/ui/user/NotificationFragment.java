package com.example.bengkel.ui.user;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bengkel.R;
import com.example.bengkel.adapter.ChatroomRecyclerAdapter;
import com.example.bengkel.adapter.NotificationRecyclerAdapter;
import com.example.bengkel.model.Chatroom;
import com.example.bengkel.model.Notif;
import com.example.bengkel.model.UserLocation;
import com.example.bengkel.ui.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements ChatroomRecyclerAdapter.ChatroomRecyclerClickListener {

    private static final String TAG = "MainActivity";

    //widgets
    private ProgressBar mProgressBar;

    //vars
    private ArrayList<Chatroom> mChatrooms = new ArrayList<>();
    private Set<String> mChatroomIds = new HashSet<>();
    private ChatroomRecyclerAdapter mChatroomRecyclerAdapter;
    private RecyclerView mChatroomRecyclerView;
    private ListenerRegistration mChatroomEventListener;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private UserLocation mUserLocation;

    NotificationRecyclerAdapter notificationRecyclerAdapter;
    ArrayList<Notif> notifArrayList=new ArrayList<>();

    RecyclerView recyclerViewNotif;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notification, container, false);
        mChatroomRecyclerView=view.findViewById(R.id.recycler_notif);
        mProgressBar = view.findViewById(R.id.progressBar);

        mDb = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        initChatroomRecyclerView();
        return view;
    }

    private void initChatroomRecyclerView() {
        mChatroomRecyclerAdapter= new ChatroomRecyclerAdapter(mChatrooms,this);
        mChatroomRecyclerView.setAdapter(mChatroomRecyclerAdapter);
        mChatroomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void getChatrooms(){

        mChatrooms.clear();
        mChatrooms.add(new Chatroom("", "title", "content", "chatroom_id", null));
        mDb.collection("Chatroom")
                .whereEqualTo("chatroom_id",firebaseAuth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Chatroom chatroom = doc.toObject(Chatroom.class);
                        mChatrooms.add(chatroom);
                    }
                    mChatroomRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void navChatroomActivity(Chatroom chatroom){
        Intent intent = new Intent(getActivity(), ChatroomActivity.class);
        intent.putExtra(getString(R.string.intent_chatroom), chatroom);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mChatroomEventListener != null){
            mChatroomEventListener.remove();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getChatrooms();
    }

    @Override
    public void onChatroomSelected(Chatroom chatroom) {
        navChatroomActivity(chatroom);
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_logout:{
                signOut();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }

    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog(){
        mProgressBar.setVisibility(View.GONE);
    }

}
