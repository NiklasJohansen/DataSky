package LabN3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ClientUDP {

    private static int serverPort = 55555; // Default port to use
    private static String serverAddress = "127.0.0.1";//"158.38.214.22";//"127.0.0.1"; // Default host, localhost

    private ClientUDP() {
        runClient();
    }

    private void runClient() {

        System.out.println(serverAddress +" " + serverPort);
        try (
                DatagramSocket socket = new DatagramSocket(serverPort);
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String inText;
            InetAddress address = InetAddress.getByName(serverAddress);
            byte[] buf = new byte[1024];
            DatagramPacket packet;

            System.out.print("Client [" + InetAddress.getLocalHost() + "] > ");

            while ((inText = stdIn.readLine()) != null && !inText.isEmpty()) {

                buf = inText.getBytes();
                packet = new DatagramPacket(buf, buf.length, address, serverPort);

                socket.send(packet);

                Arrays.fill(buf, (byte) 0);

                socket.receive(packet);

                String receivedText = new String(packet.getData());

                System.out.println("Server [" + serverAddress + "] > " + receivedText.trim());
                System.out.print("I (Client) [" + InetAddress.getLocalHost() + ":" + socket.getLocalPort() + "] > ");
            }
        } catch (UnknownHostException e) {
            System.err.println("Cannot find host: " + serverAddress);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    serverAddress);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException {

        if (args.length == 2) {
            serverAddress = args[0];
            serverPort = Integer.parseInt(args[1]);
        } else if (args.length > 2) {
            System.err.println("Usage: [<address>] [<port>]");
            System.exit(1);
        }
        new ClientUDP();
    }
}
