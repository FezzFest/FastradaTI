package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.services.ArduinoService;

import java.lang.reflect.Method;

/**
 * Created by Jonathan on 12/02/14.
 */
public class SplashActivity extends Activity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Initialize SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress
                showProgress();

                // Start tethering
                if (!prefs.getBoolean("pref_hotspot_disabled", false)) {
                    startWifiTethering();
                }

                // Start service
                startArduinoService();

                // Continue to main activity
                startMainActivity();
                finish();
            }
        });
    }

    private void showProgress() {
        LinearLayout progress = (LinearLayout) findViewById(R.id.start_progress_layout);
        progress.setVisibility(View.VISIBLE);
    }

    private void startWifiTethering() {
        // Get WiFi service
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        // Disable WiFi if running
        wifiManager.setWifiEnabled(false);

        // Enable tethering
        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("setWifiApEnabled")) {
                try {
                    method.invoke(wifiManager, null, true);
                } catch (Exception e) {
                    Log.e("Fastrada", "WiFi tethering not enabled: " + e.getMessage());
                }
                break;
            }
        }
    }

    private void startArduinoService() {
        // Update TextView
        TextView view = (TextView) findViewById(R.id.start_progress_text);
        view.setText(getString(R.string.splash_progress_service));

        // Start service
        Intent serviceIntent = new Intent(getApplicationContext(), ArduinoService.class);
        startService(serviceIntent);

        // Wait for service to be running
        boolean serviceRunning = false;
        while (!serviceRunning) {
            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if ("be.kdg.FastradaMobile.services.ArduinoService".equals(service.service.getClassName())) {
                    serviceRunning = true;
                }
            }
        }
    }

    private void startMainActivity() {
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                finish();
                break;
        }
        return true;
    }
}
