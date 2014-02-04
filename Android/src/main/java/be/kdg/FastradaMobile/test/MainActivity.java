package be.kdg.FastradaMobile.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.controllers.InputDataController;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        InputDataController controller = new InputDataController();
        TextView speed = (TextView) findViewById(R.id.hello_world);
        speed.setText(Long.toString(controller.getSpeed()));
    }

}
