package com.example.bengkel.ui.bengkel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bengkel.R;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.BengkelLocation;
import com.example.bengkel.ui.LandingPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class BengkelDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tvPesananBaru;
    private SwitchMaterial swPenuh;
    private CardView cardPesananBaru;
    private FloatingActionButton fabPesanan, fabLaporan, fabProduk, fabMekanik, fabMaps, fabHome;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    BengkelLocation bengkel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bengkel_dashboard);

        swPenuh=findViewById(R.id.sw_penuh);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        toolbar=findViewById(R.id.toolbar);

        getToolbar();
        getPesanan();

        swPenuh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    bengkel.getBengkel().setPenuh(true);
                    firestore.collection("Bengkel").document(user.getUid()).set(bengkel.getBengkel(), SetOptions.merge());
                    firestore.collection("BengkelLocation").document(user.getUid()).set(bengkel, SetOptions.merge());
                    Toast.makeText(BengkelDashboardActivity.this, "penuh", Toast.LENGTH_SHORT).show();
                }
                else {
                    bengkel.getBengkel().setPenuh(false);
                    firestore.collection("Bengkel").document(user.getUid()).set(bengkel.getBengkel(), SetOptions.merge());
                    firestore.collection("BengkelLocation").document(user.getUid()).set(bengkel, SetOptions.merge());
                    Toast.makeText(BengkelDashboardActivity.this, "tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.fab_home).setOnClickListener(this);
        findViewById(R.id.fab_pesanan).setOnClickListener(this);
        findViewById(R.id.fab_produk).setOnClickListener(this);
        findViewById(R.id.fab_mekanik).setOnClickListener(this);
        findViewById(R.id.fab_layanan).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    private void getPesanan(){
        firestore.collection("Bengkel/"+user.getUid()+"/Order")
                .whereEqualTo("status","baru")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.size()>0){
                            cardPesananBaru=findViewById(R.id.card_pesanan_baru);
                            tvPesananBaru=findViewById(R.id.tv_pesanan_baru);
                            tvPesananBaru.setText(queryDocumentSnapshots.size()+"");
                            cardPesananBaru.setVisibility(View.VISIBLE);
                            cardPesananBaru.setOnClickListener(BengkelDashboardActivity.this);
                        }
                    }
                });
    }

    private void getToolbar() {
        firestore.collection("BengkelLocation").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    bengkel=task.getResult().toObject(BengkelLocation.class);
                    toolbar.setTitle(bengkel.getBengkel().getNamaBengkel());
                    toolbar.setSubtitle(bengkel.getBengkel().getAlamatBengkel());
                    swPenuh.setChecked(bengkel.getBengkel().isPenuh());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
         case R.id.fab_pesanan:
             startActivity(new Intent(this,OrderBengkelActivity.class));
             break;
         case R.id.fab_produk:
             startActivity(new Intent(this,ProductActivity.class));
             break;
         case R.id.fab_mekanik:
             startActivity(new Intent(this,MekanikBengkelActivity.class));
             break;
         case R.id.fab_home:
             startActivity(new Intent(this,ProfileBengkelActivity.class));
             break;
             case R.id.fab_layanan: startActivity(new Intent(this,LayananActivity.class));
                break;
         case R.id.card_pesanan_baru:
             startActivity(new Intent(this,OrderBengkelActivity.class));
             break;
            case R.id.btn_logout:
                auth.signOut();
                startActivity(new Intent(BengkelDashboardActivity.this, LandingPageActivity.class));
                finish();
                break;
             default:
                 break;
     }
    }
}
