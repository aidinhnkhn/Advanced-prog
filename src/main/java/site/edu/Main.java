package site.edu;

import Savers.Saver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader=new FXMLLoader(Main.class.getResource("SignIn.fxml"));
            Parent root=loader.load();
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Saver.getInstance().saveChanges();
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.show();
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