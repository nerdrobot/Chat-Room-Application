/**
 * Hassan and Brandon
 */

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class BroadCastThread implements Runnable {


    @Override
    public void run() {
        while (true) {
            // sleep for 1/10th of a second
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }

            /**
             * check if there are any messages in the Vector. If so, remove them
             * and broadcast the messages to the chatroom
             */
            Vector<String> vector = ClientHelper.messages;
            HashMap<String, Socket> clients = ClientHelper.clients;
            while (!vector.isEmpty()) {
                String broadcastMessage = vector.remove(0);
                List<Socket> connectedClients = new ArrayList<>(clients.values());
                for (Socket socket1 : connectedClients) {
                    try {
                        OutputStreamWriter toClient = new OutputStreamWriter(socket1.getOutputStream());
                        toClient.write(broadcastMessage);
                        toClient.flush();
                    } catch (IOException e) {
                    }

                }
            }
        }

    }
}
