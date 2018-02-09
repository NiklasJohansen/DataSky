package LabN3.tcp;

import LabN3.CurrencyConverter;
import LabN3.QueryParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleClientServer
{
    private static int serverPort = 25565;
    private CurrencyConverter converter;
    private QueryParser parser;

    public SingleClientServer()
    {
        this.converter = new CurrencyConverter("currencies.csv");
        this.parser = new QueryParser();
        runServer();
    }

    private void runServer()
    {
        try
        (
            ServerSocket serverSocket = new ServerSocket(serverPort);
            Socket socket = serverSocket.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Currency Converter server is running!");

            for(String query; (query = in.readLine()) != null;)
            {
                boolean isValid = parser.parse(query);

                if(isValid)
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
                        System.out.println(respons);
                        out.println(respons);
                    }
                    else out.println("One or both currencies are not supported!");
                }
                else out.println("Unrecognizable or invalid query!’");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("Noe gikk alvorlig galt!! :(");
        }
    }

    public static void main(String[] args)
    {
        if(args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        else if (args.length > 1)
            throw new IllegalArgumentException("Usage: SingleClientServer [<serverPort number>]");

        new SingleClientServer();
    }
}
