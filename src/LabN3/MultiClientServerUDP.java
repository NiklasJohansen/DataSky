package LabN3;

import java.net.*;
import java.io.*;

public class MultiClientServerUDP
{
    private static int serverPort = 25565;
    private CurrencyConverter converter;
    private QueryParser parser;

    public MultiClientServerUDP()
    {
        this.converter = new CurrencyConverter("kursliste.csv");
        this.parser = new QueryParser();
        runServer();
    }

    private void runServer()
    {
        try (DatagramSocket serverSocket = new DatagramSocket(serverPort))
        {
            System.out.println("Currency Converter server is running!\n"
                    + "Waiting for incoming connections");

            String receivedText;
            while(true)
            {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);
                receivedText = (new String(packet.getData())).trim();

                InetAddress address = packet.getAddress();
                int clientPort = packet.getPort();

                System.out.println("Client [" + address.getHostAddress() +  ":" + clientPort +"] > " + receivedText);

                if(receivedText.equals("curr"))
                {
                    send(serverSocket, address, clientPort, converter.getAllCurrencies());
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
                        send(serverSocket, address, clientPort, respons.replace(',','.'));
                    }
                    else send(serverSocket, address, clientPort,  "One or both currencies are not supported!");
                }
                else send(serverSocket, address, clientPort, "Unrecognizable or invalid query!");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void send(DatagramSocket socket, InetAddress address, int port, String msg) throws IOException
    {
        byte[] buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
        System.out.println("Server [" + socket.getLocalAddress() + "] > " + msg);
    }

    public static void main(String[] args)
    {
        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        else if (args.length > 1)
            throw new IllegalArgumentException("Usage: SingleClientServer [<serverPort number>]");

        new MultiClientServerUDP();
    }
}

