package site.edu;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import shared.util.Config;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main extends Application {

    public static Client mainClient;
    @Override
    public void start(Stage stage) throws IOException {
        try {

            //init the client:
            Integer port = Config.getConfig().getProperty(Integer.class, "serverPort");
            Socket socket = new Socket(InetAddress.getLocalHost(), port);
            Client client = new Client(socket);
            Thread ClientThread = new Thread(client);
            ClientThread.start();
            mainClient= client;
            //init the fxml:
            FXMLLoader loader=new FXMLLoader(Main.class.getResource("SignIn.fxml"));
            Parent root=loader.load();
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
//            Platform.runLater(() -> {
//                String musicFile = System.getProperty("user.dir")+"\\src\\main\\resources\\cache\\05. Shayea - Telo.mp3";     // For example
//
//                Media sound = new Media(new File(musicFile).toURI().toString());
//                MediaPlayer mediaPlayer = new MediaPlayer(sound);
//                mediaPlayer.play();
//            });
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    //Saver.getInstance().saveChanges();
                    Platform.exit();
                    System.exit(0);
                }
            });
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("FILE NOT FOUND!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}