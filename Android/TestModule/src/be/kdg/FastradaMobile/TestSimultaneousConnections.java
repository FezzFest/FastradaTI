package be.kdg.FastradaMobile;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import be.kdg.FastradaMobile.activities.MainActivity;
import be.kdg.FastradaMobile.services.ArduinoService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by FezzFest on 7/02/14.
 */
public class TestSimultaneousConnections extends ActivityUnitTestCase<MainActivity> {
    private Activity activity;

    public TestSimultaneousConnections() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
    }

    public void testSimultaneousWiFiAnd3G() throws InterruptedException, IOException {
        // Enable WiFi tethering
        setWifiTetheringEnabled(true);

        // Start service
        Intent intent = new Intent(activity.getApplicationContext(), ArduinoService.class);
        activity.startService(intent);
        Thread.sleep(5000);

        // Get data from http
        loopGoogleRequest();

        // Thread sleep
        Thread.sleep(30000);

        // Disable tethering
        setWifiTetheringEnabled(false);
    }

    private void loopGoogleRequest() throws IOException {
        Thread thread = new Thread() {
            public void run() {
                try {
                    URL url = new URL("http://graph.facebook.com/641203392?fields=id,name");
                    for (int i = 0; i < 20; i++) {
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            Log.d("Fastrada", "JSON: " + line);
                        }

                        Thread.sleep(1000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void setWifiTetheringEnabled(boolean enable) {
        WifiManager wifiManager = (WifiManager) activity.getSystemService(activity.WIFI_SERVICE);

        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("setWifiApEnabled")) {
                try {
                    method.invoke(wifiManager, null, enable);
                } catch (Exception ex) {
                }
                break;
            }
        }
    }
}
