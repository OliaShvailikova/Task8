package com.example.home.myapplicati;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class AddMarker {

    static ArrayList<Places> placesList;

    public AddMarker() {
        placesList = new ArrayList<>();
    }

    void addMarker(GoogleMap googleMap, Places places) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(places.getLatitudeOfPlace(), places.getLongitudeOfPlace()))
                .title(places.getNameOfPlace())
                .snippet(places.getAddressOfPlace()));
        placesList.add(places);


    }
}
