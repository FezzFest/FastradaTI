package be.kdg.FastradaMobile.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import be.kdg.FastradaMobile.json.SessionData;
import be.kdg.FastradaMobile.json.SessionId;
import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by Thomas on 21/02/14.
 */
public class SessionController extends AsyncTask<String[], Void, Integer> {
    private SharedPreferences prefs;

    public SessionController(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected Integer doInBackground(String[]... params) {
        String session = params[0][0];
        String track = params[0][1];
        String vehicle = params[0][2];
        String comment = params[0][3];
        return getSessionId(session, track, vehicle, comment);
    }

    private int getSessionId(String session, String track, String vehicle, String comment) {
        OutputDataController output = new OutputDataController();
        Gson gson = new Gson();

        // Current date
        Date date = new Date(System.currentTimeMillis());

        // New SessionData object
        SessionData sessionData = new SessionData();
        sessionData.setSessionName(session);
        sessionData.setTrackName(track);
        sessionData.setVehicleName(vehicle);
        sessionData.setComment(comment);
        sessionData.setDate(date);

        // Parse JSON request
        String jsonRequest = gson.toJson(sessionData);

        // Build URL
        StringBuilder url = new StringBuilder();
        String server = prefs.getString("pref_service_address", "http://vps42465.ovh.net");
        String port = prefs.getString("pref_service_port", "8080");
        String path = "/api/sessions/new";
        url.append(server);
        url.append(":");
        url.append(port);
        url.append(path);

        // Send to server
        String jsonResult = output.doPost(url.toString(), jsonRequest);

        // Parse JSON result
        SessionId sessionId = gson.fromJson(jsonResult, SessionId.class);
        return sessionId.getSessionId();
    }
}
