package LabN3;

import java.net.*;
import java.io.*;

public class MultiClientServerUDP {

    private static int serverPort = 55555;
    private CurrencyConverter converter;
    private QueryParser parser;


    public MultiClientServerUDP() {
        this.converter = new CurrencyConverter("kursliste.csv");
        this.parser = new QueryParser();

        runServer();
    }

    private void runServer()
    {
        try (DatagramSocket serverSocket = new DatagramSocket(serverPort)) {

            System.out.println("Currency Converter server is running!\n"
                    + "Waiting for incoming connections");

            String receivedText;
            do {
                byte[] buffer = new byte[5000];

                // create datagram packet
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // read datagram packet from the socket
                serverSocket.receive(packet);

                // extract text from the packet
                receivedText = new String(packet.getData());
                receivedText = receivedText.trim();

                // convert to uppercase
                String outText = receivedText.toUpperCase();

                // put the processed output text as array of bytes into the buffer
                buffer = outText.getBytes();

                // get client's internet "address" and "port" from the hostname from the packet
                InetAddress address = packet.getAddress();
                int clientPort = packet.getPort();

                System.out.println("Client [" + address.getHostAddress() +  ":" + clientPort +"] > " + receivedText);

                // create datagram packet with the uppercase text to send back to the client
                packet = new DatagramPacket(buffer, buffer.length, address, clientPort);

                //sending back to client
                serverSocket.send(packet);

                System.out.println("Client [" + address.getHostAddress() +  ":" + clientPort +"] > " + receivedText);

                if(receivedText.equals("curr"))
                {
                    System.out.println(address + converter.getAllCurrencies());
                }
                else if(parser.parse(receivedText))
                {
                    String fromCurrency = parser.getFromCurrency();
                    String toCurrency = parser.getToCurrency();
                    float amount = parser.getFromAmount();

                    if(converter.isSupported(fromCurrency) && converter.isSupported(toCurrency))
                    {
                        float rate = converter.getRate(fromCurrency, toCurrency);
                        float value = rate * amount;
                        String respons = String.format("%.4f", amount) + " " + fromCurrency + " = "
                                + String.format("%.4f", value) + " " + toCurrency;
                        send(serverSocket, respons.replace(',','.'), packet);
                    }
                    else send(serverSocket, "One or both currencies are not supported!", packet);
                }
                else send(serverSocket, "Unrecognizable or invalid query!", packet);

            } while (receivedText != null);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(DatagramSocket socket, String msg, DatagramPacket p)
    {
        // SENDE TIL CLIENT MED SOCKET

        try {
            socket.send(p);
            System.out.println("Server [" + p.getAddress().getHostName() + "] > " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public static void main(String[] args) throws IOException {

        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        else if (args.length > 1)
            throw new IllegalArgumentException("Usage: SingleClientServer [<serverPort number>]");

        new MultiClientServerUDP();

        }
    }

