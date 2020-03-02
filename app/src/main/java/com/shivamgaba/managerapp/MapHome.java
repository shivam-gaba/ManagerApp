package com.shivamgaba.managerapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.ResourceBitmapDecoder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MapHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, PermissionsListener {
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;

    ArrayList<marker> markers = new ArrayList<marker>();
    ArrayList<DriverLiveLocation> driverLiveLocations=new ArrayList<>();

    FirebaseOptions firebaseOptions;
    FirebaseApp driverApp;
    FirebaseDatabase driverDatabase;
    DatabaseReference driverDatabaseReference;


    TextView tvTotalDrivers, tvActiveDrivers;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoic2hpdjg5NjhzaCIsImEiOiJjazVpZmY1eWIwY3Z3M21udnUwMHo5dzhnIn0.pkfihC9BK2VQTZwy-DMJLw");
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        try {
            firebaseOptions = new FirebaseOptions.Builder()
                    .setApiKey("AIzaSyAub2yA7zUX_1a_W6hlcKoPrp7UyyKHjGw")
                    .setApplicationId("1:792582559632:android:8dc382230bc9e3b482c515")
                    .setDatabaseUrl("https://driver-app-9e22d.firebaseio.com")
                    .build();

            driverApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "Driver App");
            driverDatabase = FirebaseDatabase.getInstance(driverApp);
            driverDatabaseReference = driverDatabase.getReference();
        } catch (Exception e) {
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(MapHome.this);

        tvTotalDrivers = findViewById(R.id.tvTotalDrivers);
        tvActiveDrivers = findViewById(R.id.tvActiveDrivers);

        getDriverLiveLocation();

/*
        final AlertDialog alertDialog = new SpotsDialog(MapHome.this);
        alertDialog.show();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();

                    addMarkersFromDatabase();
                }
            }
        };
        handler.postDelayed(runnable, 3000);

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

 */
    }

    // map functions===================================================================================================

    private void getDriverLiveLocation()
    {
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null && driverApp!=null) {
            DatabaseReference databaseReference = driverDatabaseReference.child("Managers/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    driverLiveLocations.clear();

                    for (DataSnapshot parentNode : dataSnapshot.getChildren()) {
                        //if the live location is provided
                        if (parentNode.hasChild("liveLocation")) {
                            DriverLiveLocation d = parentNode.child("liveLocation").getValue(DriverLiveLocation.class);
                            driverLiveLocations.add(d);
                        }
                    }

                    ArrayList<Double> lat = new ArrayList<Double>();
                    ArrayList<Double> lng = new ArrayList<Double>();

                    for (int i = 0; i < driverLiveLocations.size(); i++) {
                        lat.add(driverLiveLocations.get(i).getLiveLat());
                        lng.add(driverLiveLocations.get(i).getLiveLng());
                    }

                    for (int i = 0; i < driverLiveLocations.size(); i++) {


                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(lat.get(i), lng.get(i)));
                        mapboxMap.addMarker(markerOptions);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MapHome.this, "ERROR: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

   /* private void addMarkersFromDatabase() {
        DatabaseReference databaseReference = driverDatabaseReference.child("Managers/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                markers.clear();

                for (DataSnapshot parentNode : dataSnapshot.getChildren()) {
                    DataSnapshot childNode=parentNode.child("Markers");
                    for (DataSnapshot keyNode:childNode.getChildren()) {
                        marker m = keyNode.getValue(marker.class);
                        markers.add(m);
                    }
                }

                ArrayList<Double> lat = new ArrayList<Double>();
                ArrayList<Double> lng = new ArrayList<Double>();

                for (int i = 0; i < markers.size(); i++) {
                    lat.add(markers.get(i).getLat());
                    lng.add(markers.get(i).getLng());
                }

                for (int i = 0; i < markers.size(); i++) {

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(lat.get(i), lng.get(i)))
                            ;
                    mapboxMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapHome.this, "ERROR: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    */
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            enableLocationComponent(style);
        });
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

        } else {
            permissionsManager = new PermissionsManager(MapHome.this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    //Navigation Drawer=======================================================


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapHome.this, MainActivity.class));
                MapHome.this.finish();
                break;

            case R.id.drivers:
                break;

            case R.id.managerProfile:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}