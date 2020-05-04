/**
 * Hassan and Brandon
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReaderThread implements Runnable {
    Socket server;
    BufferedReader fromServer;

    public ReaderThread(Socket server) {
        this.server = server;
    }

    public void run() {
        try {
            fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

            while (true) {
                String message = fromServer.readLine();
                if (!message.equals("")) {
                    String[] delims = message.split("\\|");
                    if (delims[0].equals("JOIN"))
                        System.out.println(delims[1] + " has joined the chat.");
                    else if (delims[0].equals("BDMG")) {
                        String chatMessage = fromServer.readLine();
                        System.out.println(delims[1] + ": " + chatMessage);
                    } else if (delims[0].equals("PVMG")) {
                        String chatMessage = fromServer.readLine();
                        System.out.println("**************************");
                        System.out.println("     PRIVATE MESSAGE       ");
                        System.out.println("***************************");
                        System.out.println(delims[1] + ": " + chatMessage);
                        System.out.println("****************************");
                    } else if (delims[0].equals("LEAV")) {
                        System.out.println(delims[1] + "  left.");
                    } else if (delims[1].equals("421"))
                        System.out.println("The username used for private message does not exist.");
                    else if (delims[1].equals("200")) {
                    }
                    //     System.out.println("Server responded with success status code \"200\"");
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }
}
