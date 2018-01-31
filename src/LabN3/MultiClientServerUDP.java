package LabN3;

import java.net.*;
import java.io.*;

public class MultiClientServerUDP {

    private static int serverPort = 25565;
    private CurrencyConverter converter;
    private QueryParser parser;

    public MultiClientServerUDP() {
        this.converter = new CurrencyConverter("kursliste.csv");
        this.parser = new QueryParser();
        runServer();
    }

    private void runServer() {
        try (DatagramSocket serverSocket = new DatagramSocket(serverPort)) {

            System.out.println("Currency Converter server is running!\n"
                    + "Waiting for incoming connections");

            String receivedText;
            do {
                byte[] buffer = new byte[1024];

                // create datagram packet
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // read datagram packet from the socket
                serverSocket.receive(packet);

                // extract text from the packet
                receivedText = new String(packet.getData());
                receivedText = receivedText.trim();

            } while (receivedText != null);
            {


            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private static class Client extends Thread {
        private Socket socket;
        private String address;

        private Client(Socket socket) {
            this.socket = socket;
            this.address = socket.getInetAddress().getHostAddress();
            System.out.println("New client [" + address + "] connected");
        }


        public static void main(String[] args) throws IOException {

            if (args.length == 1)
                serverPort = Integer.parseInt(args[0]);
            else if (args.length > 1)
                throw new IllegalArgumentException("Usage: SingleClientServer [<serverPort number>]");

            new MultiClientServerUDP();


        }
    }
}
