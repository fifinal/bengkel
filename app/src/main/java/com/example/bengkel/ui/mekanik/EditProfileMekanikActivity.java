package com.example.bengkel.ui.mekanik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bengkel.R;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.Mekanik;
import com.example.bengkel.ui.bengkel.SetupActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.text.TextUtils.isEmpty;

public class EditProfileMekanikActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView imgMekanik;
    private EditText edtNamaMekanik, edtAlamatMekanik, edtNoHP;
    private Button btnSimpan;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    //var
    private Uri mainImageURI = null;
    private boolean isChanged=false;
    private Bitmap compressedImageFile;
    private Mekanik mekanik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_mekanik);
        getSupportActionBar().setTitle("Data Mekanik");

        imgMekanik=findViewById(R.id.img_mekanik);
        edtNamaMekanik=findViewById(R.id.edt_nama_mekanik);
        edtAlamatMekanik=findViewById(R.id.edt_alamat_mekanik);
        edtNoHP=findViewById(R.id.edt_no_hp);
        btnSimpan=findViewById(R.id.btn_simpan_mekanik);
        progressBar=findViewById(R.id.setup_progress);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        if (getIntent().hasExtra("mekanik")){
            mekanik= getIntent().getParcelableExtra("mekanik");
            setMekanik();
        }

        btnSimpan.setOnClickListener(this);
        imgMekanik.setOnClickListener(this);

    }


    private void setMekanik() {
        edtNamaMekanik.setText(mekanik.getNama());
        edtAlamatMekanik.setText(mekanik.getAlamat());
        edtNoHP.setText(mekanik.getNohp());
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.default_image)
                .placeholder(R.drawable.default_image);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(mekanik.getImgProfile())
                .into(imgMekanik);
    }

    private void loadGambarBengkel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else BringImagePicker();
        }
        else BringImagePicker();
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                imgMekanik.setImageURI(mainImageURI);
                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void simpan(Map<String, String> mekanik){
            firebaseFirestore.collection("Mekanik").document(firebaseAuth.getUid()).set(mekanik, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Map<String,String> map=new HashMap<>();
                    map.put("aktif","yes");

                    firebaseFirestore.collection("Account").document(firebaseAuth.getUid()).set(map,SetOptions.merge());
                    progressBar.setVisibility(View.GONE);
                    mekanikActivity();
                }
            });

    }

    private void mekanikActivity(){
        Intent intent=new Intent(this,HomeMekanikActivity.class);
        startActivity(intent);
        finish();
    }


    private void simpanDataMekanik() {
        final String namaMekanik= edtNamaMekanik.getText().toString().trim();
        final String alamatMekanik = edtAlamatMekanik.getText().toString().trim();
        final String noHP = edtNoHP.getText().toString().trim();

        if (!TextUtils.isEmpty(namaMekanik) && !TextUtils.isEmpty(alamatMekanik) && mainImageURI != null) {

            progressBar.setVisibility(View.VISIBLE);
            btnSimpan.setEnabled(false);

            String user_id = firebaseAuth.getCurrentUser().getUid();
            File newImageFile = new File(mainImageURI.getPath());
            try {
                compressedImageFile = new Compressor(EditProfileMekanikActivity.this)
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

            final StorageReference ref=storageReference.child("mekanik_images").child(user_id + ".jpg");
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

//                        Mekanik mekanik=new Mekanik();
//                        mekanik.setNama(namaMekanik);
//                        mekanik.setAlamat(alamatMekanik);
//                        mekanik.setImgProfile(downloadUri.toString());
//                        mekanik.setNohp();
                        Map<String,String> map=new HashMap<>();
                        map.put("nama",namaMekanik);
                        map.put("alamat",alamatMekanik);
                        map.put("imgProfile",downloadUri.toString());
                        map.put("nohp",noHP);
                        simpan(map);
                    }
                    else {
                        String error = task.getException().getMessage();
                        Toast.makeText(EditProfileMekanikActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_simpan_mekanik:
                simpanDataMekanik();
                break;
            case R.id.img_mekanik:
                loadGambarBengkel();
                break;
        }
    }
}
