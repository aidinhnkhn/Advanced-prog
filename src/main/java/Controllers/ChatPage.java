package Controllers;

import Controllers.voice.VoicePlayback;
import Controllers.voice.VoiceRecorder;
import elements.chat.Chat;
import elements.chat.pm.Pm;
import elements.chat.pm.PmType;
import elements.people.Professor;
import elements.people.Role;
import elements.people.Student;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.util.Config;
import shared.util.ImageSender;
import site.edu.Main;
import Controllers.voice.VoiceUtil;


import java.io.*;
import java.net.MalformedURLException;
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

    @FXML
    public ImageView userImage;
    @FXML
    public ImageView microphoneActiveImage;
    @FXML
    public Label fileSelected;
    private Chat chat;
    private boolean running;
    private static Logger log = LogManager.getLogger(ChatPage.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBooleans();

        new Thread( ()-> {

                while (running) {
                    setupTable();
                    if (chat!= null){
                        for (Chat setChat:Main.mainClient.getChats())
                            if (setChat.getId().equals(chat.getId())){
                                chat = setChat;
                                break;
                            }
                        setupChatText();
                    }
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

    private void setBooleans() {
        microphoneActiveImage.setVisible(false);
        running = true;
        chat = null;
        if (Main.mainClient.getUser().getRole() == Role.Admin)
            homePage.setVisible(false);
    }

    private void setupTable(){
        Platform.runLater( ()-> {
            ObservableList<Chat> chats = FXCollections.observableArrayList();
            chats.addAll(Main.mainClient.getChats());
            tableView.setItems(chats);
            lastPmColumn.setCellValueFactory(new PropertyValueFactory<Chat, String>("lastPm"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<Chat, String>("name"));
        });
    }
    public void showPm(ActionEvent actionEvent) {
        if (chat == null) return;
        String id = pmId.getText();
        for (Pm pm : chat.getMessages()){
            if (pm.getId().equals(id)) {
                if (pm.getType() == PmType.Image)
                    setupPmImage(pm);
                else if (pm.getType() == PmType.Audio)
                    playAudio(pm);
                else if (pm.getType() == PmType.Pdf)
                    savePdf(pm);
                break;
            }
        }
    }

    private void savePdf(Pm pm) {
        byte[] bytes = ImageSender.decode(pm.getFile());
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(usernameLabel.getScene().getWindow());

        if(selectedDirectory != null){
            try {
                FileUtils.writeByteArrayToFile(new File(selectedDirectory.getPath()+"\\download.pdf"), bytes);
            } catch (IOException e) {
                log.info("file saved");
            }
            fileSelected.setText("file saved");
            fileSelected.setStyle("-fx-text-fill: green");
            log.info("user sent an Image!");
        } else {
            fileSelected.setText("file status: failed!");
            fileSelected.setStyle("-fx-text-fill: red");
        }
    }

    private void playAudio(Pm pm) {
        byte[] bytes = ImageSender.decode(pm.getFile());
        VoicePlayback.playAudio(bytes);
    }

    private void setupPmImage(Pm pm) {
        byte[] bytes = ImageSender.decode(pm.getFile());
        Image image = new Image(new ByteArrayInputStream(bytes));
        chatImage.setImage(image);
    }

    public void send(ActionEvent actionEvent) {
        if (chat == null) return;
        if (chat.getStudentId1().equals("mohseni") || chat.getStudentId2().equals("mohseni"))
            return;
        Pm pm = new Pm(PmType.Text,Main.mainClient.getUser().getUsername());
        pm.setContent(pmText.getText());
        Main.mainClient.getServerController().sendPm(pm,chat.getId());
        pmText.clear();
    }

    public void record(ActionEvent actionEvent) {
        if (chat == null) return;
        if (chat.getStudentId1().equals("mohseni") || chat.getStudentId2().equals("mohseni"))
            return;
        if (VoiceUtil.isRecording()) {
            Platform.runLater(() -> {
                        microphoneImage.setVisible(true);
                        microphoneActiveImage.setVisible(false);
                    }
            );
            VoiceUtil.setRecording(false);
        } else {
            Platform.runLater(() -> {
                        microphoneImage.setVisible(false);
                        microphoneActiveImage.setVisible(true);
                    }
            );
            VoiceRecorder.captureAudio(chat.getId());
        }
    }

    public void sendImage(ActionEvent actionEvent)  {
        if (chat == null) return;
        if (chat.getStudentId1().equals("mohseni") || chat.getStudentId2().equals("mohseni"))
            return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif")); // limit fileChooser options to image files
        File selectedFile = fileChooser.showOpenDialog(usernameLabel.getScene().getWindow());

        if (selectedFile != null) {


            String encoded = ImageSender.encode(selectedFile.getPath());
            Pm pm = new Pm(PmType.Image,Main.mainClient.getUser().getUsername());
            pm.setContent(encoded);
            Main.mainClient.getServerController().sendPm(pm,chat.getId());

            fileSelected.setText("file status: Image selected");
            fileSelected.setStyle("-fx-text-fill: green");
            log.info("user sent an Image!");
        } else {
            fileSelected.setText("file status: failed!");
            fileSelected.setStyle("-fx-text-fill: red");
        }
    }

    public void sendFile(ActionEvent actionEvent) {
        if (chat == null) return;
        if (chat.getStudentId1().equals("mohseni") || chat.getStudentId2().equals("mohseni"))
            return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        File selectedFile = fileChooser.showOpenDialog(usernameLabel.getScene().getWindow());

        if (selectedFile != null) {
            if ((double) selectedFile.length() / (1024 * 1024) > 10.0){
                fileSelected.setText("file size limit exceeded!");
                fileSelected.setStyle("-fx-text-fill: red");
                return;
            }
            String encoded = ImageSender.encode(selectedFile.getPath());
            Pm pm = new Pm(PmType.Pdf,Main.mainClient.getUser().getUsername());
            pm.setContent(encoded);
            Main.mainClient.getServerController().sendPm(pm,chat.getId());

            fileSelected.setText("file status: Image selected");
            fileSelected.setStyle("-fx-text-fill: green");
            log.info("user sent an Image!");
        } else {
            fileSelected.setText("file status: failed!");
            fileSelected.setStyle("-fx-text-fill: red");
        }
    }

    public void goHomePage(ActionEvent actionEvent) {
        running = false;
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else if (Main.mainClient.getUser() instanceof Professor)
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("CreateChatPage.fxml",actionEvent);
    }

    public void showChat(MouseEvent event) {
        if (event.getClickCount() <= 1) return;
        chat = tableView.getSelectionModel().getSelectedItem();
        chatText.clear();
        if (chat == null) return;
        setLabelAndImage();
        setupChatText();
    }

    private void setupChatText(){
        Platform.runLater( ()-> {
            chatText.clear();
            for (Pm pm : chat.getMessages())
                chatText.appendText(pm.getMessage() + '\n');
        });
    }
    private void setLabelAndImage() {
        if (Main.mainClient.getUser().getId().equals(chat.getStudentId1()))
            loadILabelAndImage(chat.getImage2(),chat.getStudentName2());
        else
            loadILabelAndImage(chat.getImage1(),chat.getStudentName1());
    }
    private void loadILabelAndImage(String img,String username){
        byte[] bytes = ImageSender.decode(img);
        Image image = new Image(new ByteArrayInputStream(bytes));
        userImage.setImage(image);
        usernameLabel.setText("Chat: "+username);
    }
}
