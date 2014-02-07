import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Thomas on 04/02/14.
 */
public class Run {

    private static BufferedReader  br;
    private static DatagramSocket datagramSocket;
    private static int i = 0;
    private static byte[] tempBytes;

    public static void main(String[] args) throws InterruptedException, IOException {
        initFile();
        tempBytes = new byte[10];
        datagramSocket = new DatagramSocket(9000);
        tempBytes = getLine();
        while(tempBytes != null){
            System.out.println("Sending Packet..");
            System.out.println();
            sendPacket(tempBytes);
            //receivePacket();
            Thread.sleep(500);
            tempBytes = getLine();
        }
    }

    public static void initFile() throws FileNotFoundException {
        br = new BufferedReader(new FileReader("newdata.txt"));
    }

    public static byte[] getLine() throws IOException {
        String line;
        if ((line = br.readLine()) != null){
            byte[] bytearray = new byte[10];
            for (int i =0;i<20;i+=2){
                byte byte1 = (byte) (Integer.parseInt(line.substring(i, i+2), 16) & 0xff);
                bytearray[i/2] = byte1;
            }
            return bytearray;
        } else {
            br.close();
            return null;
        }
    }

    public static String bytArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for(byte b: a)
            sb.append(String.format("%02x ", b&0xff));
        return sb.toString();
    }


    public static void sendPacket(byte[] stream) throws IOException {
        InetAddress address = InetAddress.getByName("192.168.43.1"); //hier ip adress van ontvanger zetten
        //InetAddress address = InetAddress.getByName("127.0.0.1");
        DatagramPacket packet = new DatagramPacket(
                stream, stream.length, address, 9000);
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.send(packet);
    }

    public static void receivePacket() throws IOException {
        Thread thread = new Thread() {
            public void run() {
                i++;
                System.out.println("Receiving packet..");
                byte[] buffer2 = new byte[10];
                try {
                    DatagramPacket packet = new DatagramPacket(buffer2, buffer2.length);
                    datagramSocket.receive(packet);
                    buffer2 = packet.getData();
                    if(buffer2 != null){
                        System.out.println("UDP Packet " + i + ": " + bytArrayToHex(buffer2));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    // Generate data is een functie om een custom ID en een integer value om te zetten naar bytes. Heb je niet echt nodig maar hebben het in het project laten staan.
    public static byte[] generateData(int id, int value){
        byte[] array1 = new byte[9];
        array1[0] = (byte)id;
        ByteBuffer b = ByteBuffer.allocate(8);
        b.order(ByteOrder.nativeOrder());
        b.putInt(value);
        for(int i = 0; i<8;i++){
            array1[i+1] = b.array()[7-i];
        }
        return array1;
    }
}
