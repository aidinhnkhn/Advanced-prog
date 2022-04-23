package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import site.edu.Main;

import java.io.IOException;

public class SceneLoader {
    private static SceneLoader sceneLoader;

    //    private static Logger log=LogManager.getLogger(SceneLoader.class);
    private SceneLoader() {

    }

    public static SceneLoader getInstance() {
        if (sceneLoader == null) {
            sceneLoader = new SceneLoader();
        }
        return sceneLoader;
    }

    public void changeScene(String address, ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(address));
            Parent root = loader.load();
            Stage stage = (Stage) (((Node) (actionEvent.getSource())).getScene().getWindow());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
//            PropertyConfigurator.configure("C:/Users/aidin/Desktop/sharifedu/src/main/resources/site/edu/log4j2.xml");
//            log.info(address+" opened successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FIle NOT found");
        }
    }
    public void ChangeSceneByNode(String address,Node node){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(address));
            Parent root = loader.load();
            Stage stage = (Stage) (node.getScene().getWindow());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
//            PropertyConfigurator.configure("C:/Users/aidin/Desktop/sharifedu/src/main/resources/site/edu/log4j2.xml");
//            log.info(address+" opened successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FIle NOT found");
        }
    }
}
