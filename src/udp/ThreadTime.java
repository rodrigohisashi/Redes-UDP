package udp;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ThreadTime extends Thread {

    private DatagramPacket datagramPacket;

    public ThreadTime(DatagramPacket datagramPacket) {
        this.datagramPacket = datagramPacket;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(6000);
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.send(datagramPacket);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
