package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.os.Bundle;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.controllers.InputDataController;
import org.codeandmagic.android.gauge.GaugeView;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        InputDataController controller = new InputDataController();
        GaugeView speed = (GaugeView) findViewById(R.id.dashboard_speed_gauge);
        speed.setTargetValue(controller.getSpeed());
    }

}
