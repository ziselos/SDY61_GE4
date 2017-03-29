package com.example.zisis.sdy61_ge4;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by zisis on 153//17.
 */
public class GeofenceFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap mgoogleMap;
    MapView mapView;
    private double curLat,curLng;

    private static final String TAG = GeofenceFragment.class.getName();
    private static final String GEOFENCE_ID = "MyGeofenceId";
    private Location lastLocation;
    private LocationRequest locationRequest;

    //Fefined in milli seconds
    private final int UPDATE_INTERVAL = 3 * 60 * 1000;
    private final int FASTEST_INTERVAL = 30 * 1000;
    private static final float GEOFENCE_RADIUS = 200.0f; // in meters
    private static final long GEO_DURATION = 4 * 60 * 60 * 1000; //4 hours
    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;


    //Για την δημιουργία του Geofencing
    private GoogleApiClient googleApiClient = null;


    public GeofenceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_geofence, container, false);
        return view;
    }

    //συνάρτηση για να λάβω τις συντεταγμένες από το GPSFragment
    public void updateLatLng(Double d1,Double d2){

        curLat = d1;
        curLng = d2;
        mapView.getMapAsync(this);
        mapView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.Map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            //mapView.getMapAsync(this);
            //hide
            mapView.setVisibility(View.INVISIBLE);
        }

        createGoogleApi();


    }  //end of onCreatiView method



    // ------------------------------------------------ //
    //create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }


    /* end Geofence creation */

    // -------------------------------------------------------//
    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }
    /*  end of Geofence Request */
// ---------------------------------------------------- //


    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(getContext(), GeofenceService.class);
        return PendingIntent.getService(
                getContext(), GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                googleApiClient,
                request,
                createGeofencePendingIntent()
        ).setResultCallback((ResultCallback<? super Status>) this);
    }


    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {
            drawGeofence();
        } else {
            // inform about fail
        }
    }


    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;

    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if (geoFenceLimits != null)
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(37.957717, 23.72858))// Metro Station Neos Kosmos
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(GEOFENCE_RADIUS);
        geoFenceLimits = mgoogleMap.addCircle(circleOptions);
    }

    private void startLocationUpdates() {

        try {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(FASTEST_INTERVAL)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "Location update Lat/Long" + location.getLatitude() + " " + location.getLongitude());
                }


            });
        } catch (SecurityException e) {
            Log.d(TAG, "Security exception-" + e.getMessage());
        }

    }


    private void createGoogleApi() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());
        mgoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);



        Marker geoMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(37.957717, 23.72858))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Metro Neos Kosmos"));

        geoMarker.showInfoWindow();


        Marker currMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(curLat,curLng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title("Current position").snippet("You are here"));
        currMarker.showInfoWindow();

        

        drawGeofence();


        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(curLat, curLng));//τρέχουσα θέση
        builder.include(new LatLng(37.957717, 23.72858));//σταθμός μετρό Νέος Κόσμος
        final LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));


        //check geofence removal
        Location NewcurrLocation = new Location("Current Point");
        NewcurrLocation.setLatitude(curLat);
        NewcurrLocation.setLongitude(curLng);


        Location Neos_kosmos = new Location("Neos Kosmos");
        Neos_kosmos.setLatitude(37.957717);
        Neos_kosmos.setLongitude(23.72858);

        if (NewcurrLocation.distanceTo(Neos_kosmos) < 200){ //less than the perimeter of geofence

            Toast.makeText(getContext(),"You are in the geofence, at "+NewcurrLocation.distanceTo(Neos_kosmos)+ " meters from Neos Kosmos Metro Station",Toast.LENGTH_SHORT).show();
            //remove geofence
            //currMarker.remove();
            geoFenceLimits.remove();

        }




    }  //end onMapReady







    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.i(TAG, "onConnected()");
        getLastKnownLocation();

    }

    private void getLastKnownLocation(){

        Log.d(TAG, "getLastKnownLocation");
        try {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                //writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        }
        catch (SecurityException e){
            Log.d(TAG,"Security exception-"+e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.w(TAG, "onConnectionSuspended()");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");

    }


    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG, "onLocationChanged ["+location+"]");
        lastLocation = location;
       // writeActualLocation(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


} //end of GeofenceFragment class
