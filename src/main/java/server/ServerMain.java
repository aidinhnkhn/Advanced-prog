package server;

import shared.util.Config;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class ServerMain {
    public static void main(String[] args) {
        Integer port = Config.getConfig().getProperty(Integer.class, "serverPort");
        Server server = new Server(port);
        server.init();
    }
}
