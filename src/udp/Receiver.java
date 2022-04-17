package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Receiver {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(9876);

            ArrayList<Mensagem> recPckt = new ArrayList<>();
            int sendBase = 0;

            byte[] recBuffer = new byte[1024];

            while (true) {
                // Recebendo o pacote
                DatagramPacket recPkt = new DatagramPacket(recBuffer, recBuffer.length);
                serverSocket.receive(recPkt);
                String information = new String(recPkt.getData(), recPkt.getOffset(), recPkt.getLength());
                Mensagem receivedMessage = Mensagem.stringToMensagem(information);
                if (receivedMessage.getNumSeq() == sendBase) {
                    // Pacote guardado no buffer
                    sendBase++;
                    recPckt.add(receivedMessage);

                    System.out.println("Mensagem id " + receivedMessage.getNumSeq() + " recebida na ordem, entregando para a camada de aplicação.");
                } else if (receivedMessage.getNumSeq() < sendBase) {
                    System.out.println("Mensagem id " + receivedMessage.getNumSeq() + " recebida de forma duplicada");
                } else {
                    System.out.println("Mensagem id " + receivedMessage.getNumSeq() + " recebida fora de ordem, ainda não recebidos os identificadores");
                    System.out.println("Lista de identificadores não recebidos: ");
                    for (int i = sendBase; i < receivedMessage.getNumSeq(); i++) {
                        if (i < receivedMessage.getNumSeq() - 1) {
                            System.out.print(i + ", ");
                        } else {
                            System.out.println(i);
                        }
                    }
                }

                String ackPcket = String.valueOf(sendBase);
                byte[] ackBytes = ackPcket.getBytes();

                DatagramPacket ackPacket = new DatagramPacket(ackBytes, ackBytes.length, recPkt.getAddress(), recPkt.getPort());
                // Manda o ACK
                serverSocket.send(ackPacket);
                System.out.println("Mandando o ACK");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
