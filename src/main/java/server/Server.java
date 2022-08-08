package server;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import server.Savers.Loader;
import server.Savers.Saver;
import shared.util.AuthenticationToken;
import server.network.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static Logger log = LogManager.getLogger(Server.class);
    private final ArrayList<ClientHandler> clientHandlers;
    private static Server server;
    private ServerSocket serverSocket;
    private boolean running;
    private final int port;

    public Server(int port) {
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
            serverSocket = new ServerSocket(port);
            running = true;
            getSiteOnline();
            //listenForNewConnection();
            while (running) {
                log.info("waiting for a connection...");
                Socket socket = serverSocket.accept();
                addNewClientHandler(socket);
                log.info("====> There are " + clientHandlers.size() + " clients on the server!");
            }
        } catch (IOException e) {
            log.fatal("Server is down");
            e.printStackTrace();
        }
    }

    private void addNewClientHandler(Socket socket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(socket, AuthenticationToken.generateNewToken());
        clientHandlers.add(clientHandler);
        new Thread(clientHandler).start();
    }

    public void sendMessageToClient(String authToken, String message) {
        for (ClientHandler clientHandler : clientHandlers)
            if (clientHandler.getAuthToken().equals(authToken)) {
                clientHandler.sendMessage(message);
                break;
            }
    }

    private void getSiteOnline(){
        Loader.getInstance().initializeEdu();
        Thread saverThread = new Thread(Saver.getInstance());
        saverThread.start();
    }
}
