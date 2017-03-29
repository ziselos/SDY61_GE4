package com.example.zisis.sdy61_ge4;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by zisis on 153//17.
 * Υλοποίηση κλάσης GeofenceService για τον χειρισμό
 * του Geofence που θα χρησιμοποιήσω στην κλάση
 * GeofenceFragment
 *
 */


public class GeofenceService extends IntentService {

    public static final String TAG = "GeofenceService";

    public GeofenceService(){

        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()){

            Log.e( TAG, "Lathos" );
            return;

        }
        else
        {
            int transition = geofencingEvent.getGeofenceTransition();
            List<Geofence> geofenceList =geofencingEvent.getTriggeringGeofences();
            Geofence geofence = geofenceList.get(0);
            String requestedId = geofence.getRequestId();

            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER){
                Log.d(TAG,"Entering Geofence - "+requestedId);
                Toast.makeText(getApplicationContext(),"Entry to Geofence",Toast.LENGTH_SHORT).show();
            }
            else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT){
                Log.d(TAG,"Exiting Geofence - "+requestedId);
                Toast.makeText(getApplicationContext(),"Exit from Geofence",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
