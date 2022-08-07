package server.network;

import elements.people.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private static Logger log = LogManager.getLogger(ClientHandler.class);
    private final Socket socket;
    private final PrintWriter out;
    private final String authToken;

    private User user;

    private boolean exit;
    public ClientHandler(Socket socket, String authToken) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream());
        this.authToken = authToken;
        this.exit = false;
    }

    @Override
    public void run() {
        log.info("ClientHandler has been created!");
        try {
            Scanner in = new Scanner(socket.getInputStream());
            while (!exit) {
                System.out.println("ClientHandler is waiting for a message.");
                String messageFromClient = in.nextLine();
                while (true) {
                    String nextLine = in.nextLine();
                    if (nextLine.equals("over")) break;
                    messageFromClient += nextLine;
                }
                System.out.println("Message from Client: " + messageFromClient);
                //change the message into the class:
                //analayse the message:
                //ServerLogic.getInstance().analyse(message);
            }
        } catch (IOException e) {
            log.debug("Client handler is down");
            e.printStackTrace();
        }
    }

    public void stop(){
        Server.getServer().getClientHandlers().remove(this);
        this.exit = true;
    }

}
