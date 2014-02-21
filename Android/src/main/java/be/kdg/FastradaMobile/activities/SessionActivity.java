package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.services.CommunicationService;

/**
 * Created by FezzFest on 21/02/14.
 */
public class SessionActivity extends Activity {
    Button button;
    String name, vehicle;
    boolean gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);

        button = (Button)findViewById(R.id.session_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ((TextView)findViewById(R.id.session_name_input)).getText().toString();
                vehicle = ((TextView)findViewById(R.id.session_vehicle_name_input)).getText().toString();
                gps= ((CheckBox)findViewById(R.id.session_gps_input)).isChecked();
                Log.d("Fastrada", "THOMASISLEUK Naam=" + name+" Vehicle= "+vehicle+ "Checked = " + gps);
            }
        });
    }

    public void startService(){
        Intent intent = new Intent(this, CommunicationService.class);
        startService(intent);
    }

}
