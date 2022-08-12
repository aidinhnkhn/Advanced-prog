package site.edu;


import elements.people.Professor;
import elements.people.User;
import javafx.scene.image.Image;
import site.edu.network.ServerController;

import java.net.Socket;

public class Client implements Runnable{
    private ServerController serverController;
    private final Socket socket;

    private User user;

    private Image image;
    private Professor professor;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public void run() {
        init();
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
