package be.kdg.FastradaMobile;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;
import be.kdg.FastradaMobile.activities.MainActivity;
import be.kdg.FastradaMobile.controllers.InputDataController;

/**
 * Created by FezzFest on 4/02/14.
 */
public class TestMainActivity extends ActivityUnitTestCase<MainActivity> {
    private InputDataController controller;
    private MainActivity activity;

    public TestMainActivity() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
        controller = new InputDataController(activity.getApplicationContext());
    }

    public void testRpmIndicator() {
        TextView rpmIndicator = (TextView) activity.findViewById(R.id.dashboard_rpm_units);
        assertEquals("RPM indicator must be set to 4042 RPM.", "4042 RPM", rpmIndicator.getText());
    }

    public void testGearIndicator() {
        TextView gearIndicator = (TextView) activity.findViewById(R.id.dashboard_gear_units);
        assertEquals("Gear indicator must be set to 1.", "1", gearIndicator.getText());
    }

    public void testTemperatureIndicator() {
        TextView tempIndicator = (TextView) activity.findViewById(R.id.dashboard_temperature_units);
        assertEquals("Temperature indicator must be set to 103 °C.", "103 °C", tempIndicator.getText());
    }
}
