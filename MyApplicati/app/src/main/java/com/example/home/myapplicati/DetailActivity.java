package com.example.home.myapplicati;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class DetailActivity extends AppCompatActivity {
    EditText descriptionTV,nameTV;
    KeyListener keyListener;
    final int REQUEST_CODE_PHOTO = 1;
    ImageView imageView;
    String name, image;
    Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.image);
        TextView addressTV = (TextView) findViewById(R.id.address);
        descriptionTV = (EditText) findViewById(R.id.description);
        keyListener = descriptionTV.getKeyListener();
        changeEditText(false, null, descriptionTV);
        nameTV = (EditText) findViewById(R.id.name);
        changeEditText(false, null, nameTV);
        TextView distanceTV = (TextView) findViewById(R.id.distance);
        intent = getIntent();
        String address = intent.getStringExtra("address");
        name = intent.getStringExtra("name");
        final String description = intent.getStringExtra("description");
        String distance = intent.getStringExtra("distance");
        image = intent.getStringExtra("image");
        imageView.setImageBitmap(new WorkWithFile().readImage(image, getApplicationContext()));
        addressTV.setText(address);
        descriptionTV.setText(description);
        nameTV.setText(name);
        distanceTV.setText(getResources().getString(R.string.distance) + distance);
        getSupportActionBar().setTitle(name);
        Button change = (Button) findViewById(R.id.change);
        View.OnClickListener changeButtonclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditText(true, keyListener, descriptionTV);
                changeEditText(true, keyListener, nameTV);
            }
        };
        change.setOnClickListener(changeButtonclickListener);
        Button save = (Button) findViewById(R.id.save);
        View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Places place = AddMarker.placesList.get(intent.getIntExtra("position", 0));
                place.setDescription(descriptionTV.getText().toString());
                place.setNameOfPlace(nameTV.getText().toString());
                changeEditText(false, null, descriptionTV);
                changeEditText(false, null, nameTV);
                new WorkWithFile().saveMarkers();
            }
        };
        save.setOnClickListener(saveButtonClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_photo) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri());
            startActivityForResult(intent, REQUEST_CODE_PHOTO);
            return true;
        } else if(id==R.id.action_delete){
            AddMarker.placesList.remove(intent.getIntExtra("position",0));
            new WorkWithFile().saveMarkers();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void changeEditText(Boolean bool, KeyListener listener, EditText editText) {
        editText.setEnabled(bool);
        editText.setCursorVisible(bool);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setKeyListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent1) {
        if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode == RESULT_OK) {
                imageView.setImageBitmap(new WorkWithFile().readImage(image, getApplicationContext()));
                Places place = AddMarker.placesList.get(intent.getIntExtra("position", 0));
                place.setImage(image);
                new WorkWithFile().saveMarkers();
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private Uri generateFileUri() {
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + "MyFiles");
        sdPath.mkdirs();
        File file = new File(sdPath, name + ".png");
        image = name;
        return Uri.fromFile(file);
    }
}