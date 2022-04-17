package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
