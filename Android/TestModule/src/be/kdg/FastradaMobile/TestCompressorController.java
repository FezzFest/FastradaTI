package be.kdg.FastradaMobile;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import be.kdg.FastradaMobile.activities.MainActivity;
import be.kdg.FastradaMobile.controllers.CompressorController;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Created by FezzFest on 19/02/14.
 */
public class TestCompressorController extends ActivityUnitTestCase<MainActivity> {
    private MainActivity activity;
    private CompressorController compressor;

    public TestCompressorController() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
        compressor = new CompressorController();
    }

    public void testCompressPackets() throws IOException {
        byte[] packets = new byte[100];
        StringBuilder str = new StringBuilder();
        for (int i=0; i<100; i++) {
            str.append("AbCdEfGhIj");
        }
        packets = str.toString().getBytes();

        byte[] compressed = compressor.compress(packets);
        String decompressed = new String(decompress(compressed));

        assertEquals("Resulting message must be AbCdEfGhIj * 10", str.toString(), decompressed);
    }

    private byte[] decompress(byte[] gzip) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(gzip);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream stream = new GZIPInputStream(in);

        IOUtils.copy(stream, out);
        return out.toByteArray();
    }
}
