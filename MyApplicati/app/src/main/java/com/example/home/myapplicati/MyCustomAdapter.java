package com.example.home.myapplicati;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class MyCustomAdapter extends ArrayAdapter<Places> {

    private Context context;
    private List<Places> rentalProperties;

    public MyCustomAdapter(Context context, int resource, ArrayList<Places> objects) {
        super(context, resource, objects);
        this.context = context;
        this.rentalProperties = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Places places = rentalProperties.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.places_layout, null);
        TextView nameOfPlace = (TextView) view.findViewById(R.id.nameofplace);
        TextView address = (TextView) view.findViewById(R.id.address);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        String completeAddress = places.getAddressOfPlace();
        address.setText(completeAddress);
        nameOfPlace.setText(places.getNameOfPlace());
        image.setImageBitmap(new WorkWithFile().readImage("zdanie", context));

        return view;
    }
}


