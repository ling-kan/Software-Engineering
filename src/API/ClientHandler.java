package API;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;

class ClientHandler extends Thread
{
    /* Classes */
    private BufferedReader in = null;
    private PrintWriter out = null;
    private StockMarket stockMarket;
    private UserReg userReg;
    private Socket clientSocket;

    /* Vars */
    private boolean isAuth;
    private Integer userIndex;
    private DecimalFormat formatValues = new DecimalFormat("#.00");

    /* Constructor */
    public ClientHandler(Socket clientSocket, StockMarket stockMarket, UserReg userReg)
    {
        this.clientSocket=clientSocket;
        this.stockMarket=stockMarket;
        this.userReg=userReg;
        start();
    }

    /* Login/create user - example inputText: "AUTH:Harry:a72c3353d8d9223b40365d27e7b3affc" */
    private void authUser(String inputText){
        if (!isAuth){
            /* Regex the input using ':' - for example s[0] is AUTH, s[1] is Harry and s[2] is a72c3353d8d9223b40365d27e7b3affc */
            String [] s = inputText.split(":");
            /* See if this user exists */
            if (!userReg.isAUser(s[1],s[2])){
            /* If user doesn't exist, add it */
                userReg.addAUser(s[1],s[2]);
            }
            /* Get user index (so the rest of this session does not need to search all users for an index match again) */
            userIndex=userReg.getIndex(s[1],s[2]);
            /* Mark isAuth as true */
            isAuth = true;
            out.println("SUCCESS:"+s[1]);
        } else {
            out.println("Session already started!");
        }
    }

    /* Display stock market - example inputText: "DISPLAYMARKET"*/
    private void displayMarket(){
        /* Only run if market isn't empty */
        if (stockMarket.getStockList().size()>=1){
            for (int i = 0; i<stockMarket.getStockList().size();i++){
                /* Example output: "STOCK:IBM:1405:18542:0.12" */
                out.println("STOCK:"
                        +stockMarket.getStockList().get(i).getStockName()+":"
                        +stockMarket.getStockList().get(i).getStockValue()+":"
                        +stockMarket.getStockList().get(i).getStockQuantity()+":"
                        +stockMarket.getStockList().get(i).getValueChange()
                );
            }
        }
    }

    /* Buy a stock - example inputText: "BUY:IBM:9:Sat Nov 04 12:33:27 GMT 2017" */
    private void buyStock(String inputText){
        if (isAuth){
            if (stockMarket.getStockList().size()>=1){
                /* Regex the input using ':' - for example s[0] is BUY, s[1] is IBM, s[2] is 9 and s[3] is Sat Nov 04 12:33:27 GMT 2017 */
                String [] s = inputText.split(":");
                /* Add the position - balance checking etc is done by addPosition method */
                userReg.getTheUsers().get(userIndex).addPosition(stockMarket.getStockList().get(stockMarket.getStockIndex(s[1])),Integer.parseInt(s[2]),s[3]);
            }
        } else {
            out.println("Requires authentication!");
        }
    }

    /* Sell a position - example inputText: "SELL:IBM:Sat Nov 04 12:33:27 GMT 2017" */
    private void sellStock(String inputText){
        if (isAuth){
            /* Regex the input using ':' - for example s[0] is SELL, s[1] is IBM and s[2] is Sat Nov 04 12:33:27 GMT 2017 */
            String [] s = inputText.split(":");
            /* Sell the position - value checking etc is done by sellPosition */
            userReg.getTheUsers().get(userIndex).sellPosition(stockMarket.getStockList().get(stockMarket.getStockIndex(s[1])),s[2]);
        } else {
            out.println("Requires authentication!");
        }

    }

    /* Get user's current balance - example inputText "ACCBALANCE" */
    private Double getBalance(){
        if (isAuth){
            return Double.valueOf(formatValues.format(userReg.getTheUsers().get(userIndex).getUserBalance()));
        }
        return null;
    }

    /* Get user's account value (balance + all position values) - example inputText "ACCBALANCE" */
    private Double getAccountValue(){
        if (isAuth){
            Double value = getBalance();
            if (userReg.getTheUsers().get(userIndex).getHeldPositions().size()>0){
                for (int i = 0; i<userReg.getTheUsers().get(userIndex).getHeldPositions().size();i++){
                    /* Add value of each position owned by user */
                    value+=
                            userReg.getTheUsers().get(userIndex).getHeldPositions().get(i).getPositionQuantity()
                            *stockMarket.getStockList().get(stockMarket.getStockIndex(userReg.getTheUsers().get(userIndex).getHeldPositions().get(i).getPositionName())).getStockValue();
                }
                return Double.valueOf(formatValues.format(value));
            }
        } else {
            out.println("Requires authentication!");
        }
        return userReg.getTheUsers().get(userIndex).getUserBalance();
    }

    /* Get all positions - example inputText "ACCPOSITIONS" */
    private void getPositions(){
        if (isAuth){
            if (userReg.getTheUsers().get(userIndex).getHeldPositions().size()>=1) {
                for (int i = 0; i<userReg.getTheUsers().get(userIndex).getHeldPositions().size();i++){
                    /* Example output: "POSITION:IBM:3:Sat Nov 04 12:33:27 GMT 2017:4215" */
                    out.println("POSITION:"
                            +userReg.getTheUsers().get(userIndex).getHeldPositions().get(i).getPositionName()+":"
                            +userReg.getTheUsers().get(userIndex).getHeldPositions().get(i).getPositionQuantity()+":"
                            +userReg.getTheUsers().get(userIndex).getHeldPositions().get(i).getPositionDate()+":"
                            +userReg.getTheUsers().get(userIndex).getHeldPositions().get(i).getPositionValue()
                    );
                }
            }
        } else {
            out.println("Requires authentication!");
        }
    }

    /* extends Thread */
    public void run()
    {
        System.out.println("New client has connected, new thread started.");
        System.out.println("Client IP is: " + clientSocket.getRemoteSocketAddress() + "\n\n");
            
        try
        {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputText;
                
            while((inputText = in.readLine()) != null)
            {
                System.out.println("Client: " + clientSocket.getLocalSocketAddress() + " : " + inputText);
                    
                if(inputText.contains("AUTH"))
                {
                    authUser(inputText);
                }
                else if(inputText.equals("DISPLAYMARKET")){
                    displayMarket();
                }
                else if(inputText.contains("BUY")){
                    buyStock(inputText);
                }
                else if(inputText.contains("SELL")){
                    sellStock(inputText);
                }
                else if(inputText.equals("ACCBALANCE")){
                    out.println("ACC:"+getBalance()+":"+getAccountValue());
                }
                else if(inputText.equals("ACCPOSITIONS")){
                    getPositions();
                }
                else
                {
                    System.out.println("DEBUG:"+inputText);
                }
            }
            out.close();
            in.close();

            clientSocket.close();
            isAuth = false;
        }
        catch(IOException e)
        {
            System.out.println("Problem with socket: " + e);
        }
    }

}