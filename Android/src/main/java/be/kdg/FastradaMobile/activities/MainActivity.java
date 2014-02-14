package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.controllers.BufferController;
import org.codeandmagic.android.gauge.GaugeView;

import java.io.IOException;

public class MainActivity extends Activity {
    private static int rpmLimiter;
    private SharedPreferences prefs;
    private MediaPlayer mp;
    private AudioManager amanager;
    private GaugeView speed;
    private TextView rpmIndicator;
    private TextView rpmDescription;
    private TextView gearIndicator;
    private TextView gearDescription;
    private TextView tempIndicator;
    private TextView tempDescription;
    private boolean alarmPlaying;
    private BufferController bufferController;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        alarmPlaying = false;
        bufferController = BufferController.getInstance();

        // Speed gauge
        speed = (GaugeView) findViewById(R.id.dashboard_speed_gauge);
        speed.setFixedTargetValue(195);

        // RPM indicator
        rpmIndicator = (TextView) findViewById(R.id.dashboard_rpm_units);
        rpmDescription = (TextView) findViewById(R.id.dashboard_rpm_description);
        rpmIndicator.setText("4042 RPM");

        // Gear indicator
        gearIndicator = (TextView) findViewById(R.id.dashboard_gear_units);
        gearDescription = (TextView) findViewById(R.id.dashboard_gear_description);
        gearIndicator.setText("1");

        // Temperature indicator
        tempIndicator = (TextView) findViewById(R.id.dashboard_temperature_units);
        tempDescription = (TextView) findViewById(R.id.dashboard_temperature_description);
        tempIndicator.setText("103 °C");

        // Audio Manager + Media Player
        /*amanager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        mp = MediaPlayer.create(this, R.raw.alarm_normal);*/


        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // mp.start();
                //update the view
                showRPM(bufferController.getRpm());
                showTemp(bufferController.getTemperature());
                tempIndicator.setText(String.format("%.1f °C", bufferController.getTemperature()));
                speed.setFixedTargetValue(bufferController.getSpeed());
                gearIndicator.setText(bufferController.getGear() + "");
                handler.postDelayed(this, 50);
            }
        });

        //Update UI elements by preferences
        final LinearLayout imageView = (LinearLayout) findViewById(R.id.linearImageView);
        rpmLimiter = Integer.parseInt(prefs.getString("pref_max_RPM", "8000"));

        if(prefs.getBoolean("pref_UI_gaugeView", true)){
            speed.setVisibility(View.VISIBLE);
        } else {
            speed.setVisibility(View.INVISIBLE);
        }

        if(prefs.getBoolean("pref_UI_RPM", true)){
            rpmIndicator.setVisibility(View.VISIBLE);
            rpmDescription.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            rpmIndicator.setVisibility(View.INVISIBLE);
            rpmDescription.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }

        if(prefs.getBoolean("pref_UI_engineGear", true)){
            gearIndicator.setVisibility(View.VISIBLE);
            gearDescription.setVisibility(View.VISIBLE);
        } else {
            gearIndicator.setVisibility(View.INVISIBLE);
            gearDescription.setVisibility(View.INVISIBLE);
        }

        if(prefs.getBoolean("pref_UI_engineTemp", true)){
            tempIndicator.setVisibility(View.VISIBLE);
            tempDescription.setVisibility(View.VISIBLE);
        } else {
            tempIndicator.setVisibility(View.INVISIBLE);
            tempDescription.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

  

    private void showTemp(double temperature) {
        if (temperature >= Integer.parseInt(prefs.getString("pref_alarm_temperature", "95"))&&prefs.getBoolean("pref_alarm_enabled",true)) {
            tempIndicator.setTextColor(Color.RED);
            tempDescription.setTextColor(Color.RED);
        } else {
            tempIndicator.setTextColor(Color.WHITE);
            tempDescription.setTextColor(Color.WHITE);
        }
    }

    public void showRPM(int rpm) {
        rpmIndicator.setText(rpm + " RPM");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearImageView);
        int numberOfGreens, numberOfYellows;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            numberOfGreens = 14;
            numberOfYellows = 3;
        } else {
            numberOfGreens = 8;
            numberOfYellows = 2;
        }
        int numberOfLeds = linearLayout.getChildCount();
        for (int i = 0; i < numberOfLeds; i++) {
            ImageView imageView = (ImageView) linearLayout.getChildAt(i);
            if (imageView != null) {
                if (i < numberOfGreens) {
                    //green
                    if (rpm > (rpmLimiter / numberOfLeds) * i) {
                        imageView.setImageResource(R.drawable.led_green);
                    } else {
                        imageView.setImageResource(R.drawable.led_gray);
                    }
                } else if (i < numberOfGreens + numberOfYellows) {
                    //orange
                    if (rpm > (rpmLimiter / numberOfLeds) * i) {
                        imageView.setImageResource(R.drawable.led_yellow);
                    } else {
                        imageView.setImageResource(R.drawable.led_gray);
                    }
                } else {
                    //red
                    if (rpm > (rpmLimiter / numberOfLeds) * i) {
                        imageView.setImageResource(R.drawable.led_red);
                    } else {
                        imageView.setImageResource(R.drawable.led_gray);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}
