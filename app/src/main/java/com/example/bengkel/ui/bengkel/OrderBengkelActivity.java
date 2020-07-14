package com.example.bengkel.ui.bengkel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.bengkel.adapter.OrderRecyclerAdapter;
import com.example.bengkel.adapter.ProdukRecyclerAdapter;
import com.example.bengkel.algoritma.Node;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.BengkelLocation;
import com.example.bengkel.model.Chatroom;
import com.example.bengkel.model.Mekanik;
import com.example.bengkel.model.Order;
import com.example.bengkel.model.Produk;
import com.example.bengkel.ui.LandingPageActivity;
import com.example.bengkel.ui.user.LocationMapsFragment;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class OrderBengkelActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, IMekanik {
    //widget
    private RecyclerView recyclerViewProduct;
    private BottomAppBar bottomAppBar;
    private Toolbar toolbar;

    //var
    private OrderRecyclerAdapter orderRecyclerAdapter;
    private ArrayList<Order> orders=new ArrayList<>();
    private Order orderSelected;
    BengkelLocation bengkel;
    int selected=0;
    private MekanikListFragment mekanikListFragment;

    //firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ListenerRegistration listenerRegistrationOrder;

    public OrderBengkelActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_bengkel);
        manual();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        toolbar=findViewById(R.id.toolbar);
        getToolbar();

        recyclerViewProduct=findViewById(R.id.recycler_pesanan);
        bottomAppBar=findViewById(R.id.btn_app_bar);
        setSupportActionBar(bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(OrderBengkelActivity.this,BengkelDashboardActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                finish();
            }
        });
        bottomAppBar.setOnMenuItemClickListener(this);

        initRecycler();
        getOrder();
    }

    private void inflateMapsFragment(Order order){

        LocationMapsFragment fragment = LocationMapsFragment.newInstance();
        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(getString(R.string.intent_user_list), mUserList);
        bundle.putParcelable("bengkel",bengkel);
        bundle.putParcelable("order",order);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        transaction.replace(R.id.maps_fragment, fragment, "Lokasi");
        transaction.addToBackStack("Lokasi");
        transaction.commit();
    }

    private void getOrder(){

        listenerRegistrationOrder=firebaseFirestore.collection("Bengkel/"+firebaseAuth.getCurrentUser().getUid()+"/Order").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                orders.clear();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Order order= doc.toObject(Order.class);
                        orders.add(order);
                        orderRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void getToolbar() {
        firebaseFirestore.collection("BengkelLocation").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    bengkel=task.getResult().toObject(BengkelLocation.class);
                    toolbar.setTitle(bengkel.getBengkel().getNamaBengkel());
                    toolbar.setSubtitle(bengkel.getBengkel().getAlamatBengkel());
                }
            }
        });
    }

    private void initRecycler(){
        mekanikListFragment=new MekanikListFragment();
        orderRecyclerAdapter=new OrderRecyclerAdapter(orders);
        recyclerViewProduct.setAdapter(orderRecyclerAdapter);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerAdapter.setOnItemClickListener(new OrderRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                dialog(order);
            }

            @Override
            public void onItemLongClick(Order order) {

            }
        });
    }

    private void dialog(final Order order){
        CharSequence[] items=new CharSequence[]{"Lihat Rute","Pilih Mekanik"};
        new MaterialAlertDialogBuilder(this)
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                    }
                })
                .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selected==0){
                            orderSelected=order;
                            checkPermission(order);
                        }
                        else if (selected==1){
                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
                                    .replace(R.id.fragment_container, mekanikListFragment, getString(R.string.fragment_image_list))
                                    .commit();
                        }

                    }
                })
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected=which;
                    }
                })
                .show();
    }

    private void hapusProduk(String id){
        firebaseFirestore.collection("Order").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                orderRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(OrderBengkelActivity.this, "Produk Berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkPermission(final Order order) {
        Dexter.withActivity(OrderBengkelActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        inflateMapsFragment(order);

                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(OrderBengkelActivity.this);
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
                            Toast.makeText(OrderBengkelActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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
        builder.setMessage("Klik item untuk memilih mekanik");
        builder.setPositiveButton("Mengerti", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        listenerRegistrationOrder.remove();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        listenerRegistrationOrder.remove();
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
                Log.d("ONCHANGE ",orders.size()+"");
                Toast.makeText(OrderBengkelActivity.this, query, Toast.LENGTH_SHORT).show();
//                orderRecyclerAdapter.refresh(produks);
//                orderRecyclerAdapter.getFilter().filter(query);
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
                getOrder();
                break;
            case R.id.action_profile:{
                Intent intent=new Intent(OrderBengkelActivity.this,ProfileBengkelActivity.class);
                startActivity(intent); }
            break;
            case R.id.action_logout:{
                firebaseAuth.signOut();
                Intent intent=new Intent(OrderBengkelActivity.this, LandingPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); }
            break;
            case R.id.action_search:
                SearchView searchView=(SearchView) item.getActionView();
                Toast.makeText(OrderBengkelActivity.this, "oke", Toast.LENGTH_SHORT).show();

                break;

        }
        return true;
    }

    @Override
    public void onMekanikSelected(Mekanik mekanik) {
        // remove the image selector fragment
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
                .remove(mekanikListFragment)
                .commit();
        orderSelected.setStatus("proses");
        orderSelected.setIdMekanik(mekanik.getId());
        firebaseFirestore.collection("Bengkel/"+firebaseAuth.getCurrentUser().getUid()+"/Order")
                .document(orderSelected.getId()).set(orderSelected,SetOptions.merge())
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseFirestore.collection("Order") .document(orderSelected.getId()).set(orderSelected,SetOptions.merge());
                        Toast.makeText(OrderBengkelActivity.this, "Berhasil diproses", Toast.LENGTH_SHORT).show();
                    }
                });
        Chatroom chatroom=new Chatroom();
        chatroom.setTitle(mekanik.getNama());
        chatroom.setChatroom_id(orderSelected.getId());
        chatroom.setContent("Pesanan sedang di proses");
        chatroom.setIcon(mekanik.getImgProfile());
        firebaseFirestore.collection("Chatroom")
                .document().set(chatroom)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(OrderBengkelActivity.this, "chatroom berhasil dibuat", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
