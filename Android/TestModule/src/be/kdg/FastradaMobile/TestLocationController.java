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
import org.junit.Assert;

/**
 * Created by Jonathan on 13/03/14.
 */
public class TestLocationController extends ActivityUnitTestCase<SplashActivity> {
    private Activity activity;
    private LocationController controller;
    private LocationManager locationManager;
    private static final double LATITUDE = 51.219215899999990000;
    private static final double LONGITUDE = 4.402881799999932000;

    public TestLocationController() {
        super(SplashActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
        controller = new LocationController(activity.getApplicationContext());
    }

    public void testGetFixedLocation() throws Exception {
        bindLocation();
        Thread.sleep(1000);
        Location location = controller.getCurrentLocation();
        Assert.assertEquals("Latitude must be " + LATITUDE, LATITUDE, location.getLatitude(), 0);
        Assert.assertEquals("Longitude must be " + LONGITUDE, LONGITUDE, location.getLongitude(), 0);
        unbindLocation();
    }


    private void bindLocation() {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false, false, false, false, 3, 1);
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(LATITUDE);
        location.setLongitude(LONGITUDE);
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
