import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * To use Test Client:
 * 1. Run TestClient <ip address of server>
 * 2. The chat reads the input from the console and sends it to the server.
 * 3. The user can exit the chat by typing "exit" in the console.
 */

public class TestClient2 {
    public static final int DEFAULT_PORT = 1337;

    private static final Executor exec = Executors.newCachedThreadPool();


    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java TestClient <ip address of server>");
            System.exit(0);
        }

        Socket sock = null;         // the socket
        BufferedReader fromServer = null;
        OutputStreamWriter streamOut = null;
        ReaderThread readerThread = null;
        BufferedReader console = null;

        try {
            String pvtUser = "";
            sock = new Socket(args[0], DEFAULT_PORT);
            streamOut = new OutputStreamWriter(sock.getOutputStream());
            console = new BufferedReader(new InputStreamReader(System.in));
            /**
             * Read from the console.
             *
             */
            System.out.println("******************************************************************************************************************************************************");
            System.out.println("Welcome to Chat Room Application!");
            System.out.println("******************************************************************************************************************************************************");
            System.out.println("Instructions on using the Chat Room");
            System.out.println("1. Specify whether you want the message to be public (for everyone in the Chatroom) by typing 1 or private by typing 2.");
            System.out.println("2. Press enter");
            System.out.println("3. If the message is public then go ahead type in the message and press Enter");
            System.out.println("4. If the message is private then enter the username that needs to receive the message. Enter the message you want to send.");
            System.out.println("5. Start from 1 again.");
            System.out.println("6. To exit the chat. Enter exit on console at Step 1");
            System.out.println("*******************************************************************************************************************************************************");
            System.out.println("Enter your username: ");
            String username = console.readLine();
            streamOut.write("JOIN|" + username + "|ALL|" + Instant.now().toString() + "\r\n" + username + "\r\n");
            streamOut.flush();


            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String statusCode = fromServer.readLine();
            String[] delims = statusCode.split("\\|");

            if (delims[1].equals("200")) {
                System.out.println("***************************************************************");
                System.out.println("Successfully joined the chat with username  " + username + ".");
                System.out.println("******************************************************************");
                readerThread = new ReaderThread(sock);
                exec.execute(readerThread);

                boolean finished = false;
                while (!finished) {
                    String userInput = console.readLine();
                    if (userInput.equals("exit")) {
                        streamOut.write("LEAV|" + username + "|all|" + Instant.now().toString() + "\r\n");
                        streamOut.flush();
                        finished = true;
                        System.out.println("Successfully left the chat!");
                    } else {
                        boolean publicMessage = userInput.equalsIgnoreCase("1");
                        if (!publicMessage) {
                            pvtUser = console.readLine();
                        }
                        String output = console.readLine();
                        if (publicMessage) {
                            streamOut.write("BDMG|" + username + "|all|" + Instant.now().toString() + "\r\n");
                            streamOut.write(output + "\r\n");
                            streamOut.flush();
                        } else {
                            streamOut.write("PVMG|" + username + "|" + pvtUser + "|" + Instant.now().toString() + "\r\n");
                            streamOut.write(output + "\r\n");
                            streamOut.flush();
                        }
                    }
                }

            } else if (delims[1].equals("420")) {
                System.out.println("Not valid username: " + username);
            } else {
                System.out.println("Unexpected error occurred!");
            }


        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
