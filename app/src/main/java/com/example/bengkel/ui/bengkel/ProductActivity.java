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
import com.example.bengkel.adapter.ProdukRecyclerAdapter;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.Produk;
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


public class ProductActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

    //widget
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView recyclerViewProduct;
    private BottomAppBar bottomAppBar;

    //var
    private ProdukRecyclerAdapter produkRecyclerAdapter;
    private ArrayList<Produk> produks=new ArrayList<>();
    private Produk produkSelected;

    //firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ListenerRegistration listenerRegistrationProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        manual();
        fab=findViewById(R.id.fab);
        recyclerViewProduct=findViewById(R.id.rv_produk);
        bottomAppBar=findViewById(R.id.btn_app_bar);
        setSupportActionBar(bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(ProductActivity.this,BengkelDashboardActivity.class);
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
        getProduct();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.DOWN) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==ItemTouchHelper.DOWN){
                    Toast.makeText(ProductActivity.this, "down", Toast.LENGTH_SHORT).show();
                }
                final Produk produk=produks.get(viewHolder.getAdapterPosition());
                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(viewHolder.itemView.getContext());
                builder.setTitle("Hapus Produk");
                builder.setMessage("Yakin ingin menghapus "+produk.getNamaProduk()+" ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id=produk.getId();
                        hapusProduk(id);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        produkRecyclerAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        }).attachToRecyclerView(recyclerViewProduct);
    }

    private void getProduct(){
        listenerRegistrationProduk=firebaseFirestore.collection("Bengkel/"+firebaseAuth.getUid()+"/Product").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                produks.clear();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Produk produk = doc.toObject(Produk.class);
                        produks.add(produk);
                        produkRecyclerAdapter.notifyDataSetChanged();
                    }
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
        produkRecyclerAdapter=new ProdukRecyclerAdapter(produks);
        recyclerViewProduct.setAdapter(produkRecyclerAdapter);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(this));
        produkRecyclerAdapter.setOnItemClickListener(new ProdukRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Produk produk) {
                produkSelected=produk;
                addProduct("update");
            }

            @Override
            public void onItemLongClick(Produk produk) {

            }
        });
    }

    private void addProduct(final String state) {
        final EditText edtProduk = new EditText(this);
        final EditText edtStok = new EditText(this);
        final EditText edtHarga = new EditText(this);
        edtProduk.setInputType(InputType.TYPE_CLASS_TEXT);
        edtStok.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtHarga.setInputType(InputType.TYPE_CLASS_NUMBER);

        String title="";
        String button="";
        if (state.equals("tambah")){
            edtProduk.setHint("nama produk");
            edtStok.setHint("stok");
            edtHarga.setHint("harga");
            title="Tambah Produk";
            button="Tambah";

        }else{
            edtProduk.setText(produkSelected.getNamaProduk());
            edtStok.setText(produkSelected.getStok()+"");
            edtHarga.setText(produkSelected.getHarga()+"");
            title="Update Produk";
            button="Update";
        }

        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setPadding(10,10,10,10);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.addView(edtProduk,0);
        linearLayout.addView(edtStok,1);
        linearLayout.addView(edtHarga,2);

        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(title);
        builder.setView(linearLayout);

        builder.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id;
                if(state.equals("tambah")){
                    DocumentReference documentReference=firebaseFirestore.collection("Product").document();
                    id=documentReference.getId();
                }else{
                    id=produkSelected.getId();
                }
                String produk=edtProduk.getText().toString();
                int stok=Integer.parseInt(edtStok.getText().toString());
                int harga=Integer.parseInt(edtHarga.getText().toString());
                if (isEmpty(produk)) edtProduk.setHint("Nama produk tidak boleh kosong");
                else tambahProduk(new Produk(id,produk,stok,harga));
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

    private void tambahProduk(final Produk produk) {
        firebaseFirestore.collection("Product").document(produk.getId()).set(produk, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseFirestore.collection("Bengkel/"+firebaseAuth.getUid()+"/Product")
                        .document(produk.getId()).set(produk,SetOptions.merge());
                Toast.makeText(ProductActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                produkRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void hapusProduk(final String id){
        firebaseFirestore.collection("Product").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseFirestore.collection("Bengkel/"+firebaseAuth.getUid()+"/Product").document(id).delete();
                produkRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(ProductActivity.this, "Produk Berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private LinearLayout formProduk(){
        final EditText produk = new EditText(this);
        final EditText stok = new EditText(this);
        final EditText harga = new EditText(this);
        produk.setInputType(InputType.TYPE_CLASS_TEXT);
        stok.setInputType(InputType.TYPE_CLASS_NUMBER);
        harga.setInputType(InputType.TYPE_CLASS_NUMBER);

        produk.setHint("nama produk");
        stok.setHint("stok");
        harga.setHint("harga");

        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setPadding(10,10,10,10);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.addView(produk,0);
        linearLayout.addView(stok,1);
        linearLayout.addView(harga,2);
        return  linearLayout;

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
                addProduct("tambah");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        listenerRegistrationProduk.remove();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        listenerRegistrationProduk.remove();
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
                Log.d("ONCHANGE ",produks.size()+"");
                Toast.makeText(ProductActivity.this, query, Toast.LENGTH_SHORT).show();
                produkRecyclerAdapter.refresh(produks);
                produkRecyclerAdapter.getFilter().filter(query);
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
                getProduct();
                break;
            case R.id.action_profile:{
                Intent intent=new Intent(ProductActivity.this,ProfileBengkelActivity.class);
                startActivity(intent); }
                break;
            case R.id.action_logout:{
                firebaseAuth.signOut();
                Intent intent=new Intent(ProductActivity.this, LandingPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); }
                break;
            case R.id.action_search:
                SearchView searchView=(SearchView) item.getActionView();
                Toast.makeText(ProductActivity.this, "oke", Toast.LENGTH_SHORT).show();

                break;

        }
        return true;
    }
}
