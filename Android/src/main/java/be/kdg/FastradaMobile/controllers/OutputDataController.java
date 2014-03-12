package be.kdg.FastradaMobile.controllers;

import android.util.Log;
import be.kdg.FastradaMobile.Constants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Peter Van Akelyen on 19/02/14.
 */
public class OutputDataController {
    public String doGet(String s) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public String doPost(String s, Object params) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            if (params instanceof byte[]) {
                conn.setRequestProperty("Content-Encoding", "gzip");
            }

            conn.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            if (params instanceof String) {
                dos.writeBytes((String) params);
            } else if (params instanceof byte[]) {
                dos.write((byte[]) params);
            }

            // Log
            Log.d(Constants.TAG, "URL: " + s);
            Log.d(Constants.TAG, "Params: " + params);
            Log.d(Constants.TAG, "Response code: " + conn.getResponseCode());

            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            Log.e(Constants.TAG, "POST request failed: " + e.getMessage());
        }

        return response.toString();
    }
}
