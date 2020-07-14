package com.example.bengkel.ui.bengkel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.bengkel.adapter.LayananRecyclerAdapter;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.Layanan;
import com.example.bengkel.ui.LandingPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class LayananActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    //widget
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView recyclerViewLayanan;
    private BottomAppBar bottomAppBar;

    //var
    private LayananRecyclerAdapter layananRecyclerAdapter;
    private ArrayList<Layanan> layanans=new ArrayList<>();
    private Layanan layananSelected;
    //firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ListenerRegistration listenerRegistrationLayanan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan);
        manual();
        fab=findViewById(R.id.fab);
        recyclerViewLayanan=findViewById(R.id.rv_layanan);
        bottomAppBar=findViewById(R.id.btn_app_bar);
        setSupportActionBar(bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(LayananActivity.this,BengkelDashboardActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                finish();
            }
        });
        bottomAppBar.setOnMenuItemClickListener(this);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        toolbar=findViewById(R.id.toolbar);
        getToolbar();

        fab.setOnClickListener(this);

        initRecycler();
        getLayanan();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.DOWN) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==ItemTouchHelper.DOWN){
                }
                final Layanan layanan=layanans.get(viewHolder.getAdapterPosition());
                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(viewHolder.itemView.getContext());
                builder.setTitle("Hapus Layanan");
                builder.setMessage("Yakin ingin menghapus "+layanan.getNama()+" ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id=layanan.getId();
                        hapusLayanan(id);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        layananRecyclerAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        }).attachToRecyclerView(recyclerViewLayanan);
    }

    private void getLayanan(){
        listenerRegistrationLayanan=firebaseFirestore.collection("Bengkel/"+firebaseAuth.getUid()+"/Layanan").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                layanans.clear();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Layanan layanan = doc.toObject(Layanan.class);
                        layanans.add(layanan);
                    }
                    layananRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
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

    private void initRecycler(){
        layananRecyclerAdapter=new LayananRecyclerAdapter(layanans);
        recyclerViewLayanan.setAdapter(layananRecyclerAdapter);
        recyclerViewLayanan.setLayoutManager(new LinearLayoutManager(this));
        layananRecyclerAdapter.setOnItemClickListener(new LayananRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Layanan layanan) {
                layananSelected=layanan;
                addLayanan("update");
            }

            @Override
            public void onItemLongClick(Layanan layanan) {

            }
        });
    }

    private void addLayanan(final String state) {
        final EditText edtLayanan = new EditText(this);
        edtLayanan.setInputType(InputType.TYPE_CLASS_TEXT);

        String title="";
        String button="";
        if (state.equals("tambah")){
            edtLayanan.setHint("nama layanan");
            title="Tambah Layanan";
            button="Tambah";

        }else{
            edtLayanan.setText(layananSelected.getNama());
            title="Update Layanan";
            button="Update";
        }

        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setPadding(10,10,10,10);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.addView(edtLayanan,0);

        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(title);
        builder.setView(linearLayout);

        builder.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id;
                if(state.equals("tambah")){
                    DocumentReference documentReference=firebaseFirestore.collection("Layanan").document();
                    id=documentReference.getId();
                }else{
                    id=layananSelected.getId();
                }
                String layanan=edtLayanan.getText().toString();
                if (isEmpty(layanan)) edtLayanan.setHint("Nama layanan tidak boleh kosong");
                else tambahLayanan(new Layanan(id,layanan));
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

    private void tambahLayanan(final Layanan layanan) {
        firebaseFirestore.collection("Layanan").document(layanan.getId()).set(layanan, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseFirestore.collection("Bengkel/"+firebaseAuth.getUid()+"/Layanan")
                        .document(layanan.getId()).set(layanan,SetOptions.merge());
                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                layananRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void hapusLayanan(final String id){
        firebaseFirestore.collection("Layanan").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseFirestore.collection("Bengkel/"+firebaseAuth.getUid()+"/Layanan").document(id).delete();
                layananRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Layanan Berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void manual(){
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                addLayanan("tambah");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        listenerRegistrationLayanan.remove();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        listenerRegistrationLayanan.remove();
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
                Log.d("ONCHANGE ",layanans.size()+"");
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                layananRecyclerAdapter.refresh(layanans);
                layananRecyclerAdapter.getFilter().filter(query);
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
                getLayanan();
                break;
            case R.id.action_profile:{
                Intent intent=new Intent(this,ProfileBengkelActivity.class);
                startActivity(intent); }
            break;
            case R.id.action_logout:{
                firebaseAuth.signOut();
                Intent intent=new Intent(this, LandingPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); }
            break;
            case R.id.action_search:
                SearchView searchView=(SearchView) item.getActionView();
                Toast.makeText(this, "oke", Toast.LENGTH_SHORT).show();

                break;

        }
        return true;
    }
}
