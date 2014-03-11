package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.controllers.SessionController;
import be.kdg.FastradaMobile.services.CommunicationService;

/**
 * Created by Peter Van Akelyen on 21/02/14.
 */
public class SessionActivity extends Activity {
    private static final int NO_SESSION_ID = -1;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);

        Button startBtn = (Button) findViewById(R.id.session_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values
                String session = ((TextView) findViewById(R.id.session_name_input)).getText().toString();
                String track = ((TextView) findViewById(R.id.session_track_input)).getText().toString();
                String vehicle = ((TextView) findViewById(R.id.session_vehicle_input)).getText().toString();
                String comment = ((TextView) findViewById(R.id.session_comment_input)).getText().toString();
                String[] params = {session, track, vehicle, comment};

                // Check if service is already running
                if (session == null || session.trim().isEmpty()) {
                    Toast.makeText(SessionActivity.this, "Please fill in the session name.", Toast.LENGTH_SHORT).show();
                } else if (CommunicationService.isRunning) {
                    Toast.makeText(SessionActivity.this, "Session already running.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Show Progress Dialog
                    dialog = new ProgressDialog(SessionActivity.this);
                    dialog.setTitle("Contacting server...");
                    dialog.setMessage("Requesting session...");
                    dialog.setIndeterminate(true);
                    dialog.show();

                    // Send metadata
                    SessionController sessionController = new SessionController(SessionActivity.this);
                    sessionController.execute(params);
                }
            }
        });
    }

    public void gotSessionId(int sessionId) {
        if (sessionId != NO_SESSION_ID) {
            // Start service
            Intent intent = new Intent(this, CommunicationService.class);
            intent.putExtra("sessionId", sessionId);
            startService(intent);

            // Cancel dialog
            dialog.cancel();

            // Finish
            finish();
        } else {
            // Cancel dialog
            dialog.cancel();

            // Display message
            Toast.makeText(this, "Unable to request session ID.", Toast.LENGTH_LONG).show();

            // Finish
            finish();
        }
    }
}
