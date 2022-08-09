package site.edu.network;

import elements.people.Professor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.message.Message;
import shared.messages.message.MessageStatus;
import shared.messages.response.Response;
import shared.util.ImageSender;
import shared.util.JsonCaster;
import site.edu.Client;
import site.edu.Main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerController {
    private static Logger log = LogManager.getLogger(ServerController.class);
    private PrintStream out;
    private Scanner in;

    private boolean serverOnline;
    private Client client;
    private final Socket socket;

    public ServerController(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.serverOnline = true;
    }

    public boolean isServerOnline() {
        return serverOnline;
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
        try {
            String messageFromServer = in.nextLine();
            while (true) {
                String nextLine = in.nextLine();
                if (nextLine.equals("over")) break;
                messageFromServer += nextLine;
            }
            return messageFromServer;
        }catch (NoSuchElementException e){
            if (serverOnline) {
                log.info("Server is offline.");
            }
            serverOnline = false;
        }
        return "";
    }

    private void setAuthToken() {
        String message = receiveMessage();
        Response response = Response.fromJson(message);
        String authToken = (String) (response.getData("authToken"));
        this.client.setAuthToken(authToken);
        log.info(authToken);
    }

    public Response getCaptcha(){
        Message message = new Message(MessageStatus.Captcha,client.getAuthToken());
        this.sendMessage(Message.toJson(message));
        String responseString = receiveMessage();
        Response response = Response.fromJson(responseString);
        log.info("received captcha");
        return response;
    }

    public Response sendLoginRequest(String id,String password){
        Message message = new Message(MessageStatus.Login, client.getAuthToken());
        message.addData("id",id);
        message.addData("password",password);
        Main.mainClient.getServerController().sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        log.info("received login response");
        return response;
    }

    public void sendNewPassword(String password){
        Message message = new Message(MessageStatus.NewPassword,client.getAuthToken());
        message.addData("newPassword",password);
        sendMessage(Message.toJson(message));
    }


    public void sendLastEnter() {
        Message message = new Message(MessageStatus.LastEnter,client.getAuthToken());
        sendMessage(Message.toJson(message));
    }

    public Response getUserImage() {
        Message message = new Message(MessageStatus.UserImage,client.getAuthToken());
        message.addData("id",Main.mainClient.getUser().getId());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        log.info("received user Image");
        return response;
    }

    public Professor getProfessor(String supervisorId) {
        Message message = new Message(MessageStatus.getProfessor,client.getAuthToken());
        message.addData("id",supervisorId);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        log.info("professor received!");
        Professor professor = JsonCaster.professorCaster((String) response.getData("professor"));
        return  professor;
    }
}
