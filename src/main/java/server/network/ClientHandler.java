package server.network;

import elements.people.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import server.Server;
import shared.messages.message.Message;
import shared.messages.response.Response;
import shared.messages.response.ResponseStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private static Logger log = LogManager.getLogger(ClientHandler.class);
    private final Socket socket;
    private final PrintWriter out;

    private final Scanner in;
    private final String authToken;

    private User user;

    private boolean exit;
    public ClientHandler(Socket socket, String authToken) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream());
        this.in = new Scanner(socket.getInputStream());
        this.authToken = authToken;
        this.exit = false;
    }

    public String getAuthToken() {
        return authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        log.info("ClientHandler has been created!");
        try {
            sendAuthToken();
            while (!exit) {
                //System.out.println("ClientHandler is waiting for a message.");
                String messageFromClient = in.nextLine();
                while (true) {
                    String nextLine = in.nextLine();
                    if (nextLine.equals("over")) break;
                    messageFromClient += nextLine;
                }
                log.info("Message from Client: " + messageFromClient);
                //this.sendMessage("message received!\nover");
                //change the message into the class:
                Message message = Message.fromJson(messageFromClient);
                //analayse the message:
                Analyser.getInstance().analyse(message);
            }
        } catch (NoSuchElementException e) {
            log.debug("Client handler is down");
            stop();
        }
    }

    public void sendAuthToken(){
        Response response = new Response(ResponseStatus.AuThToken);
        response.addData("authToken",this.authToken);
        this.sendMessage(Response.toJson(response));
    }
    public void stop(){
        Server.getServer().getClientHandlers().remove(this);
        this.exit = true;
    }

    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }


}
