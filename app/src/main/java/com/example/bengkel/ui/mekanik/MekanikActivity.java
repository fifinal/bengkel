package com.example.bengkel.ui.mekanik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bengkel.R;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.Mekanik;
import com.example.bengkel.ui.LandingPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MekanikActivity extends AppCompatActivity {

    private ImageView imgBengkel;
    private CircleImageView imgMekanik;
    private TextView tvEmail, tvNamaMekanik, tvNoHP;
    private Button btnLogout,btnEdit;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private Mekanik mekanik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mekanik);
        imgBengkel=findViewById(R.id.img_bengkel);
        imgMekanik=findViewById(R.id.img_mekanik);
        tvNamaMekanik=findViewById(R.id.tv_nama_mekanik);
        tvEmail=findViewById(R.id.tv_email);
        tvNoHP=findViewById(R.id.tv_no_hp);
        btnLogout=findViewById(R.id.btn_logout);
        btnEdit=findViewById(R.id.btn_edit);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=firebaseAuth.getInstance();

        if (getIntent().hasExtra("mekanik")){
            mekanik= getIntent().getParcelableExtra("mekanik");
            setMekanik();
        }
        else getMekanik();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MekanikActivity.this, EditProfileMekanikActivity.class);
                intent.putExtra("mekanik",mekanik);
                startActivity(intent);

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(MekanikActivity.this, LandingPageActivity.class));
                finish();
            }
        });

    }
    private void getMekanik() {
        firebaseFirestore.collection("Mekanik").document(firebaseAuth.getUid()).get().addOnCompleteListener(this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult()!=null){
                        mekanik=task.getResult().toObject(Mekanik.class);
                        setMekanik();
                    }
                }
            }
        });
    }
    private void getBengkel() {
        firebaseFirestore.collection("Bengkel").document(mekanik.getIdBengkel()).get().addOnCompleteListener(this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult()!=null){
                        Bengkel bengkel=task.getResult().toObject(Bengkel.class);
                        RequestOptions requestOptions = new RequestOptions()
                                .error(R.drawable.post_placeholder)
                                .placeholder(R.drawable.post_placeholder);

                        Glide.with(MekanikActivity.this)
                                .setDefaultRequestOptions(requestOptions)
                                .load(bengkel.getImgBengkel())
                                .into(imgBengkel);
                    }
                }
            }
        });
    }

    private void setMekanik() {
        tvEmail.setText(mekanik.getEmail());
        tvNamaMekanik.setText(mekanik.getNama());
        tvNoHP.setText(mekanik.getNohp());
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.default_image)
                .placeholder(R.drawable.default_image);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(mekanik.getImgProfile())
                .into(imgMekanik);
        getBengkel();
    }
}