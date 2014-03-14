package be.kdg.FastradaMobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.test.ActivityUnitTestCase;
import be.kdg.FastradaMobile.activities.MainActivity;
import be.kdg.FastradaMobile.activities.SplashActivity;
import be.kdg.FastradaMobile.controllers.InputDataController;
import be.kdg.FastradaMobile.services.LocationService;
import dataInterpreter.ByteCalculateController;
import dataInterpreter.ConfigController;
import org.junit.Assert;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by Jonathan on 13/03/14.
 */
public class TestLocationController extends ActivityUnitTestCase<SplashActivity> {
    private Activity activity;
    private LocationManager locationManager;
    private static final double UPDATED_LATITUDE = 51.349215899999990000;
    private static final double LATITUDE = 51.219215899999990000;
    private static final double LONGITUDE = 4.402881799999932000;
    private byte[] locationResult;

    public TestLocationController() {
        super(SplashActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), SplashActivity.class);
        startActivity(intent, null, null);

        // Initialize activity
        activity = getActivity();

        // Start service
        startLocationService();
    }

    public void startLocationService() throws InterruptedException {
        Intent locationService = new Intent(activity, LocationService.class);
        activity.startService(locationService);

        Thread.sleep(5000);
    }

    public void testGetFixedLocation() throws Exception {
        // Receive location update
        receiveLocationUpdate();
        Thread.sleep(1000);

        // First bind and unbind
        bindLocation(LATITUDE, LONGITUDE);
        Thread.sleep(1000);
        unbindLocation();

        // Bind with 2nd location
        bindLocation(UPDATED_LATITUDE, LONGITUDE);
        Thread.sleep(1000);

        // Parse location
        InputStream stream = activity.getResources().openRawResource(R.raw.config);
        ConfigController config = new ConfigController(stream);
        ByteCalculateController byteCalculator = new ByteCalculateController(config);
        Map<String, Double> map = byteCalculator.calculatePacket(locationResult);
        double latitude = map.get("latitude");
        double longitude = map.get("longitude");

        Assert.assertEquals("Latitude must be " + LATITUDE, LATITUDE, latitude, 0);
        Assert.assertEquals("Longitude must be " + LONGITUDE, LONGITUDE, longitude, 0);
        unbindLocation();
    }

    private void receiveLocationUpdate() {
        Thread thread = new Thread() {
            public void run() {
                InputDataController inputData = new InputDataController(activity);
                while (true) {
                    locationResult = inputData.receiveUdpPacket();
                }
            }
        };
        thread.start();
    }

    private void bindLocation(double latitude, double longitude) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false, false, false, false, 3, 1);
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAccuracy(1);
        location.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= 17) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
    }

    private void unbindLocation() {
        locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
    }
}
