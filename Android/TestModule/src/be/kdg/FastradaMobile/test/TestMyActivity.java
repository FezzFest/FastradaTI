package be.kdg.FastradaMobile.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.controllers.InputDataController;


/**
 * Created by FezzFest on 4/02/14.
 */
public class TestMyActivity extends ActivityUnitTestCase<MainActivity> {
    private InputDataController controller;
    private MainActivity activity;

    public TestMyActivity() {
        super(MainActivity.class);
        controller = new InputDataController();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(),
                MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
    }

    public void testSetSpeedInDashboard() {
        int speed = Integer.parseInt(((TextView) activity.findViewById(R.id.hello_world)).getText().toString());
        assertEquals("Speed reported by controller must be the same as speed in UI.", controller.getSpeed(), speed);
    }
}
