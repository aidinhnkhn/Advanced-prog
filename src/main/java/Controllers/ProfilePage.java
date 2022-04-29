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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initUser();

        if (LogicalAgent.getInstance().getUser() instanceof Student){
            Student student=(Student) LogicalAgent.getInstance().getUser();
            supervisor.setText("supervisor: "+Professor.getProfessor(student.getSupervisorId()).getUsername());
            enterYear.setText(new StringBuilder("enter year: "+student.getId().charAt(1)+student.getId().charAt(2)).toString());
            isEducating.setText((student.isEducating()?"undergraduate":"graduated"));
            average.setText("average: "+Double.toString(student.getَAverage()));
        }
        else{
            supervisor.setVisible(false);
            enterYear.setVisible(false);
            isEducating.setVisible(false);
            average.setVisible(false);
        }
        if (LogicalAgent.getInstance().getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }
    public void initUser(){
        User user=LogicalAgent.getInstance().getUser();
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
        try {
            String filename=user.getId()+".png";
            File file=new File(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename);
            if (!file.exists()){
                log.warn("image doesn't exist");
                return;
            }
            InputStream stream = new FileInputStream(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename);
            Image image = new Image(stream);
            imageView.setImage(image);
            stream.close();
        } catch (IOException e){
            log.error("couldn't load the image");
        }
    }

    public void saveChanges(ActionEvent actionEvent) {
        User user=LogicalAgent.getInstance().getUser();
        if (!user.getEmail().equals(email.getText()))
            log.info(user.getId()+" changed his email.");
        if (!user.getPhoneNumber().equals(phoneNumber.getText()))
            log.info(user.getId()+" changed his number!");
        if (user.isTheme()!=themeBox.isSelected())
            log.info(user.getId()+" changed the theme!");
        user.setEmail(email.getText());
        user.setPhoneNumber(phoneNumber.getText());
        user.setTheme(themeBox.isSelected());

        if (LogicalAgent.getInstance().getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }
}
