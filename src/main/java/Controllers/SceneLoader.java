package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {
    private static SceneLoader sceneLoader;
    private SceneLoader(){

    }
    public static SceneLoader getInstance(){
        if (sceneLoader==null){
            sceneLoader=new SceneLoader();
        }
        return sceneLoader;
    }
    public void changeScene(String address, ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource(address));
        Parent root= loader.load();
        Stage stage=(Stage)(((Node)(actionEvent.getSource())).getScene().getWindow());
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
