package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.controllers.BufferController;
import org.codeandmagic.android.gauge.GaugeView;

import java.nio.Buffer;

public class MainActivity extends Activity
{
    private final static int rpmLimiter= 8000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Speed gauge
        final GaugeView speed = (GaugeView) findViewById(R.id.dashboard_speed_gauge);
        speed.setTargetValue(20);

        // RPM indicator
        final TextView rpmIndicator = (TextView) findViewById(R.id.dashboard_rpm_units);
        rpmIndicator.setText("4042 RPM");

        // Gear indicator
        final TextView gearIndicator = (TextView) findViewById(R.id.dashboard_gear_units);
        gearIndicator.setText("1");

        // Temperature indicator
        final TextView tempIndicator = (TextView) findViewById(R.id.dashboard_temperature_units);
        tempIndicator.setText("103 °C");

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                //update the view
              BufferController bufferController =  BufferController.getInstance();
                showRPM(bufferController.getRpm());
                rpmIndicator.setText(bufferController.getRpm() + " RPM");
                tempIndicator.setText(String.format("%.2f °C",bufferController.getTemperature()));
               speed.setTargetValue(bufferController.getSpeed());
                gearIndicator.setText(bufferController.getGear()+"");
                handler.postDelayed(this, 50);
            }
        });
    }

    public void showRPM(int rpm){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearImageView);
        int numberOfGreens, numberOfYellows;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            numberOfGreens = 14;
            numberOfYellows = 3;
        } else {
            numberOfGreens = 8;
            numberOfYellows = 2;
        }
        int numberOfLeds = linearLayout.getChildCount();
        for(int i=0; i<numberOfLeds; i++) {
            ImageView imageView = (ImageView) linearLayout.getChildAt(i);
            if(imageView != null){
                if (i<numberOfGreens) {
                    //green
                    if(rpm>(rpmLimiter/ numberOfLeds)*i){
                        imageView.setImageResource(R.drawable.led_green);
                    }
                    else{
                        imageView.setImageResource(R.drawable.led_gray);
                    }
                } else if(i<numberOfGreens+numberOfYellows){
                    //orange
                    if(rpm>(rpmLimiter/ numberOfLeds)*i){
                        imageView.setImageResource(R.drawable.led_yellow);
                    }
                    else{
                        imageView.setImageResource(R.drawable.led_gray);
                    }
                } else {
                    //red
                    if(rpm>(rpmLimiter/ numberOfLeds)*i){
                        imageView.setImageResource(R.drawable.led_red);
                    }
                    else{
                        imageView.setImageResource(R.drawable.led_gray);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}
