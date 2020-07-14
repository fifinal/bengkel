package com.example.bengkel.ui.bengkel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bengkel.R;
import com.example.bengkel.adapter.MekanikRecyclerAdapter;
import com.example.bengkel.model.Account;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.Mekanik;
import com.example.bengkel.ui.LandingPageActivity;
import com.example.bengkel.ui.mekanik.MekanikActivity;
import com.example.bengkel.util.SpaceItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class MekanikBengkelActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    FloatingActionButton fab;
    RecyclerView recyclerViewMekanik;
    BottomAppBar bottomAppBar;
    Toolbar toolbar;

    private MekanikRecyclerAdapter mekanikRecyclerAdapter;
    private ArrayList<Mekanik> mekaniks=new ArrayList<>();
    private Mekanik mekanikSelected;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ListenerRegistration listenerRegistrationMekanik;
    private FirebaseOptions.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mekanik_bengkel);
        manual();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        builder=new FirebaseOptions.Builder();
        builder.setApiKey("AIzaSyBEvrMTqlV4CXjGDVcgZMCjtpX_7gUhNW4");
        builder.setDatabaseUrl("https://maps-da6ed.firebaseio.com");
        builder.setProjectId("maps-da6ed");
        builder.setApplicationId("com.example.bengkel");


        toolbar=findViewById(R.id.toolbar);
        getToolbar();
        fab=findViewById(R.id.fab);
        recyclerViewMekanik=findViewById(R.id.recycler_mekanik);
        bottomAppBar=findViewById(R.id.btn_app_bar);
        setSupportActionBar(bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MekanikBengkelActivity.this,BengkelDashboardActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                finish();
            }
        });
        bottomAppBar.setOnMenuItemClickListener(this);


        fab.setOnClickListener(this);

        initRecycler();
        getMekanik();

    }

    private void getToolbar() {
        firebaseFirestore.collection("Bengkel").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Bengkel bengkel=task.getResult().toObject(Bengkel.class);
                    toolbar.setTitle(bengkel.getNamaBengkel());
                    toolbar.setSubtitle(bengkel.getAlamatBengkel());
                }
            }
        });
    }

    private void saveAccount(final String email,final String password){

        FirebaseOptions firebaseOptions=builder.build();
        final FirebaseApp firebaseApp=FirebaseApp.initializeApp(MekanikBengkelActivity.this,firebaseOptions,"secondary");

        FirebaseAuth.getInstance(firebaseApp)
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String id=task.getResult().getUser().getUid();
                            final Account account=new Account(id,email,"mekanik","no");
                            firebaseFirestore.collection("Account")
                                   .document(id).set(account)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            String idBengkel=firebaseAuth.getCurrentUser().getUid();
                                            Mekanik mekanik=new Mekanik();
                                            mekanik.setId(account.getId());
                                            mekanik.setEmail(email);
                                            mekanik.setIdBengkel(idBengkel);
                                            saveMekanik(mekanik);

                                        }
                                    });

                            FirebaseAuth.getInstance(firebaseApp).signOut();
                            firebaseApp.delete();
                        }
                    }
                });

    }

    private void saveMekanik(final Mekanik mekanik){

        firebaseFirestore.collection("Mekanik")
                .document(mekanik.getId()).set(mekanik, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseFirestore.collection("Bengkel/"+user.getUid()+"/Mekanik")
                                .document(mekanik.getId()).set(mekanik,SetOptions.merge());
                        Toast.makeText(MekanikBengkelActivity.this, "Berhasil ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getMekanik(){
        listenerRegistrationMekanik=firebaseFirestore.collection("Mekanik").whereEqualTo("idBengkel",user.getUid()).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mekaniks.clear();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

//                        firebaseFirestore.collection("Mekanik")
//                                .document(doc.getId()).get()
//                                .addOnCompleteListener(MekanikBengkelActivity.this, new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                            if (doc!=null){
                                            Mekanik mekanik = doc.toObject(Mekanik.class);
                                            mekaniks.add(mekanik);
                                            mekanikRecyclerAdapter.notifyDataSetChanged();
                                            }


//
//                                    }
//                                });

                    }
                }
            }
        });
    }

    private void initRecycler(){
        mekanikRecyclerAdapter=new MekanikRecyclerAdapter(mekaniks,this);
        recyclerViewMekanik.setAdapter(mekanikRecyclerAdapter);
        recyclerViewMekanik.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewMekanik.addItemDecoration(new SpaceItem(20));
        mekanikRecyclerAdapter.setOnItemClickListener(new MekanikRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Mekanik mekanik) {
                mekanikSelected=mekanik;
                hapusMekanik(mekanik.getId());
            }

            @Override
            public void onItemLongClick(Mekanik mekanik) {

            }
        });
    }

    private void toProfileMekanik(Mekanik mekanik){
        Intent intent=new Intent(MekanikBengkelActivity.this, MekanikActivity.class);
        intent.putExtra("mekanik",mekanik);
        startActivity(intent);
    }

    private void addMekanik() {
        final EditText edtEmail = new EditText(this);
        edtEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edtEmail.setHint("Email pegawai");

        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setPadding(10,10,10,10);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.addView(edtEmail,0);

        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Tambah Akun Pegawai");
        builder.setView(linearLayout);
        builder.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email=edtEmail.getText().toString();
                if (isEmpty(email)) edtEmail.setHint("Email harus diisi");
                else {
                    saveAccount(email,email);
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void hapusMekanik(String id){
        firebaseFirestore.collection("Mekanik").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mekanikRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(MekanikBengkelActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manual(){
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Manual ");
        builder.setMessage("Klik item untuk menghapus");
        builder.setPositiveButton("Mengerti", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                addMekanik();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        listenerRegistrationMekanik.remove();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        listenerRegistrationMekanik.remove();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.bottom_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("ONCHANGE ",mekaniks.size()+"");
                Toast.makeText(MekanikBengkelActivity.this, query, Toast.LENGTH_SHORT).show();
//                mekanikRecyclerAdapter.refresh(produks);
//                mekanikRecyclerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                getMekanik();
                break;
            case R.id.action_profile:{
                Intent intent=new Intent(MekanikBengkelActivity.this,ProfileBengkelActivity.class);
                startActivity(intent); }
            break;
            case R.id.action_logout:{
                firebaseAuth.signOut();
                Intent intent=new Intent(MekanikBengkelActivity.this, LandingPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); }
            break;
            case R.id.action_search:
                SearchView searchView=(SearchView) item.getActionView();
                Toast.makeText(MekanikBengkelActivity.this, "oke", Toast.LENGTH_SHORT).show();

                break;

        }
        return true;
    }
}
