package com.example.zisis.sdy61_ge4;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GPSFragment extends Fragment {


    private LocationManager locationManager;
    private LocationListener locationListener;
    onLatLngSetListener onLatLngSetListener;


    GPSTracker gps;

    public GPSFragment() {
        // Required empty public constructor


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gps, container, false);
        Button btnCurrentLocation = (Button)view.findViewById(R.id.btnCurrentLocation);
        TextView textLat = (TextView)view.findViewById(R.id.txtLat);
        TextView textLong = (TextView)view.findViewById(R.id.txtLong);
        final TextView textShowLat = (TextView)view.findViewById(R.id.ShowLat);
        final TextView textShowLong = (TextView)view.findViewById(R.id.ShowLong);
        final MediaPlayer mpBtnCurrLoc = MediaPlayer.create(getContext(), R.raw.sample3);


        //δημιουργια αντικειμενου για να δειξω το Latitude και το Longitude

        gps = new GPSTracker(getContext());//getContext
        if(gps.canGetLocation){
            textShowLat.setText(String.valueOf(gps.getLatitude()));
            textShowLong.setText(String.valueOf(gps.getLongitude()));



        }
        else
        {
            Toast.makeText(getContext(),"Αδύνατος ο εντοπισμός της τρέχουσας θέσης",Toast.LENGTH_SHORT).show();
        }

       btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mpBtnCurrLoc.start();
               onLatLngSetListener.setLatLng(Double.valueOf(textShowLat.getText().toString()),Double.valueOf(textShowLong.getText().toString()));
           }
       });

        return view;
    }


    /* Υλοποίηση interface για την μεταφορά των δεδομένων μου από το
     * GPSFragment στο GeofenceFragment εντός της Geofencing
     * Activity
    */

    public interface onLatLngSetListener {

        public void setLatLng(Double Lat,Double Lng);



    } //end of interface

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onLatLngSetListener = (onLatLngSetListener) activity;
        } catch (Exception e){

        }
    }

    //Υλοποίηση της κλάσης GPSTracker για τον εντοπισμό Lat and Long

    public class GPSTracker extends Service implements LocationListener{

        private final Context context;
        double latitude,longitude;
        boolean isGPSEnabled = false;
        boolean canGetLocation =false;
        boolean isNetworkEnabled = false;

        Location location;

        private static final long MIN_DISTANCE_FOR_UPDATES = 10;
        private static final long MIN_TIME_BW_UPDATES = 1000*60*1;

        protected LocationManager locationManager;


        public GPSTracker(Context context){
            this.context = context;
            getLocation();

        }

        public Location getLocation(){
            try {
                locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled){


                }
                else
                {
                    this.canGetLocation = true;
                    if (isNetworkEnabled)
                    {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATES, this);
                    }

                    if (locationManager!=null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }

                if(isGPSEnabled){
                    if(location == null){
                        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_FOR_UPDATES,this);

                        if(locationManager!=null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location!=null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }

            } catch(Exception e) {
                e.printStackTrace();
            }

            return  location;

        }



        public void stopUsingGPS(){

            if(locationManager != null){

                locationManager.removeUpdates(GPSTracker.this);
            }

        }

       public double getLatitude(){
            if(location!=null){
                latitude = location.getLatitude();
            }
            return latitude;

        }

        public double getLongitude(){
            if(location!=null){
                longitude=location.getLongitude();
            }
            return longitude;

        }

        public boolean canGetLocation(){
            return this.canGetLocation;
        }


        @Override
        public void onLocationChanged(Location location) {
            if(location!=null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

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


        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }  //end of GPSTracker
}
