package com.laptopfix.laptopfixrun.Activities.Customer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import java.util.Arrays;

public class ChangeAddressActivity extends AppCompatActivity implements View.OnClickListener, PlaceSelectionListener {

    private Toolbar toolbar;
    private PlacesClient placesClient;
    private Button btnSave;
    private String address;
    private AutocompleteSupportFragment autocompleteSupportFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        showToolbar(getString(R.string.address), true);

        Constants.newAddress = null;

        btnSave = findViewById(R.id.btnSave);
        if(!Places.isInitialized()){
            Places.initialize(this, getString(R.string.google_server_key));
        }
        placesClient = Places.createClient(this);
        autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocompletePlaces);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteSupportFragment.setCountry("MX");
        autocompleteSupportFragment.setOnPlaceSelectedListener(this);
        btnSave.setOnClickListener(this);
    }

    private void showToolbar(String title, boolean upButton) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                if(Constants.newAddress != null){
                    onBackPressed();
                }else{
                    Toast.makeText(this, "Elija una dirección", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPlaceSelected(@NonNull Place place) {
        Constants.newAddress = place.getLatLng();
    }

    @Override
    public void onError(@NonNull Status status) {
        Toast.makeText(this, status.toString(), Toast.LENGTH_SHORT).show();
    }
}