package dataInterpreter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Carlo on 28/02/14.
 */
public class CompressorController {
    public static byte[] compress(byte[] packets) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            GZIPOutputStream stream = new GZIPOutputStream(out);
            stream.write(packets);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}