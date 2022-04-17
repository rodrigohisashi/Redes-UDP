package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.*;

public class Sender {

    public final static int WINDOW = 2;

    public final static int TIMER = 5000;

    public static void main(String[] args) throws Exception {

        int nextSeqNum = 0;

        int sendBase = 0;

        ArrayList<Mensagem> packetSent = new ArrayList<>();

        DatagramSocket clientSocket = new DatagramSocket();

        InetAddress IPAdress = InetAddress.getByName("127.0.0.1");

        while (true) {
            Scanner entrada = new Scanner(System.in);
            String dataUser = entrada.next();
            byte[] data = dataUser.getBytes();

            System.out.println("Escolha uma das opções de envio:");

            System.out.println("1 - Lento");
            System.out.println("2 - Perda");
            System.out.println("3 - Fora de Ordem");
            System.out.println("4 - Duplicada");
            System.out.println("5 - Normal");

            String escolha = entrada.next();
            if (!Objects.equals(escolha, "3")) {
                String[] esc = {"Lento", "Perda", "Fora de Ordem", "Duplicada", "Normal"};
                System.out.println("Mensagem " + escolha + " enviada como "+ esc[Integer.parseInt(escolha) - 1] +" com id " + nextSeqNum);
            }
            if (nextSeqNum - sendBase < WINDOW) {

                if (Objects.equals(escolha, "1")) {

                    Mensagem mensagem1 = new Mensagem(nextSeqNum, data);
                    byte[] sndPacket = mensagem1.mensagemToString().getBytes();
                    packetSent.add(mensagem1);
                    DatagramPacket sendPacket = new DatagramPacket(sndPacket, sndPacket.length, IPAdress, 9876);

                    ThreadTime thread = new ThreadTime(sendPacket);
                    thread.start();

                    nextSeqNum++;

                } else {
                    Mensagem mensagem1 = new Mensagem(nextSeqNum, data);

                    if (Objects.equals(escolha, "3")) {
                        Random rand = new Random();
                        int numSq = nextSeqNum + 1 + rand.nextInt(10);
                        mensagem1.setNumSeq(numSq);
                        System.out.println("Mensagem " + escolha + "enviada como Fora de Ordem com id " + numSq);
                    }

                    byte[] sndPacket = mensagem1.mensagemToString().getBytes();

                    // Adiciona o pacote mandado na lista
                    packetSent.add(mensagem1);

                    DatagramPacket sendPacket = new DatagramPacket(sndPacket, sndPacket.length, IPAdress, 9876);

                    if (!Objects.equals(escolha, "2")) {
                        // Manda o pacote
                        clientSocket.send(sendPacket);
                    }
                    if (Objects.equals(escolha, "4")) {
                        // Manda o pacote
                        clientSocket.send(sendPacket);
                    }

                    // Aumenta o numeroSequencia
                    nextSeqNum++;

                }
            }

            byte[] ackBytes = new byte[1024];

            DatagramPacket ack = new DatagramPacket(ackBytes, ackBytes.length);


            try {
                clientSocket.setSoTimeout(TIMER);
                clientSocket.receive(ack);

                int ackNumber = Integer.parseInt(new String(ack.getData(), ack.getOffset(), ack.getLength()));
                System.out.println("Mensagem id " + (ackNumber - 1) +  " recebida pelo receiver.");
                sendBase = Math.max(ackNumber, sendBase);

            } catch (SocketTimeoutException e) {
                // Mandando de novo os pacotes perdidos!
                System.out.println("IDs a serem reenviados: ");
                for (int i = sendBase; i < nextSeqNum; i++) {

                    byte[] data2 = packetSent.get(i).mensagemToString().getBytes();

                    DatagramPacket packet = new DatagramPacket(data2, data2.length, IPAdress, 9876);
                    clientSocket.send(packet);

                    System.out.println(packetSent.get(i).getNumSeq());

                    clientSocket.receive(ack);
                    int ackNumber = Integer.parseInt(new String(ack.getData(), ack.getOffset(), ack.getLength()));
                    System.out.println("Mensagem id " + (ackNumber - 1) +  " recebida pelo receiver.");
                    sendBase = Math.max(ackNumber, sendBase);
                }
            }

        }
    }

}
