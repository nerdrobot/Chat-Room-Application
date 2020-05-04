

/**
 * Hassan and Brandon
 */

import java.io.IOException;
import java.net.Socket;


public class Connection implements Runnable {
    private Socket client;
    private static final int BUFFER_SIZE = 1024;
    private Handler handler = new Handler();

    public Connection(Socket client) {
        this.client = client;
    }

    /**
     * This method runs in a separate thread.
     */
    public void run() {
        try {
            handler.process(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



