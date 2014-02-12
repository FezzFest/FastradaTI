package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;
import org.codeandmagic.android.gauge.GaugeView;

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
        GaugeView speed = (GaugeView) findViewById(R.id.dashboard_speed_gauge);
        speed.setTargetValue(20);

        // RPM indicator
        TextView rpmIndicator = (TextView) findViewById(R.id.dashboard_rpm_units);
        rpmIndicator.setText("4042 RPM");

        // PSI indicator
        TextView psiIndicator = (TextView) findViewById(R.id.dashboard_pressure_units);
        psiIndicator.setText("16 PSI");

        // Temperature indicator
        TextView tempIndicator = (TextView) findViewById(R.id.dashboard_temperature_units);
        tempIndicator.setText("103 °C");

        showRPM(2500);
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
                }else if(i<numberOfGreens+numberOfYellows){
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
}
