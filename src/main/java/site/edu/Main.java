package site.edu;

import Savers.Saver;
import shared.util.Config;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.Socket;

public class Main extends Application {

    public static Client mainClient;
    @Override
    public void start(Stage stage) throws IOException {
        try {

            //init the client:
            Integer port = Config.getConfig().getProperty(Integer.class, "serverPort");
            Socket socket = new Socket("localhost", port);
            Client client = new Client(socket);
            Thread ClientThread = new Thread(client);
            ClientThread.start();
//            client.init();
            mainClient= client;
            //init the fxml:
            FXMLLoader loader=new FXMLLoader(Main.class.getResource("SignIn.fxml"));
            Parent root=loader.load();
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Saver.getInstance().saveChanges();
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