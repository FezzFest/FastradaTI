import dataInterpreter.CompressorController;
import org.junit.Test;

import org.apache.commons.io.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Carlo on 28/02/14.
 */
public class TestCompressorController {
    @Test
    public void testCompressPackets() throws IOException {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            str.append("AbCdEfGhIj");
        }
        byte[] packets = str.toString().getBytes();

        byte[] compressed = CompressorController.compress(packets);
        String decompressed = new String(decompress(compressed));

        assertEquals("Compressed and decompressed message must be equal", str.toString(), decompressed);
    }

    private byte[] decompress(byte[] gzip) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(gzip);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream stream = new GZIPInputStream(in);

        IOUtils.copy(stream, out);
        return out.toByteArray();
    }
}
