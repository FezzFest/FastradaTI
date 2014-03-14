package be.kdg.FastradaMobile.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import be.kdg.FastradaMobile.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jonathan on 13/03/14.
 */
public class LocationService extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3;
    private static final long MIN_TIME_BW_UPDATES = 500;
    private static Logger log = Logger.getLogger(LocationService.class.getClass().getName());


    private Location location;
    private LocationManager locationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getCurrentLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    public Location getCurrentLocation() {
        if (location == null) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }

        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Latitude
        Log.d(Constants.TAG, "Updated latitude: " + location.getLatitude());
        int latitude = (int) (Math.floor(location.getLatitude() * 1000000));
        Log.d(Constants.TAG, "Updated integer latitude: " + latitude);

        // Longitude
        Log.d(Constants.TAG, "Updated longitude: " + location.getLongitude());
        int longitude = (int) (Math.floor(location.getLongitude() * 1000000));
        Log.d(Constants.TAG, "Updated integer longitude: " + longitude);

        byte[] latitudeBytes = ByteBuffer.allocate(4).putInt(latitude).array();
        byte[] longitudeBytes =  ByteBuffer.allocate(4).putInt(longitude).array();

        byte[] packet = {(byte) 0x00, (byte) 0x10, longitudeBytes[0], longitudeBytes[1], longitudeBytes[2], longitudeBytes[3], latitudeBytes[0], latitudeBytes[1], latitudeBytes[2], latitudeBytes[3]};
        sendUdpPacket(packet, 9000);
    }

    private void sendUdpPacket(final byte[] packet, final int port) {
        Thread thread = new Thread() {
            public void run() {
                byte[] arrayStream = packet;
                try {
                    DatagramSocket datagramSocket = new DatagramSocket();
                    InetAddress address = InetAddress.getByName("127.0.0.1");
                    DatagramPacket packet = new DatagramPacket(arrayStream, arrayStream.length, address, port);
                    datagramSocket.send(packet);

                    Log.d(Constants.TAG, "[UDP] Sent packet: " + arrayStream[0]);
                } catch (IOException e) {
                    log.log(Level.WARNING, e.getMessage(), e);
                }
            }
        };
        thread.start();
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
