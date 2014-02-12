package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import be.kdg.FastradaMobile.services.ArduinoService;

/**
 * Created by Jonathan on 12/02/14.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Start service
        Intent intent = new Intent(getApplicationContext(), ArduinoService.class);
        startService(intent);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent2);
    }
}
