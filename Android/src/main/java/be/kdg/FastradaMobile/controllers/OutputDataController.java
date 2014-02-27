package be.kdg.FastradaMobile.controllers;

import android.util.Log;

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

    public String doPost(String s, String params) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", "vps42465.ovh.net:8080");
            conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");

            conn.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(params);

            // Log
            Log.d("Fastrada", "URL: " + s);
            Log.d("Fastrada", "Params: " + params);
            Log.d("Fastrada", "Response code: " + conn.getResponseCode());

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
}
