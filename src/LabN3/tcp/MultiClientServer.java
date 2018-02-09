package LabN3.tcp;

import LabN3.CurrencyConverter;
import LabN3.QueryParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiClientServer
{
    private static int serverPort = 25565;
    private CurrencyConverter converter;
    private QueryParser parser;

    public MultiClientServer()
    {
        this.converter = new CurrencyConverter("currencies.csv");
        this.parser = new QueryParser();
        runServer();
    }

    private void runServer()
    {
        try (ServerSocket serverSocket = new ServerSocket(serverPort))
        {
            System.out.println("Currency Converter server is running!\n"
            + "Waiting for incoming connections");

            while(true)
                (new Client(serverSocket.accept())).start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("Something went wrong :/");
        }
    }

    public static void main(String[] args)
    {
        if(args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        else if (args.length > 1)
            throw new IllegalArgumentException("Usage: SingleClientServer [<serverPort number>]");

        new MultiClientServer();
    }

    private class Client extends Thread
    {
        private Socket socket;
        private String address;

        private Client(Socket socket)
        {
            this.socket = socket;
            this.address = socket.getInetAddress().getHostAddress();
            System.out.println("New client [" + address+ "] connected");
        }

        public void run()
        {
            try
            (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                for(String query; (query = in.readLine()) != null;)
                {
                    System.out.println("Client [" + address + "] > " + query);

                    if(query.equals("curr"))
                    {
                        send(out, converter.getAllCurrencies());
                    }
                    else if(parser.parse(query))
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
                            send(out, respons.replace(',','.'));
                        }
                        else send(out, "One or both currencies are not supported!");
                    }
                    else send(out, "Unrecognizable or invalid query!");
                }

                socket.close();
            }
            catch (IOException e)
            {
                System.out.println("Client [" + address + "] disconnected");
            }
        }

        private void send(PrintWriter pw, String msg)
        {
            pw.println(msg);
            System.out.println("Server [" + socket.getLocalAddress().getHostAddress() + "] > " + msg);
        }
    }
}
