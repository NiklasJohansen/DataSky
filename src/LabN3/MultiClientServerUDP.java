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
        try (
                // create an UDP/datagram socket for server on the given port
                DatagramSocket serverSocket = new DatagramSocket(serverPort)

        ) {
            String receivedText;
            do {
                byte[] buf = new byte[1024];


                // create datagram packet
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                // read datagram packet from the socket
                serverSocket.receive(packet);

                // extract text from the packet
                receivedText = new String(packet.getData());
                receivedText = receivedText.trim();

                // convert to uppercase
                String outText = receivedText.toUpperCase();

                // put the processed output text as array of bytes into the buffer
                buf = outText.getBytes();

                // get client's internet "address" and "port" from the hostname from the packet
                InetAddress clientAddr = packet.getAddress();
                int clientPort = packet.getPort();

                System.out.println("Client [" + clientAddr.getHostAddress() + ":" + clientPort + "] > " + receivedText);

                // create datagram packet with the uppercase text to send back to the client
                packet = new DatagramPacket(buf, buf.length, clientAddr, clientPort);

                // send the uppercase text back to the client
                serverSocket.send(packet);

                System.out.println("I (Server) [" + InetAddress.getLocalHost() + ":" + serverPort + "] > " + outText);
            } while (receivedText != null);

            System.out.println("I am done, Bye!");

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + serverPort + " or listening for a connection");
            System.out.println(e.getMessage());
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
