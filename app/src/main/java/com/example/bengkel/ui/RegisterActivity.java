package com.example.bengkel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bengkel.R;
import com.example.bengkel.model.Account;
import com.example.bengkel.ui.bengkel.SetupActivity;
import com.example.bengkel.ui.mekanik.EditProfileMekanikActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import static android.text.TextUtils.isEmpty;
import static com.example.bengkel.util.Check.doStringsMatch;


public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener
{
    private static final String TAG = "RegisterActivity";

    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    //vars
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private boolean roleMekanik=false;

    private RelativeLayout rlayout;
    private Animation animation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        auth();

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);

        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnRegister=findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        hideSoftKeyboard();
    }

    private void auth(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void checkAccount(final String id, final String email){
        firebaseFirestore.collection("Account")
                .whereEqualTo("email",email)
                .get().addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult().getDocuments().size()>0){
                            Account accountMekanik=task.getResult().getDocuments().get(0).toObject(Account.class);
                            if(accountMekanik.getRole().equals("mekanik")){
                                redirectSetupMekanikScreen();
                            }else redirectSetupScreen();
                        }else {
                            Account account=new Account(id,email,"bengkel","no");
                            saveAccount(account);
                        }
                    }
                }
        });
    }

    private void registerNewEmail(final String email, final String password){
        showDialog();
        btnRegister.setEnabled(false);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            signIn(email,password);
                        }
                        else {
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "registrasi gagal.", Snackbar.LENGTH_SHORT).show();
                            hideDialog();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAccount(Account account){
        firebaseFirestore.collection("Account").document(account.getId()).set(account).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideDialog();
                redirectSetupScreen();
            }
        });
    }

    private void signIn(final String email, String password){
        //check if the fields are filled out
        if(!isEmpty(email) && !isEmpty(password)){
            showDialog();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String id=task.getResult().getUser().getUid();
                            checkAccount(id,email);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
            });
        }else{
            Toast.makeText(RegisterActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Redirects the user to the login screen
     */
    private void redirectSetupScreen(){
        Intent intent = new Intent(RegisterActivity.this, SetupActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectSetupMekanikScreen(){
        Intent intent = new Intent(RegisterActivity.this, EditProfileMekanikActivity.class);
        startActivity(intent);
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

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:{
                Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if(!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())){

                    //check if passwords match
                    if(doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())){

                        //Initiate registration task
                        registerNewEmail(mEmail.getText().toString(), mPassword.getText().toString());
                    }else{
                        Toast.makeText(RegisterActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
