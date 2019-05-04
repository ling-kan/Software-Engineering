import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/* Run as a thread */
public class FromServer implements Runnable {
    /* Class */
    private static FromServer fromServer;
    /* Reader */
    private BufferedReader in;
    /* Message */
    private StringBuilder message = new StringBuilder();

    /* Constructor */
    private FromServer(Socket clientSocket) {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
        System.out.println("Started: "+this.getClass());
    }

    /* Singleton */
    public static FromServer getFromServer(Socket clientSocket){
        if (fromServer==null){
            fromServer=new FromServer(clientSocket);
        }
        return fromServer;
    }

    /* Singleton */
    public static FromServer getFromServer(){
        return fromServer;
    }

    /* When run as a new thread (i.e. Implements Runnable) */
    @Override
    public void run() {
        System.out.println("New thread started: " + this.toString());
        while(true){
            try {
                message.append(in.readLine()+System.getProperty("line.separator"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Get message */
    public StringBuilder getMessage() {
        return message;
    }
}