import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 12/02/14.
 */
public class RunFromMemory {
    private static final String IP_ADDRESS = "192.168.43.1";
    private static DatagramSocket datagramSocket;

    public static void main(String[] args) throws InterruptedException, IOException {
        datagramSocket = new DatagramSocket();
        List<byte[]> id100Packets = makePacketsId100(0, 10000, 0, 10000, -20, 110);
        List<byte[]> id101Packets = makePacketsId101(0, 255, 0, 6);
        List<byte[]> id102Packets = makePacketsId102(0, 255);

        while (true) {
            for (int i = 0; i < 3000; i++) {
                sendPacket(id100Packets.get(i));
                Thread.sleep(2);
                sendPacket(id101Packets.get(i));
                Thread.sleep(2);
                sendPacket(id102Packets.get(i));
                Thread.sleep(2);
            }

            for (int i = 2999; i > 0; i--) {
                sendPacket(id100Packets.get(i));
                Thread.sleep(2);
                sendPacket(id101Packets.get(i));
                Thread.sleep(2);
                sendPacket(id102Packets.get(i));
                Thread.sleep(2);
            }
        }
    }

    private static List<byte[]> makePacketsId100(double rpmStart, double rpmEnd, double throttleStart, double throttleEnd, double tempStart, double tempEnd) {
        List<byte[]> packets = new ArrayList<byte[]>();
        double rpmStep = (rpmEnd - rpmStart) / 3000;
        double throttleStep = (throttleEnd - throttleStart) / 3000;
        double tempStep = (tempEnd - tempStart) / 3000;
        for (int i = 0; i < 3000; i++) {
            byte[] rpmBytes = intToByteArray((int) (rpmStart += rpmStep));
            int throttle = (int) (throttleStart / 0.0015259);
            byte[] throttleBytes = intToByteArray(throttle);
            throttleStart += throttleStep;
            int temp = (int) ((tempStart + 50) / 0.0030518);
            byte[] tempBytes = intToByteArray(temp);
            tempStart += tempStep;
            byte[] packet = {(byte) 0x01, (byte) 0x00, rpmBytes[2], rpmBytes[3], throttleBytes[2], throttleBytes[3], tempBytes[2], tempBytes[3], (byte) 0x00, (byte) 0x00};
            packets.add(packet);
        }
        return packets;
    }

    private static List<byte[]> makePacketsId101(double speedStart, double speedEnd, double gearStart, double gearEnd) {
        List<byte[]> packets = new ArrayList<byte[]>();
        double speedStep = (speedEnd - speedStart) / 3000;
        double gearStep = (gearEnd - gearStart) / 3000;
        for (int i = 0; i < 3000; i++) {
            int speed = (int) (speedStart / 0.00549324);
            byte[] speedBytes = intToByteArray(speed);
            speedStart += speedStep;
            byte[] gearBytes = intToByteArray((int) (gearStart += gearStep));
            byte[] packet = {(byte) 0x01, (byte) 0x01, speedBytes[2], speedBytes[3], gearBytes[3], (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            packets.add(packet);
        }
        return packets;
    }

    private static List<byte[]> makePacketsId102(double fuelStart, double fuelEnd) {
        List<byte[]> packets = new ArrayList<byte[]>();
        double fuelStep = (fuelEnd - fuelStart) / 3000;
        for (int i = 0; i < 3000; i++) {
            byte[] fuelBytes = intToByteArray((int) (fuelStart += fuelStep));
            byte[] packet = {(byte) 0x01, (byte) 0x02, fuelBytes[2], fuelBytes[3], (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            packets.add(packet);
        }
        return packets;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    private static void sendPacket(byte[] stream) throws IOException {
        InetAddress address = InetAddress.getByName(IP_ADDRESS); // IP-adres van de ontvanger hier zetten
        DatagramPacket packet = new DatagramPacket(stream, stream.length, address, 9000);
        datagramSocket.send(packet);
    }
}
