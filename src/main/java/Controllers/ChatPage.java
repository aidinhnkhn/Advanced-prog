package Controllers;

import elements.chat.Chat;
import elements.people.Student;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import site.edu.Main;


import java.net.URL;
import java.util.ResourceBundle;

public class ChatPage implements Initializable {
    
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ImageView chatImage;
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

    @FXML
    public TableColumn<Chat, String> lastPmColumn;

    @FXML
    public TableColumn<Chat, String> nameColumn;

    @FXML
    public TableView<Chat> tableView;

    private boolean running;
    private static Logger log = LogManager.getLogger(ChatPage.class);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        running = true;
        new Thread( ()-> {
                while (running) {
                    setupTable();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.debug("chat page thread stopped!");
                    }
                }
        }).start();

        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    private void setupTable(){
        ObservableList<Chat> chats= FXCollections.observableArrayList();
        chats.addAll(Main.mainClient.getChats());
        System.out.println(Main.mainClient.getChats());
        tableView.setItems(chats);
        lastPmColumn.setCellValueFactory(new PropertyValueFactory<Chat, String>("lastPm"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Chat, String>("name"));
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
        running = false;
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }
}
