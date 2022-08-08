package site.edu.network;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.message.Message;
import shared.messages.message.MessageStatus;
import shared.messages.response.Response;
import site.edu.Client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerController {
    private static Logger log = LogManager.getLogger(ServerController.class);
    private PrintStream out;
    private Scanner in;

    private Client client;
    private final Socket socket;

    public ServerController(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void connectToServer() {
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new Scanner(socket.getInputStream());
            log.info("Client connected!");
            //this.sendMessage("Client connected.\nover");
            setAuthToken();
        } catch (IOException e) {
            log.error("serverController couldn't connect.");
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public String receiveMessage() {
        String messageFromServer = in.nextLine();
        while (true) {
            String nextLine = in.nextLine();
            if (nextLine.equals("over")) break;
            messageFromServer += nextLine;
        }
        return messageFromServer;
    }

    private void setAuthToken() {
        String message = receiveMessage();
        Response response = Response.fromJson(message);
        String authToken = (String) (response.getData("authToken"));
        this.client.setAuthToken(authToken);
        log.info(authToken);
    }

    public void sendTestMessage(){
        Message message = new Message(MessageStatus.Test,client.getAuthToken());
        message.addData("Test","if i can test it now");
        this.sendMessage(Message.toJson(message));
        log.info(receiveMessage());
    }
}
