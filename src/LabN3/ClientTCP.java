package LabN3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTCP
{
    private static String serverAddress = "127.0.0.1";
    private static int serverPort = 25565;

    private ClientTCP()
    {
        runClient();
    }

    private void runClient()
    {
        try
        (
            Socket socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.print("Client [" + InetAddress.getLocalHost()+ "] > ");

            for (String input; (input = stdIn.readLine()) != null && !input.isEmpty();)
            {
                out.println(input);
                System.out.println("Server [" + serverAddress + "] > " + in.readLine());
                System.out.print("Client [" + InetAddress.getLocalHost()+ "] > ");
            }
        }
        catch (UnknownHostException e)
        {
            System.err.println("Unknown server " + serverAddress);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Could not connect to " + serverAddress + ":" + serverPort);
            System.exit(1);
        }
    }

    public static void main(String[] args)
    {
        if(args.length == 2)
        {
            serverAddress = args[0];
            serverPort = Integer.parseInt(args[1]);
        }
        else if (args.length > 2)
        {
            System.err.println("Usage: [<address>] [<port>]");
            System.exit(1);
        }

        new ClientTCP();
    }
}
