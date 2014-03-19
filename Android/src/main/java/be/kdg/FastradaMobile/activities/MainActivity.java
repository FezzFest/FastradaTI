package be.kdg.FastradaMobile.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.*;
import android.preference.PreferenceManager;

import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.kdg.FastradaMobile.Constants;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.controllers.UserInterfaceController;
import be.kdg.FastradaMobile.services.CommunicationService;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import org.codeandmagic.android.gauge.GaugeView;

public class MainActivity extends SherlockActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
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
    private UserInterfaceController interfaceController;
    private SoundPool sp;
    private int soundId;
    private int streamId;
    private int rpmLimiter;
    private boolean alarmPlaying;
    private boolean isGingerbread = true;
    private int id = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Check what version of Android we are running
        if (Build.VERSION.SDK_INT >= 11) {
            isGingerbread = false;
        }

        // Fullscreen w/ hidden ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!isGingerbread) {
            prefs.registerOnSharedPreferenceChangeListener(this);
        }
        interfaceController = UserInterfaceController.getInstance();

        // Set UI elements to their default position
        if (!isGingerbread) {
            setUIElementsPosition();
        }

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
            int maxSpeed = Integer.parseInt(prefs.getString("pref_max_speed", Constants.DEF_MAX_SPEED));
            speed.setNeedleEnabled(maxSpeed);
        }

        // RPM indicator
        rpmIndicator = (TextView) findViewById(R.id.dashboard_rpm_units);
        rpmDescription = (TextView) findViewById(R.id.dashboard_rpm_description);

        // Gear indicator
        gearIndicator = (TextView) findViewById(R.id.dashboard_gear_units);
        gearDescription = (TextView) findViewById(R.id.dashboard_gear_description);

        // Temperature indicator
        tempIndicator = (TextView) findViewById(R.id.dashboard_temperature_units);
        tempDescription = (TextView) findViewById(R.id.dashboard_temperature_description);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Update view
                showRPM(interfaceController.getRpm());
                showTemp(interfaceController.getTemperature());
                tempIndicator.setText(String.format("%.1f °C", interfaceController.getTemperature()));
                speed.setFixedTargetValue(interfaceController.getSpeed());
                gearIndicator.setText(interfaceController.getGear() + "");
                handler.postDelayed(this, 50);
            }
        });
        updateUiByPrefs();
    }

    private void updateUiByPrefs() {
        final LinearLayout imageView = (LinearLayout) findViewById(R.id.linearImageView);
        rpmLimiter = Integer.parseInt(prefs.getString("pref_max_RPM", Constants.DEF_MAX_RPM));

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
        if (temperature >= Integer.parseInt(prefs.getString("pref_alarm_temperature", Constants.DEF_TEMP_ALARM)) && prefs.getBoolean("pref_alarm_enabled", true)) {
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
        getSupportMenuInflater().inflate(R.menu.main_menu, menu);
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
                getSupportActionBar().show();
                hideActionBar(Constants.AB_TIMEOUT);

                if (!isGingerbread) {
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
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (longPressed && !isGingerbread) {
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
                if (!isGingerbread) {
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
        }

        return true;
    }

    private void setUIElementsPosition() {
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
                getSupportActionBar().hide();
            }
        }.start();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // If a shared preference is changed, check if MainActivity needs UI refresh.
        if (key.equals("pref_UI_RPM")) {
            recreate();
        } else if (key.equals("pref_UI_engineGear")) {
            recreate();
        } else if (key.equals("pref_UI_engineTemp")) {
            recreate();
        } else if (key.equals("pref_gauge_style")) {
            recreate();
        } else if (key.equals("pref_max_speed")) {
            recreate();
        } else if (key.equals("pref_max_RPM")) {
            recreate();
        } else if (key.equals("pref_UI_topLayout_X") || key.equals("pref_UI_topLayout_Y") || key.equals("pref_UI_bottomLayout_X") || key.equals("pref_UI_bottomLayout_Y") || key.equals("pref_UI_gaugeViewPort_X") || key.equals("pref_UI_gaugeViewPort_Y") || key.equals("pref_UI_leftLayout_X") || key.equals("pref_UI_leftLayout_Y") || key.equals("pref_UI_rightLayout_X") || key.equals("pref_UI_rightLayout_Y") || key.equals("pref_UI_gaugeViewLand_X") || key.equals("pref_UI_gaugeViewLand_Y")) {
            updateUiByPrefs();
            recreate();
        }
    }
}
