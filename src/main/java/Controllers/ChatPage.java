package Controllers;

import elements.people.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import site.edu.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatPage implements Initializable {
    
    @FXML
    public AnchorPane anchorPane;

    @FXML
    public TextField chatId;

    @FXML
    public TextArea chatIdsText;

    @FXML
    public ImageView chatImage;

    @FXML
    public Button chatShowButton;

    @FXML
    public TextArea chatText;

    @FXML
    public Button fileButton;

    @FXML
    public Button imageButton;

    @FXML
    public ImageView microphoneImage;

    @FXML
    public TextField pmId;

    @FXML
    public Button pmShowButton;

    @FXML
    public TextArea pmText;

    @FXML
    public Button recordButton;

    @FXML
    public Button sendButton;

    @FXML
    public Label usernameLabel;

    @FXML
    public Button homePage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    public void showChat(ActionEvent actionEvent) {
    }

    public void showPm(ActionEvent actionEvent) {
    }

    public void send(ActionEvent actionEvent) {
    }

    public void record(ActionEvent actionEvent) {
    }

    public void sendImage(ActionEvent actionEvent) {
    }

    public void sendFile(ActionEvent actionEvent) {
    }

    public void goHomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }
}
