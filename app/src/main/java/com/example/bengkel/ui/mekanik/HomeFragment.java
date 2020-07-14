package com.example.bengkel.ui.mekanik;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bengkel.R;
import com.example.bengkel.adapter.OrderRecyclerAdapter;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.Chatroom;
import com.example.bengkel.model.Mekanik;
import com.example.bengkel.model.Order;
import com.example.bengkel.ui.LandingPageActivity;
import com.example.bengkel.ui.bengkel.BengkelDashboardActivity;
import com.example.bengkel.ui.bengkel.MekanikListFragment;
import com.example.bengkel.ui.bengkel.OrderBengkelActivity;
import com.example.bengkel.ui.bengkel.ProfileBengkelActivity;
import com.example.bengkel.ui.user.ChatroomActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    //widget
    private RecyclerView recyclerViewProduct;

    //var
    private OrderRecyclerAdapter orderRecyclerAdapter;
    private ArrayList<Order> orders=new ArrayList<>();
    private Order orderSelected;
    private MekanikListFragment mekanikListFragment;

    //firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ListenerRegistration listenerRegistrationOrder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        manual();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        recyclerViewProduct=view.findViewById(R.id.recycler_pesanan);


        initRecycler();
        getOrder();

        return view;
    }

    private void getOrder(){

        listenerRegistrationOrder=firebaseFirestore.collection("Order")
                .whereEqualTo("idMekanik",firebaseAuth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        orders.clear();
                        if(queryDocumentSnapshots.getDocuments().size()>0){
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Order order= doc.toObject(Order.class);
                                orders.add(order);
                                orderRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void navChatroomActivity(Chatroom chatroom){
        Intent intent = new Intent(getActivity(), ChatroomActivity.class);
        intent.putExtra(getString(R.string.intent_chatroom), chatroom);
        startActivity(intent);
    }



    private void initRecycler(){
        mekanikListFragment=new MekanikListFragment();
        orderRecyclerAdapter=new OrderRecyclerAdapter(orders);
        recyclerViewProduct.setAdapter(orderRecyclerAdapter);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecyclerAdapter.setOnItemClickListener(new OrderRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                orderSelected=order;
                Chatroom chatroom=new Chatroom();
                chatroom.setChatroom_id(order.getId());
                chatroom.setTitle(order.getNama());
                navChatroomActivity(chatroom);
            }

            @Override
            public void onItemLongClick(Order order) {

            }
        });
    }

    private void hapusProduk(String id){
        firebaseFirestore.collection("Order").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                orderRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Produk Berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private LinearLayout formProduk(){
        final EditText produk = new EditText(getContext());
        final EditText stok = new EditText(getContext());
        final EditText harga = new EditText(getContext());
        produk.setInputType(InputType.TYPE_CLASS_TEXT);
        stok.setInputType(InputType.TYPE_CLASS_NUMBER);
        harga.setInputType(InputType.TYPE_CLASS_NUMBER);

        produk.setHint("nama produk");
        stok.setHint("stok");
        harga.setHint("harga");

        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setPadding(10,10,10,10);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.addView(produk,0);
        linearLayout.addView(stok,1);
        linearLayout.addView(harga,2);
        return  linearLayout;

    }

    private void manual(){
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Manual ");
        builder.setMessage("Klik item untuk update");
        builder.setPositiveButton("Mengerti", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        listenerRegistrationOrder.remove();
        super.onDestroy();
    }

}