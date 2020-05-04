
/**
 * Hassan and Brandon
 */

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ChatRoomServerMT {

    /* The port number for the server */
    public static final int DEFAULT_PORT = 1337;

    /* create a thread pool for concurrency */
    public static final Executor exec = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        ServerSocket sock = null;
        Vector<String> vector = new Vector<>();
        Map<String, OutputStream> clients = new HashMap<>();
        try {
            sock = new ServerSocket(DEFAULT_PORT);
            Runnable broadcast = new BroadCastThread();
            exec.execute(broadcast);
            System.out.println("Server started at port " + sock.getLocalPort());
            for (; ; ) {
                Socket client = sock.accept();
                if (client != null) {
                    System.out.println("****");
                    System.out.println("****");
                    System.out.println("Processing the client socket");
                    Runnable task = new Connection(client);
                    exec.execute(task);
                }
            }

        } catch (IOException ioe) {
            System.err.println(ioe);
        } finally {
            if (sock != null)
                sock.close();
        }
    }
}

