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
import be.kdg.FastradaMobile.controllers.SessionController;
import be.kdg.FastradaMobile.services.CommunicationService;

import java.util.concurrent.ExecutionException;

/**
 * Created by Peter Van Akelyen on 21/02/14.
 */
public class SessionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);

        Button startBtn = (Button) findViewById(R.id.session_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String session = ((TextView) findViewById(R.id.session_name_input)).getText().toString();
                String track = ((TextView) findViewById(R.id.session_track_input)).getText().toString();
                String vehicle = ((TextView) findViewById(R.id.session_vehicle_input)).getText().toString();
                String comment = ((TextView) findViewById(R.id.session_comment_input)).getText().toString();
                String[] params = {session, track, vehicle, comment};

                // Send metadata
                SessionController sessionController = new SessionController(SessionActivity.this);
                int sessionId = 0;
                try {
                    sessionId = sessionController.execute(params).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Log.d("Fastrada", "Session ID: " + sessionId);

                // Start service
                //startService();
            }
        });
    }

    private void startService() {
        Intent intent = new Intent(this, CommunicationService.class);
        startService(intent);
    }
}
