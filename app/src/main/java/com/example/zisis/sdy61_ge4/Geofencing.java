package com.example.zisis.sdy61_ge4;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class Geofencing extends AppCompatActivity implements GPSFragment.onLatLngSetListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing);


    }

    @Override
    public void setLatLng(Double Lat, Double Lng) {

        //Δημιουργία αντικειμένου GeofenceFragment για να λάβει τις
        //συντεταγμένες από το GPSFragment
        GeofenceFragment geofenceFragment = (GeofenceFragment) getSupportFragmentManager().findFragmentById(R.id.GeofencingPlace);
        geofenceFragment.updateLatLng(Lat,Lng);

    }
}
