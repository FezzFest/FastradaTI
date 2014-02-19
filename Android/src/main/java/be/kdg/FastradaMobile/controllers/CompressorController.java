package be.kdg.FastradaMobile.controllers;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Created by FezzFest on 19/02/14.
 */
public class CompressorController {
    public byte[] compress(byte[] packets) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            GZIPOutputStream stream = new GZIPOutputStream(out);
            stream.write(packets);
            stream.close();

            Log.d("Fastrada", "Compressed: " + packets.length + " bytes");
            Log.d("Fastrada", "Ratio: " + 1.0f * packets.length / out.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
