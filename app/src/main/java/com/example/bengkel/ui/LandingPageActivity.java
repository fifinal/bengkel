package com.example.bengkel.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bengkel.R;
import com.example.bengkel.algoritma.Node;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.BengkelLocation;
import com.example.bengkel.model.Mekanik;
import com.example.bengkel.ui.bengkel.BengkelDashboardActivity;
import com.example.bengkel.ui.bengkel.SetupActivity;
import com.example.bengkel.ui.mekanik.EditProfileMekanikActivity;
import com.example.bengkel.ui.mekanik.HomeMekanikActivity;
import com.example.bengkel.ui.mekanik.MekanikActivity;
import com.example.bengkel.ui.user.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLogin,tvNamaUser;
    private Button btnGrant;
    private CircleImageView circleIcon;
    private ProgressBar mProgressBar;


    private Map<String, Node> nodeMap;
    private List<Bengkel> bengkels;
    private List<BengkelLocation>bengkelLocations;
    private String role;
    private String aktif;

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        tvLogin=findViewById(R.id.tv_login);
        tvNamaUser=findViewById(R.id.tv_nama_user);
        circleIcon=findViewById(R.id.img_icon);
        btnGrant=findViewById(R.id.btn_grant);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        nodeMap=new HashMap<>();
        bengkels=new ArrayList<>();
        bengkelLocations=new ArrayList<>();

        checkAuth();

        tvLogin.setOnClickListener(this);
        btnGrant.setOnClickListener(this);
    }

    private void checkAuth() {
        showDialog();

        final RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.default_image)
                .placeholder(R.drawable.default_image);
        btnGrant.setEnabled(false);

        if (user==null){
            tvLogin.setVisibility(View.VISIBLE);
            btnGrant.setEnabled(true);
            hideDialog();
        }
        else if(user.isAnonymous()){
            hideDialog();
            mainActivity();
        }
        else{
            tvLogin.setVisibility(View.GONE);
            Toast.makeText(LandingPageActivity.this, " : "+user.getUid(), Toast.LENGTH_SHORT).show();
            reference=firebaseFirestore.collection("Account").document(user.getUid());
            reference.get().addOnCompleteListener(LandingPageActivity.this, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    hideDialog();
                    btnGrant.setEnabled(true);
                    role=task.getResult().getString("role");
                    aktif=task.getResult().getString("aktif");
                    if (aktif.equals("no")) {
                        hideDialog();
                        return;
                    }
                    if (role.equals("bengkel")){
                        firebaseFirestore.collection("Bengkel")
                                .document(user.getUid()).get()
                                .addOnCompleteListener(LandingPageActivity.this, new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            Bengkel bengkel = task.getResult().toObject(Bengkel.class);
                                            tvNamaUser.setText(bengkel.getNamaBengkel());
                                            Glide.with(LandingPageActivity.this)
                                                    .setDefaultRequestOptions(requestOptions)
                                                    .load(bengkel.getImgBengkel())
                                                    .into(circleIcon);
                                            hideDialog();
                                        }
                                    }
                                });
                    }
                    else {
                        firebaseFirestore.collection("Mekanik")
                                .document(user.getUid()).get()
                                .addOnCompleteListener(LandingPageActivity.this, new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            Mekanik mekanik=task.getResult().toObject(Mekanik.class);
                                            tvNamaUser.setText(mekanik.getNama());
                                            Glide.with(LandingPageActivity.this)
                                                    .setDefaultRequestOptions(requestOptions)
                                                    .load(mekanik.getImgProfile())
                                                    .into(circleIcon);
                                            hideDialog();
                                        }
                                    }
                                });
                    }

                }
            });

        }
    }

    private void signInAnonim() {
        showDialog();
        firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideDialog();
                mainActivity();
            }
        });
    }

    private void btnMulai(){
        if (role.equals("bengkel")){
            if(aktif.equals("no")) intent(SetupActivity.class);
            else intent(BengkelDashboardActivity.class);
        }
        else if (role.equals("mekanik")){
            if(aktif.equals("no")) intent(EditProfileMekanikActivity.class);
            else intent(HomeMekanikActivity.class);
        }
    }


    private void intent(Class<?> classTujuan){
        Intent intent = new Intent(LandingPageActivity.this, classTujuan);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mainActivity() {
        startActivity(new Intent(LandingPageActivity.this, HomeActivity.class));
        finish();
    }
    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    public void sendBengkelToFireStrore( List<BengkelLocation>bengkelLocations){
        CollectionReference bengkelLocationRef = firebaseFirestore.collection("BengkelLocation");

        for (final BengkelLocation bengkelLocation:bengkelLocations){
            bengkelLocationRef.document().set(bengkelLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(LandingPageActivity.this, "berhasil "+bengkelLocation.getBengkel().getNamaBengkel(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        Toast.makeText(this, "selesai", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:
                Intent intent=new Intent(LandingPageActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_grant:
                if (user!=null&&!user.isAnonymous()) btnMulai();
                else checkPermission();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuth();
    }

    private void checkPermission() {
        Dexter.withActivity(LandingPageActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                       signInAnonim();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LandingPageActivity.this);
                            builder.setTitle("Permission Denied");
                            builder.setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder.setIcon(getResources().getDrawable(R.drawable.ic_location_24dp,null));
                                builder.setBackground(getResources().getDrawable(R.drawable.circularbordersolid, null));
                            }
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else {
                            Toast.makeText(LandingPageActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

}