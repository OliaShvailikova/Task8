package com.example.home.myapplicati;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ListFragment extends Fragment {
    ArrayList<Places>list1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_import, container, false);
        ListView list=(ListView)myView.findViewById(R.id.listView);
        list1= AddMarker.placesList;
        if (list1.get(1).getDistance()!=0.0) {
            Collections.sort(list1, new Comparator<Places>() {
                @Override
                public int compare(Places o1, Places o2) {
                    return Float.compare(o1.getDistance(),o2.getDistance());
                }
            });
        }
        ArrayAdapter<Places> adapter = new MyCustomAdapter(getActivity(), 0, list1);
        list.setAdapter(adapter);
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            //on click
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Places places = list1.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("name", places.getNameOfPlace());
                intent.putExtra("address", places.getAddressOfPlace());
                intent.putExtra("distance", places.getDistance());
                intent.putExtra("description", places.getDescription());
                intent.putExtra("position",position);
                intent.putExtra("image",places.getImage());
                startActivity(intent);
            }
        };
        list.setOnItemClickListener(adapterViewListener);
        return myView;
    }
}
