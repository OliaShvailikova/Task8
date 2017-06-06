package com.example.home.myapplicati;

import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnPoiClickListener {
    private SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    private android.support.v4.app.FragmentManager fragmentManager;
    private int i = 0;
    AddMarker addMarker;
    private File file;
    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "locations.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportMapFragment = SupportMapFragment.newInstance();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyLocationListener.SetUpLocationListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        supportMapFragment.getMapAsync(this);
        fragmentManager = getSupportFragmentManager();
        if (!supportMapFragment.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.map, supportMapFragment).commit();
        } else {
            fragmentManager.beginTransaction().show(supportMapFragment).commit();
        }
        addMarker = new AddMarker();
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        sdPath.mkdirs();
        file = new File(sdPath, FILENAME_SD);
        new WorkWithFile().copyImageToSd(getApplicationContext());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getFragmentManager();
        int id = item.getItemId();
        if (supportMapFragment.isAdded())
            fragmentManager.beginTransaction().hide(supportMapFragment).commit();
        if (id == R.id.nav_list) {
            fragmentManager.beginTransaction().hide(supportMapFragment).commit();
            fm.beginTransaction().replace(R.id.context_frame, new ListFragment()).commit();
        } else if (id == R.id.nav_map) {
            if (!supportMapFragment.isAdded()) {
                fragmentManager.beginTransaction().add(R.id.map, supportMapFragment).commit();
            } else {
                fragmentManager.beginTransaction().show(supportMapFragment).commit();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        new WorkWithFile().saveMarkers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       new WorkWithFile().saveMarkers();
    }

    @Override
    protected void onStop() {
        super.onStop();
       new WorkWithFile().saveMarkers();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                i = i + 1;
                addMarker.addMarker(mMap,
                        newPlace(getResources().getString(R.string.location) + i, latLng.latitude,
                                latLng.longitude, new AddressOfPlace(getApplicationContext(), latLng).getAddress(), "", "zdanie"));
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (!file.canRead()) {
            JSONArray jsonArray = new ReadJSON(getApplicationContext()).readJson();
            setMarkers(jsonArray, googleMap);
        } else {
            try {
                JSONArray jsonArray = new JSONArray(new WorkWithFile().readFileSD());
                setMarkers(jsonArray, googleMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnPoiClickListener(this);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.4237120, 30.9978144), 13));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (MyLocationListener.imHere != null) {
            LatLng myLocation = new LatLng(MyLocationListener.imHere.getLatitude(), MyLocationListener.imHere.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
        }
        return false;
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        addMarker.addMarker(mMap, newPlace(pointOfInterest.name,
                pointOfInterest.latLng.latitude, pointOfInterest.latLng.longitude,
                new AddressOfPlace(getApplicationContext(), pointOfInterest.latLng).getAddress(), "", "zdanie"));
    }

    private Places newPlace(String name, double latitude, double longitude, String address, String description, String image) {
        return new Places(name, latitude, longitude, address, getDistance(latitude, longitude), description, image);
    }

    private float getDistance(double latitude, double longitude) {
        if (MyLocationListener.imHere != null) {
            Location locationA = new Location("My Location");
            locationA.setLatitude(MyLocationListener.imHere.getLatitude());
            locationA.setLongitude(MyLocationListener.imHere.getLongitude());
            Location locationB = new Location("Place");
            locationB.setLatitude(latitude);
            locationB.setLongitude(longitude);
            float distance = locationA.distanceTo(locationB);
            return distance;
        } else
            return (float) 0.0;
    }

    void setMarkers(JSONArray jsonArray, GoogleMap googleMap) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                addMarker.addMarker(googleMap, newPlace(jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).getDouble("lat"),
                        jsonArray.getJSONObject(i).getDouble("long"), new AddressOfPlace(getApplicationContext(),
                                new LatLng(jsonArray.getJSONObject(i).getDouble("lat"),
                                        jsonArray.getJSONObject(i).getDouble("long"))).getAddress(),
                        jsonArray.getJSONObject(i).getString("description"), jsonArray.getJSONObject(i).getString("image")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
