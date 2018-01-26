package LabN3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleClientServer
{
    private static int serverPort = 25565;
    private CurrencyConverter coverter;
    private QueryParser parser;

    public SingleClientServer()
    {
        this.coverter = new CurrencyConverter("filMedMasseGreier.csv");
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
                    // Henter data fra parser
                    // Henter "exchange-rate" fra converter med dataen fra parser
                    // Regner ut mengden i ny valuta
                    // Bygger returstreng
                    // Sender returstreng til client
                }
                else
                {
                    // Send feilmelding til client
                }
            }
        }
        catch (IOException e)
        {
            // Skriver ut feilmelding
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
