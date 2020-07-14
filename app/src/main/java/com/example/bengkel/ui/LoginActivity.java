package com.example.bengkel.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bengkel.R;
import com.example.bengkel.ui.bengkel.BengkelDashboardActivity;
import com.example.bengkel.ui.bengkel.SetupActivity;
import com.example.bengkel.ui.mekanik.EditProfileMekanikActivity;
import com.example.bengkel.ui.mekanik.HomeMekanikActivity;
import com.example.bengkel.ui.mekanik.MekanikActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener
{

    private static final String TAG = "LoginActivity";

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    // widgets
    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;
    private TextView tvLogin;
    private Button btnLogin;

    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (user==null || user.isAnonymous()){
            if (user!=null) firebaseAuth.signOut();
            setContentView(R.layout.activity_login);
            tvLogin = findViewById(R.id.tvLogin);
            mEmail = findViewById(R.id.email);
            mPassword = findViewById(R.id.password);
            mProgressBar = findViewById(R.id.progressBar);
            btnLogin=findViewById(R.id.email_sign_in_button);
            btnLogin.setOnClickListener(this);
            findViewById(R.id.link_register).setOnClickListener(this);
            setupFirebaseAuth();
            hideSoftKeyboard();
        }
        else setupFirebaseAuth();
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

    private void intent(Class<?> classTujuan){
        Intent intent = new Intent(LoginActivity.this, classTujuan);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    /*
        ----------------------------- Firebase setup ---------------------------------
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                    if (firebaseUser!=null&&!firebaseUser.isAnonymous()) {
                        firebaseFirestore.collection("Account").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    String role = task.getResult().getString("role");
                                    String aktif = task.getResult().getString("aktif");
                                    if (role.equals("bengkel")) {
                                        if (aktif.equals("no")) intent(SetupActivity.class);
                                        else intent(BengkelDashboardActivity.class);
                                    } else if (role.equals("mekanik")) {
                                        if (aktif.equals("no"))
                                            intent(EditProfileMekanikActivity.class);
                                        else intent(HomeMekanikActivity.class);
                                    }
                                }
                            }
                        });
                    }
                }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn(){
        //check if the fields are filled out
        if(!isEmpty(mEmail.getText().toString())
                && !isEmpty(mPassword.getText().toString())){
            Log.d(TAG, "onClick: attempting to authenticate.");

            showDialog();
            btnLogin.setEnabled(false);

            FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getText().toString(),
                    mPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideDialog();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    btnLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
            });
        }else{
            Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.link_register:{
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                Pair[] pairs    = new Pair[1];
                pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(intent,activityOptions.toBundle());
                break;
            }

            case R.id.email_sign_in_button:{
               signIn();
               break;
            }
        }
    }
}