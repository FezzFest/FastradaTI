package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import be.kdg.FastradaMobile.controllers.InputDataController;

/**
 * Created by FezzFest on 5/02/14.
 */
public class ArduinoService extends IntentService {
    public ArduinoService() {
        super("ArduinoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InputDataController controller = new InputDataController(getApplicationContext());
        byte[] received = controller.receiveUdpPacket();

        int value = received[0];
        Log.d("Fastrada", "Value: " + value);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().putInt("speed", value).commit();
    }
}
