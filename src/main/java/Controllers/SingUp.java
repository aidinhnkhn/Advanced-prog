package Controllers;

import elements.people.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import logic.LogicalAgent;
import logic.SignUpLogic;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import site.edu.Main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class SingUp implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    TextField usernameField, melicode, phoneNumber, email,supervisorId;
    @FXML
    PasswordField passField;
    @FXML
    PasswordField confirmPassField;
    @FXML
    ComboBox<String> choiceBox, department, degree;
    @FXML
    Button signUpButton, loadImage,homePage;
    @FXML
    ImageView imageView;
    @FXML
    Label fileSelected;

    private String imageFile;
    private static Logger log = LogManager.getLogger(SingUp.class);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll("Student", "Professor", "HeadDepartment", "EducationalAssistant");
        department.getItems().addAll("Chemical Eng", "Computer Eng", "Physics", "Mathematics", "Chemistry");
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    public void degreeSetItems(MouseEvent mouseEvent) {
        degree.getItems().clear();
        if (choiceBox.getValue() == null) return;
        if (choiceBox.getValue().equals("Student")) {
            degree.getItems().addAll("Bachelor", "Masters", "PHD");
        } else {
            degree.getItems().addAll("Assistant Professor", "Associate Professor", "full Professor");
        }
    }

    public void pickImage(ActionEvent actionEvent) throws MalformedURLException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif")); // limit fileChooser options to image files
        File selectedFile = fileChooser.showOpenDialog(fileSelected.getScene().getWindow());

        if (selectedFile != null) {

            imageFile = selectedFile.toURI().toURL().toString();

            Image image = new Image(imageFile);
            imageView.setImage(image);
            fileSelected.setText("Image selected");
            fileSelected.setStyle("-fx-text-fill: green");
            log.info("user picked an image!");
        } else {
            fileSelected.setText("Image file selection failed!");
            fileSelected.setStyle("-fx-text-fill: red");
        }
    }

    public void register(ActionEvent actionEvent) {
        boolean successful=SignUpLogic.getInstance().SignUp(usernameField.getText(), passField.getText(), confirmPassField.getText(),melicode.getText(),
                phoneNumber.getText(),email.getText(),choiceBox.getValue(),department.getValue(),degree.getValue(),imageView.getImage(),supervisorId.getText());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Sign up status");
        alert.setTitle("Sign up");
        if(successful) {
            alert.setContentText("successful!");
        }
        else
            alert.setContentText("unsuccessful! please fill each form correctly");
        alert.show();
    }

    public void goBack(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }
}
