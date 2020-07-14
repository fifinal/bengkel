package com.example.bengkel.ui.bengkel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bengkel.R;
import com.example.bengkel.adapter.LayananRecyclerAdapter;
import com.example.bengkel.adapter.ProdukRecyclerAdapter;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.Layanan;
import com.example.bengkel.model.Produk;
import com.example.bengkel.ui.LandingPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.TextUtils.isEmpty;

public class ProfileBengkelActivity extends AppCompatActivity {
    private ImageView imgBengkel;
    private TextView tvAlamat, tvNamaBengkel,tvBuka, tvTelp,tvPenuh;
    private Button btnLogout,btnEdit;
    private RecyclerView recyclerViewLayanan,recyclerViewProduk;
    private LinearLayout layout;

    //var
    private LayananRecyclerAdapter layananRecyclerAdapter;
    private ProdukRecyclerAdapter produkRecyclerAdapter;

    private Layanan layanan;
    private ArrayList<Layanan> layananList=new ArrayList<>();
    private ArrayList<Produk> produks=new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private Bengkel bengkel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_bengkel);
        setTitle("Profile Bengkel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);

        recyclerViewLayanan=findViewById(R.id.rv_layanan);
        recyclerViewProduk=findViewById(R.id.rv_produk);
        layout=findViewById(R.id.layout_button);
        imgBengkel=findViewById(R.id.img_bengkel);
        tvNamaBengkel=findViewById(R.id.tv_nama_bengkel);
        tvAlamat=findViewById(R.id.tv_alamat_bengkel);
        tvBuka=findViewById(R.id.tv_buka);
        tvTelp=findViewById(R.id.tv_telp);
        tvPenuh=findViewById(R.id.tv_penuh);
        btnLogout=findViewById(R.id.btn_logout);
        btnEdit=findViewById(R.id.btn_edit);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=firebaseAuth.getInstance();

        initRecycler();

        if (getIntent().hasExtra("bengkel")){
            bengkel=getIntent().getParcelableExtra("bengkel");
            setBengkel();
            layout.setVisibility(View.GONE);
            getLayanan(bengkel.getBengkelId());
        }
        else {
            layout.setVisibility(View.VISIBLE);
            getBengkel();
            getLayanan(firebaseAuth.getUid());
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileBengkelActivity.this, SetupActivity.class);
                intent.putExtra("bengkel",bengkel);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(ProfileBengkelActivity.this, LandingPageActivity.class));
                finish();
            }
        });
    }

    private void initRecycler(){
        layananRecyclerAdapter=new LayananRecyclerAdapter(layananList);
        recyclerViewLayanan.setAdapter(layananRecyclerAdapter);
        recyclerViewLayanan.setLayoutManager(new LinearLayoutManager(this));

        produkRecyclerAdapter=new ProdukRecyclerAdapter(produks);
        recyclerViewProduk.setAdapter(produkRecyclerAdapter);
        recyclerViewProduk.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getBengkel() {
        firebaseFirestore.collection("Bengkel").document(firebaseAuth.getUid()).get().addOnCompleteListener(this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult()!=null){
                        bengkel=task.getResult().toObject(Bengkel.class);
                        setBengkel();
                    }
                }
            }
        });
    }

    private void getLayanan(String id) {
        firebaseFirestore.collection("Bengkel/"+id+"/Layanan").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots.size()!=0) {
                        layananList.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Layanan layanan=documentSnapshot.toObject(Layanan.class);
                            layananList.add(layanan);
                        }
                        layananRecyclerAdapter.notifyDataSetChanged();
                    }
            }
        });
        getProduk(id);
    }

    private void getProduk(String id) {
        firebaseFirestore.collection("Bengkel/"+id+"/Product").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.size()!=0) {
                    produks.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Produk produk=documentSnapshot.toObject(Produk.class);
                        produks.add(produk);
                    }
                    produkRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setBengkel() {
        tvNamaBengkel.setText(bengkel.getNamaBengkel());
        tvAlamat.setText(bengkel.getAlamatBengkel());
        String operational=bengkel.getBuka()+" - "+bengkel.getTutup();
        tvBuka.setText(operational);
        tvTelp.setText(bengkel.getTelp());
        tvPenuh.setText(bengkel.isPenuh()?"Penuh":"Tersedia");
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.post_placeholder)
                .placeholder(R.drawable.post_placeholder);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(bengkel.getImgBengkel())
                .into(imgBengkel);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
