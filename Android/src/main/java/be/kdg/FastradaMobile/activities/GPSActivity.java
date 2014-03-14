package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;



/**
 * Created by Thomas on 11/03/14.
 */
public class GPSActivity extends Activity implements LocationListener{

    // flag for GPS status
    boolean isGPSEnabled = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 500;

    // Declaring a Location Manager
    protected LocationManager locationManager;
    int i = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopUsingGPS();
            }
        });
        getGPSData();
    }

    public void getGPSData(){
         getLocation();
        ((TextView) findViewById(R.id.longitude)).setText("Longitude = " + getLongitude());
        ((TextView) findViewById(R.id.latitude)).setText("Latitude = " + getLatitude());
        ((TextView) findViewById(R.id.log)).setText("Location Update N°0");
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Fastrada", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }


    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSActivity.this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        ((TextView) findViewById(R.id.longitude)).setText("Longitude = " + location.getLongitude());
        ((TextView) findViewById(R.id.latitude)).setText("Latitude = " + location.getLatitude());
        i++;
        ((TextView) findViewById(R.id.log)).setText("Location Update N°" + i);
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
}


