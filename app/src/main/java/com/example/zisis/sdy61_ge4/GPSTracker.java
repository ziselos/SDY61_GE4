package com.example.zisis.sdy61_ge4;


 /*Created by zisis on 223//17.
 //Υλοποίηση της κλάσης GPSTracker για τον εντοπισμό Lat and Long
 */

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class GPSTracker extends Service implements LocationListener {

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