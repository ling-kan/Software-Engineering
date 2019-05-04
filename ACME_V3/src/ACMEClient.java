import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ACMEClient extends JFrame{
    /* Socket */
    private Socket clientSocket;
    /* Class */
    private static ACMEClient acmeClient;
    /* Singletons */
    private ToServer toServer;
    private FromServer fromServer;
    private User user;
    private StockList stockList;
    /* Stocks */
    private ArrayList<Stock> stockArrayList = new ArrayList<>();
    /* Swing */
    private JButton authButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel loginWindow;
    /* Format date */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd hh mm ss");
    /* Constructor */
    public ACMEClient(){
        /* Try and connect */
        while (clientSocket == null){
            try {
                clientSocket = new Socket("127.0.0.1", 5000);
            } catch (IOException e) {
                System.out.println("Cannot connect to server on:" + clientSocket.getLocalPort());
            }
        }
        /* Assign singletons */
        this.toServer=ToServer.getToServer(clientSocket);
        this.fromServer=FromServer.getFromServer(clientSocket);
        this.stockList=StockList.getStockList();
        this.user = User.getUser();

        /* Upon connection start reader and writer threads */
        Thread send = new Thread(this.toServer);
        Thread receive = new Thread(this.fromServer);
        send.start();
        receive.start();

        /* Create stock objects */
        populateStock();

        /* Window setup */
        this.setContentPane(loginWindow);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setTitle("ACMETrader: Authenticate");
        this.setVisible(true);

        /* Wait for login attempt */
        authButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* If username or password is empty don't attempt to auth */
                if (!usernameField.getText().equals("") || passwordField.getPassword().length!=0){
                    createUser();
                }
            }
        });

        /* Wait for authentication */
        while (!fromServer.getMessage().toString().contains("SUCCESS:"+usernameField.getText())){}

        /* Make this window disappear */
        this.dispose();

        /* Display user info */
        user.setVisible(true);

        /* Open stock viewer */
        stockList.setVisible(true);

        /* User buys */
        stockList.getBuyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPosition();
            }
        });

        /* User sells */
        user.getClosePositionButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closePosition();
            }
        });

        /* Update values every 2S*/
        ActionListener updateVal = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateValues();
                updateStocks();
                updatePositions();
            }
        };
        Timer updVal = new Timer(2000,updateVal);
        updVal.setInitialDelay(0);
        updVal.start();
    }

    /* Create a user */
    private void createUser(){
        toServer.sendMessage("AUTH:"+usernameField.getText()+":"+hashPass());
        user.setUserName(usernameField.getText());
        user.setTitle("ACMETrader: "+usernameField.getText());
    }

    /* Populate the stock */
    private void populateStock(){
        toServer.sendMessage("DISPLAYMARKET");
        /* Sleep for a moment */
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /* Split string builder by line */
        String[] outputThis = fromServer.getMessage().toString().split(System.getProperty("line.separator"));
        for (int i = 0; i<outputThis.length;i++){
            /* Split lines */
            String [] s = outputThis[i].split(":");
            /* Add it as a stock item */
            stockArrayList.add(new Stock(s[1],Double.parseDouble(s[2]),Integer.parseInt(s[3]),Double.parseDouble(s[4])));
            stockList.getStockEntity().addElement(s[1]);
        }
    }

    /* Update user balance/value */
    private void updateValues(){
        toServer.sendMessage("ACCBALANCE");
        /* Sleep for a moment */
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        /* Split string builder by line */
        String[] outputThis = fromServer.getMessage().toString().split(System.getProperty("line.separator"));
        /* Work backwards through the loop, only want latest value */
        for (int i = outputThis.length; i>0;i--){
            /* If it's the input we are looking for */
            if (outputThis[i-1].contains("ACC:")){
                /* Split it */
                String [] s = outputThis[i-1].split(":");
                /* Assign it */
                user.setUserBalance(Double.parseDouble(s[1]));
                user.setUserValue(Double.parseDouble(s[2]));
                /* And update the user form */
                user.setAccountBalanceLabel("Account balance: £"+s[1]);
                user.setAccountValueLabel("Account value: £"+s[2]);
                /* And break the loop */
                break;
            }
        }
    }

    /* Update stock price, quantity and val change */
    private void updateStocks(){
        toServer.sendMessage("DISPLAYMARKET");
        /* Sleep for a moment */
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        /* Split string builder by line */
        String[] outputThis = fromServer.getMessage().toString().split(System.getProperty("line.separator"));
        for (int i = 0; i < stockArrayList.size(); i++){
            /* Go through output */
            for (int y=outputThis.length; y>0; y--){
                if (outputThis[y-1].contains("STOCK:")) {
                /* See if output pertains to stock we're updating */
                    if (outputThis[y - 1].contains(stockArrayList.get(i).getStockName())) {
                    /* Split */
                        String[] s = outputThis[y - 1].split(":");
                        stockArrayList.get(i).setStockValue(Double.parseDouble(s[2]));
                        stockArrayList.get(i).setStockQuantity(Integer.parseInt(s[3]));
                        stockArrayList.get(i).setValueChange(Double.parseDouble(s[4]));
                    }
                }
            }
        }
    }

    /* Update positions */
    private void updatePositions(){
        toServer.sendMessage("ACCPOSITIONS");
        /* Sleep for a moment */
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        /* Split string builder by line */
        String[] outputThis = fromServer.getMessage().toString().split(System.getProperty("line.separator"));
        /* Go through output */
        for (int y=outputThis.length; y>0; y--){
            if (outputThis[y-1].contains("POSITION")){
                /* Split */
                String [] s = outputThis[y-1].split(":");
                boolean exists = false;
                /* check if date & name exist already */
                for (int i = 0; i < user.getPositionArrayList().size(); i++) {
                    if (user.getPositionArrayList().get(i).getPositionName().equals(s[1])&&user.getPositionArrayList().get(i).getPositionDate().equals(s[3])) {
                        exists = true;
                    }
                }
                /* Don't add the position if the date & stocktype is already present */
                if (!exists){
                    user.getPositionArrayList().add(new Position(s[1],Integer.parseInt(s[2]),Double.parseDouble(s[4]),s[3]));
                    user.getUserPositionEntity().addElement("STOCK: "+user.getPositionArrayList().get(user.getPositionArrayList().size()-1).getPositionName()
                            +"      QUANTITY: "+user.getPositionArrayList().get(user.getPositionArrayList().size()-1).getPositionQuantity()
                            +"      PRICE: "+user.getPositionArrayList().get(user.getPositionArrayList().size()-1).getPositionValue());
                    break;
                }
            }
        }
    }

    /* Hash password as MD5 */
    private String hashPass(){
        /* Encrypted */
        String thePass = "";
        /* Char array which has password */
        char userPass [] = passwordField.getPassword();
        /* Create string from array */
        for (int i = 0; i< userPass.length; i++)
        {
            thePass+=userPass[i];
        }
        /* Fill array with 0s as it shouldn't be kept as plaintext once its finished with */
        Arrays.fill(userPass,'0');

        /* Hash the password */
        MessageDigest hashPass = null;
        try {
            hashPass = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        hashPass.update(thePass.getBytes(),0,thePass.length());

        /* null the password string as it shouldn't be kept as plaintext once its finished with */
        thePass=null;
        return new BigInteger(1,hashPass.digest()).toString(16);
    }

    /* Open position */
    private void openPosition(){
        /* Confirm */
        ConfirmWindow c = new ConfirmWindow();
        c.getBuyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toServer.sendMessage("BUY:"+stockArrayList.get(stockList.getAvailableStock().getSelectedIndex()).getStockName()+":"+stockList.getStockQuantity()+":"+dateFormat.format(new Date()));
                c.dispose();
            }
        });
        c.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.dispose();
            }
        });
    }

    /* Close position */
    private void closePosition(){
        /* Confirm */
        ConfirmWindow c = new ConfirmWindow();
        c.getBuyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toServer.sendMessage("SELL:"+user.getPositionArrayList().get(user.getUserPositionList().getSelectedIndex()).getPositionName()
                        +":"+user.getPositionArrayList().get(user.getUserPositionList().getSelectedIndex()).getPositionDate());
                c.dispose();
                user.getUserPositionEntity().remove(user.getUserPositionList().getSelectedIndex());
            }
        });
        c.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.dispose();
            }
        });
    }

    /* Entry point */
    public static void main(String[] args){
        System.out.println("ACMEClient started at: "+new Date());
        acmeClient=new ACMEClient();
    }
}
