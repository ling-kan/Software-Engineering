import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/* Run as a thread */
public class ToServer implements Runnable {
    /* Class */
    private static ToServer toServer;

    /* Reader */
    private PrintWriter out;

    /* Constructor */
    private ToServer(Socket clientSocket) {
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Started: "+this.getClass());
    }

    /* Singleton */
    public static ToServer getToServer(Socket clientSocket){
        if (toServer==null){
            toServer=new ToServer(clientSocket);
        }
        return toServer;
    }

    /* Singleton */
    public static ToServer getToServer(){
        return toServer;
    }

    /* When run as a new thread (i.e. Implements Runnable) */
    @Override
    public void run() {
        System.out.println("New thread started: " + this.toString());
        while(true){
            /* Hold this */
        }
    }

    /* Send message */
    public void sendMessage(String message) {
        out.println(message);
    }
}