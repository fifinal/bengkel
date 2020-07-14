package com.example.bengkel.ui.user;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bengkel.R;
import com.example.bengkel.adapter.BengkelRecyclerAdapter;
import com.example.bengkel.algoritma.Directions;
import com.example.bengkel.algoritma.Edge;
import com.example.bengkel.algoritma.Graph;
import com.example.bengkel.algoritma.Node;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.BengkelLocation;
import com.example.bengkel.model.ClusterMarker;
import com.example.bengkel.model.Order;
import com.example.bengkel.model.UserLocation;
import com.example.bengkel.ui.bengkel.ProfileBengkelActivity;
import com.example.bengkel.ui.bengkel.SetupActivity;
import com.example.bengkel.ui.mekanik.MekanikActivity;
import com.example.bengkel.util.MyClusterManagerRenderer;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.ClusterManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.bengkel.util.Constants.MAPVIEW_BUNDLE_KEY;

public class LocationMapsFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, View.OnClickListener {
    // var static
    private static final String TAG = "Location Fragment";
    private static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    private static final int MAP_LAYOUT_STATE_EXPANDED = 1;
    private static final float DEFAULT_ZOOM = 16.0f;

    // firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ListenerRegistration mBengkelEventListener;

    // widgets
    private RecyclerView mBengkelListRecyclerView;
    private MapView mMapView;
    private RelativeLayout mMapContainer, layoutNotif;
    private Button btnCariBengkel;
    private ImageButton btnPesan;
    private ImageView imgBtnNotif;
    private ProgressBar progressBar;

    // var
    private ArrayList<Bengkel> bengkels = new ArrayList<>();
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();
    private ArrayList<BengkelLocation> bengkelLocations;
    private BengkelLocation bengkelLocationSelected;
    private Bengkel bengkelSelected;
    private BengkelRecyclerAdapter bengkelRecyclerAdapter;
    private UserLocation mUserPosition;
    private LatLng currenctUserLocation;
    private int mMapLayoutState = 0,selected=0;

    // maps
    private GoogleMap mGoogleMap;
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();
    private LatLngBounds mMapBoundary;
    Polyline polyline;
    Marker marker;

    // Location gps
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private Location mLastKnownLocation;

    // graph
    private Map<String, Node> nodes;
    private List<Edge> edgeList;
    private List<Node> nodeList;
    private Graph graph;
    private Directions directions;
    private List<Edge> edgesResult;
    private Map<String, List<GeoPoint>> edgeMap;
    private Node nodeStart;
    private Node nodeEnd;

    public LocationMapsFragment() {
        // Required empty public constructor
    }

    public static LocationMapsFragment newInstance() {
        return new LocationMapsFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_maps, container, false);

        // init widget
        mMapView = view.findViewById(R.id.user_list_map);
        btnCariBengkel=view.findViewById(R.id.btn_cari_bengkel);
        mBengkelListRecyclerView = view.findViewById(R.id.user_list_recycler_view);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        // init variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        bengkelLocations = new ArrayList<>();
        nodeList = new ArrayList<>();
        edgeList = new ArrayList<>();
        edgeMap = new HashMap<>();
        nodes = new HashMap<>();
        graph = new Graph();
        showDialog();
        // get all bengkel
        getNode();

        // recycler
        initBengkelListRecyclerView();
        initGoogleMap(savedInstanceState);

        return view;
    }

    private void ruteFromBengkel(){
        if (getArguments() != null) {
            Order order = getArguments().getParcelable("order");
            BengkelLocation bengkel = getArguments().getParcelable("bengkel");
            btnCariBengkel.setVisibility(View.GONE);
            nodeStart = new Node("start",bengkel.getGeoPoint());
            nodeEnd = new Node("end", order.getGeoPoint());
            resetGraph();
            directions = new Directions(graph); // init graph sebagai input untuk menghitung direction
            directions.setOrigin(nodeStart); // set node awal
            directions.setDestination(nodeEnd); // set node tujuan
            directions.shortestRoutes(); // hitung semua rute menggunakan bellmanford
            edgesResult = directions.getRoutes(); // Mendapatkan hasil rute terpendek
            addPolyLines(); // gambar rute di maps
            progressBar.setVisibility(View.GONE);

        }else {
            progressBar.setVisibility(View.GONE);
            btnCariBengkel.setVisibility(View.VISIBLE);
            btnCariBengkel.setOnClickListener(this);
        }
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    private void getBengkel() {
        progressBar.setVisibility(View.VISIBLE);
        bengkelLocations.clear();
        firebaseFirestore.collection("BengkelLocation").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                BengkelLocation bengkelLocation = doc.toObject(BengkelLocation.class);
                                if (bengkelLocation != null) {
                                    bengkelLocation.getBengkel().setBengkelId(doc.getId());
                                    bengkelLocations.add(bengkelLocation);
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            bengkelRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getNode() {
        firebaseFirestore.collection("Nodes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Node node = doc.toObject(Node.class);
                        nodeList.add(node);
                        nodes.put(node.getNode(), node);
                    }
                    graph.setNodeList(nodeList);
                    getEdge();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEdge() {
        firebaseFirestore.collection("Edges").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    edgeList.clear();
                    edgeMap.clear();
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        List<GeoPoint> route = (List<GeoPoint>) doc.get("latLng");
                        Edge edge = new Edge();
                        edge.setNode(doc.getString("node"));
                        edge.setWeight(doc.get("weight", Float.TYPE));
                        edge.setLatLng(route);
                        edgeList.add(edge);
                        edgeMap.put(doc.getString("node"), route);
                    }
                    graph.setEdges(edgeList);
                    ruteFromBengkel();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentLocationUser() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // ---------------------------------- LocationRequest
        // ------------------------------------
        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(5000);
        mLocationRequestHighAccuracy.setFastestInterval(2000);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null)
                    currenctUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
        };

        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, locationCallback, Looper.myLooper());
    }

    private void initBengkelListRecyclerView() {
        bengkelRecyclerAdapter = new BengkelRecyclerAdapter(bengkelLocations, getActivity());
        mBengkelListRecyclerView.setAdapter(bengkelRecyclerAdapter);
        mBengkelListRecyclerView
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        bengkelRecyclerAdapter.setOnItemClickListener(new BengkelRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BengkelLocation bengkelLocation, int position) {
                dialog(bengkelLocation);

            }

            @Override
            public void onItemLongClick(Bengkel bengkel) {


            }
        });
    }

    private void dialog(final BengkelLocation bengkelLocation){
        CharSequence[] items=new CharSequence[]{"Lihat Rute","Lihat Bengkel"};
        new MaterialAlertDialogBuilder(getActivity())
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selected==0){
                            nodeStart = new Node("start",
                                    new GeoPoint(currenctUserLocation.latitude, currenctUserLocation.longitude));
                            nodeEnd = new Node("end", bengkelLocation.getGeoPoint());
                            bengkelLocationSelected = bengkelLocation;
                            if (!bengkelLocation.getBengkel().isPenuh()){
                                showRoutes();
                            }else {
                                Toast.makeText(getActivity(), "Bengkel Sedang Penuh", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (selected==1){
                            Intent intent=new Intent(getActivity(), ProfileBengkelActivity.class);
                            intent.putExtra("bengkel",bengkelLocation.getBengkel());
                            startActivity(intent);
                        }
                        selected=0;
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

    private void setMarker(String route, String distance) {
        if (bengkelLocationSelected != null) {
            if (marker != null)
                marker.remove();
            marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(bengkelLocationSelected.getGeoPoint().getLatitude(),
                            bengkelLocationSelected.getGeoPoint().getLongitude()))
                    .title("Klik untuk menghubungi ")
                    .snippet(bengkelLocationSelected.getBengkel().getNamaBengkel() + "\nRute \n"+route+"\nJarak : " + distance));
            marker.showInfoWindow();
        }
    }

    private void resetGraph() {
        graph = new Graph();
        graph.setNodeList(nodeList);
        graph.setEdges(edgeList);
    }

    private void showRoutes() {
        if (currenctUserLocation != null) {
            resetGraph();
            directions = new Directions(graph); // init graph sebagai input untuk menghitung direction
            directions.setOrigin(nodeStart); // set node awal
            directions.setDestination(nodeEnd); // set node tujuan
            directions.shortestRoutes(); // hitung semua rute menggunakan bellmanford
            edgesResult = directions.getRoutes(); // Mendapatkan hasil rute terpendek
            addPolyLines(); // gambar rute di maps
        } else {
            Toast.makeText(getContext(), "Lokasi anda tidak dikatehui", Toast.LENGTH_SHORT).show();
        }
    }

    private void addPolyLines() {
        List<LatLng> newDecodedPath = new ArrayList<>();
        float distance = 0;
        String route="";
        if (polyline != null)
            polyline.remove();
        for (Edge edge : edgesResult) {
            if(!edge.getNode().equals("start-start"))route+="Node : "+edge.getNode()+"\n";
            if (edgeMap.containsKey(edge.getNode())) {
                for (GeoPoint point : edgeMap.get(edge.getNode())) {
                    LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                    newDecodedPath.add(latLng);
                }
            } else if (edge.getNode().contains("start")) {
                LatLng latLng = new LatLng(nodeStart.getGeo().getLatitude(), nodeStart.getGeo().getLongitude());
                newDecodedPath.add(latLng);
            } else if (edge.getNode().contains("end")) {
                distance = edge.getWeight() / 1000;
                LatLng latLng = new LatLng(nodeEnd.getGeo().getLatitude(), nodeEnd.getGeo().getLongitude());
                newDecodedPath.add(latLng);
            }
        }

        route=route.replaceAll("-"," dari ").replaceAll("end","tujuan");
        polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
        polyline.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        polyline.setClickable(true);
        setMarker(route,String.format("%.2f", distance));
        zoomRoute(polyline.getPoints());
    }

    public void zoomRoute(List<LatLng> lstLatLngRoute) {
        if (mGoogleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty())
            return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 50;
        LatLngBounds latLngBounds = boundsBuilder.build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding), 600, null);
    }

    private void setCameraView() {
        // Set a boundary to start
        double bottomBoundary = nodeStart.getGeo().getLatitude() - .1;
        double leftBoundary = nodeStart.getGeo().getLongitude() - .1;
        double topBoundary = nodeStart.getGeo().getLatitude() + .1;
        double rightBoundary = nodeStart.getGeo().getLongitude() + .1;

        mMapBoundary = new LatLngBounds(new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary));

        mGoogleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(nodeStart.getGeo().getLatitude(), nodeStart.getGeo().getLongitude()), 16));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));
    }

    private void setCurrentPosition() {
        currenctUserLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currenctUserLocation, DEFAULT_ZOOM));
    }

    private void sendMessage(String nama, String idBengkel) {

        DocumentReference reference = firebaseFirestore.collection("Order")
                .document(firebaseAuth.getCurrentUser().getUid());
        Order order = new Order();
        order.setNama(nama);
        order.setStatus("baru");
        order.setGeoPoint(new GeoPoint(currenctUserLocation.latitude, currenctUserLocation.longitude));
        order.setId(reference.getId());
        reference.set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(getActivity(), "Pesan Terkirim", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        firebaseFirestore.collection("Bengkel/" + idBengkel + "/Order").document(reference.getId()).set(order)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getActivity(), "Pesan Terkirim", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void checkGps() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getCurrentLocationUser();
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(getActivity(), 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                        Toast.makeText(getActivity(), e1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK)
                getCurrentLocationUser();
        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        final EditText edtNama = new EditText(getActivity());
        edtNama.setInputType(InputType.TYPE_CLASS_TEXT);
        edtNama.setHint("Nama Anda");

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.addView(edtNama, 0);

        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setMessage("hubungi " + marker.getSnippet())
                .setCancelable(true).setView(linearLayout)
                .setPositiveButton("oke", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                            @SuppressWarnings("unused") final int id) {
                        String nama = edtNama.getText().toString().trim();
                        if (nama.isEmpty())
                            edtNama.setHint("Nama Harus diisi");
                        else {
                            sendMessage(nama, bengkelLocationSelected.getBengkel().getBengkelId());
                            dialog.dismiss();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        builder.show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setOnInfoWindowClickListener(this);
        hideDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        checkGps();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cari_bengkel:
                getBengkel();
                break;
        }
    }

    private void showDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
