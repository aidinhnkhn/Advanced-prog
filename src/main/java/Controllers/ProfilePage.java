package Controllers;

import elements.people.Professor;
import elements.people.Student;
import elements.people.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.response.Response;
import shared.util.ImageSender;
import site.edu.Main;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfilePage implements Initializable {
    @FXML
    public Button homePageButton;
    @FXML
    public Label average;

    @FXML
    public Label codemeli;

    @FXML
    public Label degree;

    @FXML
    public Label department;

    @FXML
    public TextField email;

    @FXML
    public Label enterYear;

    @FXML
    public Label id;

    @FXML
    public ImageView imageView;

    @FXML
    public Label isEducating;

    @FXML
    public TextField phoneNumber;

    @FXML
    public Button saveChangeButton;

    @FXML
    public Label supervisor;

    @FXML
    public Label username;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    CheckBox themeBox;
    private static Logger log = LogManager.getLogger(ProfilePage.class);
    public void HomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initUser();

        if (Main.mainClient.getUser() instanceof Student){
            initStudent();
        }
        else{
            supervisor.setVisible(false);
            enterYear.setVisible(false);
            isEducating.setVisible(false);
            average.setVisible(false);
        }
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }
    public void initUser(){
        User user=Main.mainClient.getUser();
        username.setText("username: "+user.getUsername());
        phoneNumber.setText(user.getPhoneNumber());
        id.setText("id: "+user.getId());
        codemeli.setText("meli code: "+user.getMelicode());
        email.setText(user.getEmail());
        department.setText("department: "+user.getDepartmentId());
        degree.setText("degree: "+user.getDegree());
        themeBox.setSelected(user.isTheme());
        initImage(user);
    }
    public void initImage(User user){
        if(Main.mainClient.getServerController().isServerOnline()) {
            Response response = Main.mainClient.getServerController().getUserImage();
            byte[] bytes = ImageSender.decode((String) response.getData("image"));
            Image image = new Image(new ByteArrayInputStream(bytes));
            Main.mainClient.setImage(image);
            imageView.setImage(image);
        }else{
            imageView.setImage(Main.mainClient.getImage());
        }
        log.info("user image loaded!");
    }

    public void initStudent(){
        Student student=(Student) Main.mainClient.getUser();
        Professor professor;
        if(Main.mainClient.getServerController().isServerOnline()) {
            professor = Main.mainClient.getServerController().getProfessor(student.getSupervisorId());
            Main.mainClient.setProfessor(professor);
        }
        else
            professor = Main.mainClient.getProfessor();
        supervisor.setText("supervisor: "+ professor.getUsername());
        enterYear.setText("enter year: "+student.getEnterYear());
        isEducating.setText((student.isEducating()?"undergraduate":"graduated"));
        average.setText("average: "+Double.toString(student.getَAverage()));
    }

    public void saveChanges(ActionEvent actionEvent) {
        if (!Main.mainClient.getServerController().isServerOnline()) return;
        User user=Main.mainClient.getUser();
        if (!user.getEmail().equals(email.getText()))
            log.info(user.getId()+" changed his email.");
        if (!user.getPhoneNumber().equals(phoneNumber.getText()))
            log.info(user.getId()+" changed his number!");
        if (user.isTheme()!=themeBox.isSelected())
            log.info(user.getId()+" changed the theme!");
        user.setEmail(email.getText());
        user.setPhoneNumber(phoneNumber.getText());
        user.setTheme(themeBox.isSelected());

        Main.mainClient.getServerController().sendNewUserInfo(user);
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }
}
