package com.example.bengkel.ui.bengkel;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bengkel.R;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.BengkelLocation;
import com.example.bengkel.ui.LandingPageActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MAPS_LOKASI = 1;
    private ImageView imgBengkel;
    private Uri mainImageURI = null;
    private GeoPoint geoPoint;

    private String user_id;

    private boolean isChanged = false;

    private EditText edtNamaBengkel, edtAlamatBengkel,edtTelp;
    private Button btnSimpan, btnBuka,btnTutup;
    private ImageButton btnLokasi;
    private ProgressBar setupProgress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;
    private Bengkel bengkel;
    private String buka="";
    private String tutup="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        imgBengkel = findViewById(R.id.img_bengkel);
        edtNamaBengkel = findViewById(R.id.edt_nama_bengkel);
        edtAlamatBengkel = findViewById(R.id.edt_alamat_bengkel);
        edtTelp = findViewById(R.id.edt_telp);
        btnBuka = findViewById(R.id.btn_buka);
        btnTutup = findViewById(R.id.btn_tutup);
        btnSimpan = findViewById(R.id.btn_simpan_bengkel);
        btnLokasi = findViewById(R.id.btn_lokasi);
        setupProgress = findViewById(R.id.setup_progress);

        if (getIntent().hasExtra("bengkel")){
            bengkel=getIntent().getParcelableExtra("bengkel");
            setBengkel();
        }

        btnBuka.setOnClickListener(this);
        btnTutup.setOnClickListener(this);
        btnLokasi.setOnClickListener(this);
        btnSimpan.setOnClickListener(this);
        imgBengkel.setOnClickListener(this);

    }

    public void setBengkel(){
        edtNamaBengkel.setText(bengkel.getNamaBengkel());
        edtAlamatBengkel.setText(bengkel.getAlamatBengkel());
        edtTelp.setText(bengkel.getTelp());
        btnBuka.setText(bengkel.getBuka());
        btnTutup.setText(bengkel.getTutup());
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.post_placeholder)
                .placeholder(R.drawable.post_placeholder);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(bengkel.getImgBengkel())
                .into(imgBengkel);
        mainImageURI=Uri.parse(bengkel.getImgBengkel());
        firebaseFirestore.collection("BengkelLocation").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult()!=null){
                    BengkelLocation bengkelLocation=task.getResult().toObject(BengkelLocation.class);
                    geoPoint= bengkelLocation.getGeoPoint();
                }
            }
        });

    }

    private void storeFirestore(Bengkel bengkel) {
        final BengkelLocation bengkelLocation=new BengkelLocation(bengkel,geoPoint,null);
        firebaseFirestore.collection("Bengkel").document(user_id).set(bengkel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Map<String,String> map=new HashMap<>();
                    map.put("aktif","yes");

                    firebaseFirestore.collection("Account").document(user_id).set(map,SetOptions.merge());
                    firebaseFirestore.collection("BengkelLocation").document(user_id).set(bengkelLocation);

                    Toast.makeText(SetupActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(SetupActivity.this, BengkelDashboardActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                }
                setupProgress.setVisibility(View.INVISIBLE);
                btnSimpan.setEnabled(true);
            }
        });
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4, 3)
                .start(SetupActivity.this);
    }

    private void simpanDataBengkel() {
        final String namaBengkel = edtNamaBengkel.getText().toString().trim();
        final String alamatBengkel = edtAlamatBengkel.getText().toString().trim();
        final String telp = edtTelp.getText().toString().trim();

        if (TextUtils.isEmpty(namaBengkel)){
            edtNamaBengkel.setError("nama bengkel harus didisi");
        }
        else if (TextUtils.isEmpty(alamatBengkel)){
            edtAlamatBengkel.setError("alamat bengkel harus didisi");
        }
        else if (TextUtils.isEmpty(telp)){
            edtAlamatBengkel.setError("telp bengkel harus didisi");
        }
        else if (imgBengkel == null){
            Toast.makeText(this, "pilih dulu gambar bengkel", Toast.LENGTH_SHORT).show();
        }
        else if (geoPoint == null){
            Toast.makeText(this, "pilih lokasi", Toast.LENGTH_SHORT).show();
        }
        else {
            if (getIntent().getParcelableExtra("bengkel")!=null){
                bengkel=new Bengkel(user_id,bengkel.getImgBengkel(),namaBengkel,alamatBengkel);
                bengkel.setBuka(btnBuka.getText().toString());
                bengkel.setTutup(btnTutup.getText().toString());
                bengkel.setTelp(telp);
                storeFirestore(bengkel);
            }else{


            setupProgress.setVisibility(View.VISIBLE);
            btnSimpan.setEnabled(false);

                user_id = firebaseAuth.getCurrentUser().getUid();
                File newImageFile = new File(mainImageURI.getPath());
                try {
                    compressedImageFile = new Compressor(SetupActivity.this)
                            .setMaxHeight(125)
                            .setMaxWidth(125)
                            .setQuality(50)
                            .compressToBitmap(newImageFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thumbData = baos.toByteArray();

                final StorageReference ref=storageReference.child("bengkel_images").child(user_id + ".jpg");
                UploadTask image_path = ref.putBytes(thumbData);
                image_path.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return  ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri downloadUri;
                        if (task.isSuccessful()){
                            downloadUri=task.getResult();
                            if (downloadUri==null) downloadUri= mainImageURI;

                            bengkel=new Bengkel(user_id,downloadUri.toString(),namaBengkel,alamatBengkel);
                            bengkel.setBuka(buka);
                            bengkel.setTutup(tutup);
                            bengkel.setTelp(telp);
                            Toast.makeText(SetupActivity.this, namaBengkel+downloadUri.toString(), Toast.LENGTH_SHORT).show();
                            storeFirestore(bengkel);
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(SetupActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                            setupProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }

        }
    }

    private void loadGambarBengkel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else BringImagePicker();
        }
        else BringImagePicker();
    }

    private void checkPermission() {
        Dexter.withActivity(SetupActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(SetupActivity.this, MapsActivity.class);
                        startActivityForResult(intent,MAPS_LOKASI);
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SetupActivity.this);
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
                            Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                imgBengkel.setImageURI(mainImageURI);
                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if(requestCode==MAPS_LOKASI&&resultCode==RESULT_OK){
            double lat=data.getDoubleExtra(MapsActivity.LAT,1);
            double lng=data.getDoubleExtra(MapsActivity.LNG,1);
            geoPoint=new GeoPoint(lat,lng);

            Toast.makeText(this, "lat : "+lat+" \n lng "+lng, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_simpan_bengkel:
                simpanDataBengkel();
                break;
            case R.id.btn_buka:
                showTimePicker("buka");
                break;
            case R.id.btn_tutup:
                showTimePicker("tutup");
                break;
            case R.id.btn_lokasi:
                checkPermission();
                break;
            case R.id.img_bengkel:
                loadGambarBengkel();
                break;
        }
    }

    private void showTimePicker(final String operational){
        Calendar calendar=Calendar.getInstance();

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String jam=(hourOfDay<10)?"0"+hourOfDay:hourOfDay+"";
                String menit= (minute<10) ?"0"+minute: minute+"";

                if (operational.equals("buka")){
                    buka=jam+":"+menit;
                    btnBuka.setText(buka);
                }else {
                    tutup=jam+":"+menit;
                    btnTutup.setText(tutup);
                }

            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this)).show();
    }


}
