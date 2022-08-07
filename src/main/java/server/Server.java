package server;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import server.network.AuthenticationToken;
import server.network.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static Logger log = LogManager.getLogger(Server.class);
    private final ArrayList<ClientHandler> clientHandlers;
    private static Server server;

    private final int port;
    public Server(int port){
        Server.server = this;
        this.port = port;
        clientHandlers = new ArrayList<>();
    }

    public static Server getServer() {
        return server;
    }

    public ArrayList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void init() {
        log.info("Server is running");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("waiting for a connection...");
                Socket socket = serverSocket.accept();
                addNewClientHandler(socket);
                System.out.println("====> There are " + clientHandlers.size() + " clients on the server!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewClientHandler(Socket socket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(socket, AuthenticationToken.generateNewToken());
        clientHandlers.add(clientHandler);
        new Thread(clientHandler).start();
    }
}
