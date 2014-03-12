package dataInterpreter;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
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

    public static byte[] decompress(byte[] gzip) {
        ByteArrayInputStream in = new ByteArrayInputStream(gzip);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            GZIPInputStream stream = new GZIPInputStream(in);
            IOUtils.copy(stream, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
