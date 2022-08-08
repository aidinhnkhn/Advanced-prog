package site.edu;


import site.edu.network.ServerController;

import java.net.Socket;

public class Client implements Runnable{
    private ServerController serverController;
    private final Socket socket;

    private String authToken;
    public Client(Socket socket){
        this.socket = socket;
    }

    public void init(){
        serverController = new ServerController(socket,this);
        serverController.connectToServer();
    }

    public ServerController getServerController() {
        return serverController;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public void run() {
        init();
    }
}
