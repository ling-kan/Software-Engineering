package API;

import java.net.*;
import java.io.*;

public class StockMarketServer
{
    /* Classes */
    private ServerSocket serverSocket = null;
    private StockMarket stockMarket = StockMarket.getStockMarket();
    private static StockMarketServer stockMarketServer = new StockMarketServer();
    private UserReg userReg = UserReg.getUserReg();

    /* Constructor */
    public StockMarketServer()
    {
        /* Start StockMarket */
        Thread stockMarketThread = new Thread(stockMarket);
        stockMarketThread.start();
        this.listenForClients();
    }

    /* Await connection on serverSocket */
    public void listenForClients()
    {
        try
        {
            serverSocket = new ServerSocket(5000);
            
            while(true)
            {
                System.out.println("Listening for connections from Client.\n");
                new ClientHandler(serverSocket.accept(), stockMarket,userReg);
            }
        }
        catch(IOException e)
        {
            System.out.println("Error in setting up socket " + e);
            System.exit(1);
        }
    }

    /* Entry point */
    public static void main(String [] args)
    {
        /* Start */
        System.out.println("StockMarket started on: "+stockMarketServer.toString()+System.getProperty("line.separator"));
    }
}