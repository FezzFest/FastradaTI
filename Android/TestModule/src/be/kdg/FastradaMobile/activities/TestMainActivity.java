package be.kdg.FastradaMobile.activities;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
        controller = new InputDataController();
    }

    /*
    public void testDashboardSpeedIndicator() {
        GaugeView speedView = (GaugeView) activity.findViewById(R.id.dashboard_speed_gauge);

        TextView speedView = (TextView) activity.findViewById(R.id.dashboard_speed_indicator);
        int speed = Integer.parseInt(speedView.getText().toString());

        assertEquals("Speed reported by controller must be the same as speed displayed on dashboard.", controller.getSpeed(), speed);
    }*/
}
