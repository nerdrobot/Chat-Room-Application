/**
 * Hassan and Brandon
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Set;


public class Handler {
    /**
     * this method is invoked by a separate thread
     */


    public void process(Socket client) throws IOException {
        BufferedReader fromClient = null;
        OutputStreamWriter toClient = null;

        try {
            fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            toClient = new OutputStreamWriter(client.getOutputStream());


            while (true) {
                String line;
                int i = 0;
                String firstLine = "";
                String secondLine = "";
                firstLine = fromClient.readLine();
                String[] values = firstLine.split("\\|");
                if (!values[0].equals(CommandList.LEAV) || values[0].equals(CommandList.JOIN))
                    secondLine = fromClient.readLine();

                Message messageContent;
                if (!secondLine.equals("")) {
                    messageContent = new Message(values[0], values[1], values[2], values[3], secondLine);
                } else {
                    messageContent = new Message(values[0], values[1], values[2], values[3], null);
                }

                if (values[0].equalsIgnoreCase(CommandList.JOIN)) {
                    System.out.println("Check Point!");
                    System.out.println(messageContent);
                    System.out.println("Check Point passed!");
                    if (isValidUsername(messageContent.getSource())) {
                        System.out.println("Valid username " + messageContent.getSource());
                        addClients(messageContent.getSource(), client);
                        processSuccessMessage(toClient);
                        ClientHelper.messages.add(messageContent.toString());
                    } else {
                        System.out.println("Not valid username: " + messageContent.getSource());
                        processFailureMessage(toClient, client);
                    }
                } else if (values[0].equalsIgnoreCase(CommandList.BROADCAST_MESSAGE)) {
                    String broadCastMessage = messageContent.toString() + secondLine + "\r\n";
                    ClientHelper.messages.add(broadCastMessage);
                    processSuccessMessage(toClient);
                    //broadcast message to all clients
                } else if (values[0].equalsIgnoreCase(CommandList.LEAV)) {
                    processSuccessMessage(toClient);
                    leaveChat(messageContent.getSource());
                    ClientHelper.messages.add(messageContent.toString());
                    //broadcast leave message to all clients
                } else if (values[0].equalsIgnoreCase(CommandList.PRIVATE_MESSSAGE)) {
                    if (!checkExistingUsernames(messageContent.getDestination())) {
                        processSuccessMessage(toClient);
                        privateMessage(messageContent);
                    } else {
                        processFailurePrivateMessage(toClient, client);
                    }
                }
            }

        } catch (IOException ioe) {
            throw ioe;
        }
    }

    private boolean isValidUsername(String username) {
        boolean success = true;

        if (!checkExistingUsernames(username)) {
            success = false;
        }

        boolean validChars = checkPattern(username);
        //allowed characters a-z, A-Z, 0-9, space, _ , -, $, ^, ~, ;
        if (!validChars) {
            success = false;
        }

        if (username.length() == 0) {
            success = false;
        }

        if (username.length() > 15) {
            success = false;
        }
        return success;
    }


    private void processSuccessMessage(OutputStreamWriter toClient) throws IOException {
        toClient.write(CommandList.STATUS_CODE + "|" + CommandList.SUCCESS_STATUS_CODE + "\r\n");
        toClient.flush();
    }

    private void processFailureMessage(OutputStreamWriter toClient, Socket socket) throws IOException {
        toClient.write(CommandList.STATUS_CODE + "|" + CommandList.BAD_USERNAME_STATUS_CODE + "\r\n");
        toClient.flush();
        toClient.close();
        socket.close();
    }

    private void processFailurePrivateMessage(OutputStreamWriter toClient, Socket socket) throws IOException {
        toClient.write(CommandList.STATUS_CODE + "|" + CommandList.INVALID_USERNAME_STATUS_CODE + "\r\n");
        toClient.flush();
        toClient.close();
        socket.close();
    }


    private void privateMessage(Message message) throws IOException {
        Socket socket = ClientHelper.clients.get(message.getDestination());
        OutputStreamWriter toClient = new OutputStreamWriter(socket.getOutputStream());
        toClient.write("");
        toClient.write(message.toString() + message.getContent() + "\r\n");
        toClient.flush();
    }

    private boolean checkExistingUsernames(String username) {
        boolean success = true;
        Set<String> keys = ClientHelper.clients.keySet();
        //check existing usernames
        for (String key : keys) {
            if (key.equals(username)) {
                success = false;
            }
        }
        return success;
    }

    private boolean checkPattern(String username) {
        return username.matches("^[a-zA-Z0-9_$^~; /-]*$");
    }


    public void addClients(String username, Socket socket) {
        ClientHelper.clients.put(username, socket);
    }

    private void leaveChat(String username) throws IOException {
        Socket socket = ClientHelper.clients.get(username);
        ClientHelper.clients.remove(username);
        socket.close();

    }


}
