package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.controllers.UserInterfaceController;
import be.kdg.FastradaMobile.services.CommunicationService;
import org.codeandmagic.android.gauge.GaugeView;

public class MainActivity extends Activity {
    private static final int GREEN_LED_PORTRAIT = 8;
    private static final int GREEN_LED_LAND = 14;
    private static final int YELLOW_LED_PORTRAIT = 2;
    private static final int YELLOW_LED_LAND = 3;

    private SharedPreferences prefs;
    private GaugeView speed;
    private TextView rpmIndicator;
    private TextView rpmDescription;
    private TextView gearIndicator;
    private TextView gearDescription;
    private TextView tempIndicator;
    private TextView tempDescription;
    private UserInterfaceController bufferController;
    private SoundPool sp;
    private int soundId;
    private int streamId;
    private int rpmLimiter;
    private boolean alarmPlaying;
    int id = 0;
    String[] values = new String[12];

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Fullscreen w/ hidden ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setPositionUIElements();
        bufferController = UserInterfaceController.getInstance();

        // Keeps the screen on while activity is running
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Alarm sound
        sp = new SoundPool(5, AudioManager.STREAM_ALARM, 0);
        soundId = sp.load(this, R.raw.alarm_hell, 1);
        alarmPlaying = false;

        // Speed gauge
        speed = (GaugeView) findViewById(R.id.dashboard_speed_gauge);
        if (prefs.getString("pref_gauge_style", "text").equals("needle")) {
            // Get max speed
            int maxSpeed = Integer.parseInt(prefs.getString("pref_max_speed", "250"));
            speed.setNeedleEnabled(maxSpeed);
        }
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

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                //update the view
                showRPM(bufferController.getRpm());
                showTemp(bufferController.getTemperature());
                tempIndicator.setText(String.format("%.1f °C", bufferController.getTemperature()));
                speed.setFixedTargetValue(bufferController.getSpeed());
                gearIndicator.setText(bufferController.getGear() + "");
                handler.postDelayed(this, 50);
            }
        });


        // Update UI elements by preferences
        final LinearLayout imageView = (LinearLayout) findViewById(R.id.linearImageView);
        rpmLimiter = Integer.parseInt(prefs.getString("pref_max_RPM", "8000"));

        if (prefs.getBoolean("pref_UI_gaugeView", true)) {
            speed.setVisibility(View.VISIBLE);
        } else {
            speed.setVisibility(View.INVISIBLE);
        }

        if (prefs.getBoolean("pref_UI_RPM", true)) {
            rpmIndicator.setVisibility(View.VISIBLE);
            rpmDescription.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            rpmIndicator.setVisibility(View.INVISIBLE);
            rpmDescription.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }

        if (prefs.getBoolean("pref_UI_engineGear", true)) {
            gearIndicator.setVisibility(View.VISIBLE);
            gearDescription.setVisibility(View.VISIBLE);
        } else {
            gearIndicator.setVisibility(View.INVISIBLE);
            gearDescription.setVisibility(View.INVISIBLE);
        }

        if (prefs.getBoolean("pref_UI_engineTemp", true)) {
            tempIndicator.setVisibility(View.VISIBLE);
            tempDescription.setVisibility(View.VISIBLE);
        } else {
            tempIndicator.setVisibility(View.INVISIBLE);
            tempDescription.setVisibility(View.INVISIBLE);
        }
    }

    private void showTemp(double temperature) {
        if (temperature >= Integer.parseInt(prefs.getString("pref_alarm_temperature", "95")) && prefs.getBoolean("pref_alarm_enabled", true)) {
            tempIndicator.setTextColor(Color.RED);
            tempDescription.setTextColor(Color.RED);
            if (!alarmPlaying) {
                streamId = sp.play(soundId, 1, 1, 0, 0, 1);
                alarmPlaying = true;
            }
        } else {
            tempIndicator.setTextColor(Color.WHITE);
            tempDescription.setTextColor(Color.WHITE);
            if (alarmPlaying) {
                sp.pause(streamId);
                alarmPlaying = false;
            }
        }
    }

    public void showRPM(int rpm) {
        rpmIndicator.setText(rpm + " RPM");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearImageView);
        int numberOfGreens, numberOfYellows;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            numberOfGreens = GREEN_LED_LAND;
            numberOfYellows = YELLOW_LED_LAND;
        } else {
            numberOfGreens = GREEN_LED_PORTRAIT;
            numberOfYellows = YELLOW_LED_PORTRAIT;
        }
        int numberOfLeds = linearLayout.getChildCount();
        for (int i = 0; i < numberOfLeds; i++) {
            ImageView imageView = (ImageView) linearLayout.getChildAt(i);
            if (imageView != null) {
                if (i < numberOfGreens) {
                    // GREEN
                    if (rpm > (rpmLimiter / numberOfLeds) * i) {
                        imageView.setImageResource(R.drawable.led_green);
                    } else {
                        imageView.setImageResource(R.drawable.led_gray);
                    }
                } else if (i < numberOfGreens + numberOfYellows) {
                    // YELLOW
                    if (rpm > (rpmLimiter / numberOfLeds) * i) {
                        imageView.setImageResource(R.drawable.led_yellow);
                    } else {
                        imageView.setImageResource(R.drawable.led_gray);
                    }
                } else {
                    // RED
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (CommunicationService.isRunning) {
            menu.findItem(R.id.menu_start_session).setVisible(false);
            menu.findItem(R.id.menu_stop_session).setVisible(true);
        } else {
            menu.findItem(R.id.menu_start_session).setVisible(true);
            menu.findItem(R.id.menu_stop_session).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stop_session:
                stopService(new Intent(this, CommunicationService.class));
                break;
            case R.id.menu_start_session:
                startActivity(new Intent(this, SessionActivity.class));
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu_exit:
                System.exit(0);
                break;
        }

        return true;
    }

    boolean longPressed = false;
    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            longPressed = true;

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(50);
        }
    });

    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        int X = (int) event.getX();
        int Y = (int) event.getY();
        LinearLayout linearLayout1;
        LinearLayout linearLayout2;

        if (getResources().getConfiguration().orientation == 1) {
            linearLayout1 = (LinearLayout) findViewById(R.id.dashboard_top);
            linearLayout2 = (LinearLayout) findViewById(R.id.dashboard_bottom);
        } else {
            linearLayout1 = (LinearLayout) findViewById(R.id.dashboard_left);
            linearLayout2 = (LinearLayout) findViewById(R.id.dashboard_right);
        }

        GaugeView gaugeView = (GaugeView) findViewById(R.id.dashboard_speed_gauge);
        LinearLayout[] viewGroups = new LinearLayout[2];

        viewGroups[0] = linearLayout1;
        viewGroups[1] = linearLayout2;

        int eventAction = event.getAction();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                // Show ActionBar
                getActionBar().show();
                hideActionBar(5000);

                id = 0;
                for (LinearLayout UIelement : viewGroups) {
                    if (X > UIelement.getX() && X < UIelement.getX() + UIelement.getWidth() && Y > UIelement.getY() && Y < UIelement.getY() + UIelement.getHeight()) {
                        break;
                    }
                    id++;
                }
                if (id == 2) {
                    if (X > gaugeView.getX() && X < gaugeView.getX() + gaugeView.getWidth() && Y > gaugeView.getY() && Y < gaugeView.getY() + gaugeView.getHeight()) {
                        break;
                    }
                    id++;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (longPressed) {
                    if (id < 2) {
                        viewGroups[id].setX(X - viewGroups[id].getWidth() / 2);
                        viewGroups[id].setY(Y - viewGroups[id].getHeight() / 2);
                    } else if (id == 2) {
                        gaugeView.setX(X - gaugeView.getWidth() / 2);
                        gaugeView.setY(Y - gaugeView.getWidth() / 2);

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                longPressed = false;
                SharedPreferences.Editor editor = prefs.edit();
                if (getResources().getConfiguration().orientation == 1) {
                    editor.putString("pref_UI_topLayout_X", "" + viewGroups[0].getTranslationX());
                    editor.putString("pref_UI_topLayout_Y", "" + viewGroups[0].getTranslationY());
                    editor.putString("pref_UI_bottomLayout_X", "" + viewGroups[1].getTranslationX());
                    editor.putString("pref_UI_bottomLayout_Y", "" + viewGroups[1].getTranslationY());
                    editor.putString("pref_UI_gaugeViewPort_X", "" + gaugeView.getTranslationX());
                    editor.putString("pref_UI_gaugeViewPort_Y", "" + gaugeView.getTranslationY());
                } else {
                    editor.putString("pref_UI_leftLayout_X", "" + viewGroups[0].getTranslationX());
                    editor.putString("pref_UI_leftLayout_Y", "" + viewGroups[0].getTranslationY());
                    editor.putString("pref_UI_rightLayout_X", "" + viewGroups[1].getTranslationX());
                    editor.putString("pref_UI_rightLayout_Y", "" + viewGroups[1].getTranslationY());
                    editor.putString("pref_UI_gaugeViewLand_X", "" + gaugeView.getTranslationX());
                    editor.putString("pref_UI_gaugeViewLand_Y", "" + gaugeView.getTranslationY());
                }

                editor.commit();
        }

        return true;
    }

    private void setPositionUIElements() {
        GaugeView gaugeView = (GaugeView) findViewById(R.id.dashboard_speed_gauge);
        if (getResources().getConfiguration().orientation == 1) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dashboard_top);
            linearLayout.setX(Float.parseFloat(prefs.getString("pref_UI_topLayout_X", "0")));
            linearLayout.setY(Float.parseFloat(prefs.getString("pref_UI_topLayout_Y", "0")));
            LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.dashboard_bottom);
            linearLayout2.setX(Float.parseFloat(prefs.getString("pref_UI_bottomLayout_X", "0")));
            linearLayout2.setY(Float.parseFloat(prefs.getString("pref_UI_bottomLayout_Y", "0")));
            gaugeView.setX(Float.parseFloat(prefs.getString("pref_UI_gaugeViewPort_X", "0")));
            gaugeView.setY(Float.parseFloat(prefs.getString("pref_UI_gaugeViewPort_Y", "0")));
        } else {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dashboard_left);
            linearLayout.setX(Float.parseFloat(prefs.getString("pref_UI_leftLayout_X", "0")));
            linearLayout.setY(Float.parseFloat(prefs.getString("pref_UI_leftLayout_Y", "0")));
            LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.dashboard_right);
            linearLayout2.setX(Float.parseFloat(prefs.getString("pref_UI_rightLayout_X", "0")));
            linearLayout2.setY(Float.parseFloat(prefs.getString("pref_UI_rightLayout_Y", "0")));
            gaugeView.setX(Float.parseFloat(prefs.getString("pref_UI_gaugeViewLand_X", "0")));
            gaugeView.setY(Float.parseFloat(prefs.getString("pref_UI_gaugeViewLand_Y", "0")));
        }
    }

    private void hideActionBar(int timeout) {
        new CountDownTimer(timeout, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Do nothing
            }

            @Override
            public void onFinish() {
                getActionBar().hide();
            }
        }.start();
    }
}
