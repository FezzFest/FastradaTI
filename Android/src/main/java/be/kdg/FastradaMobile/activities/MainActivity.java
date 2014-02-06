package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;
import org.codeandmagic.android.gauge.GaugeView;

public class MainActivity extends Activity
{
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
    }
}
