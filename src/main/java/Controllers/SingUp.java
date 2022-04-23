package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import logic.SignUpLogic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class SingUp implements Initializable {
    @FXML
    TextField usernameField, melicode, phoneNumber, email,supervisorId;
    @FXML
    PasswordField passField;
    @FXML
    PasswordField confirmPassField;
    @FXML
    ComboBox<String> choiceBox, department, degree;
    @FXML
    Button signUpButton, loadImage;
    @FXML
    ImageView imageView;
    @FXML
    Label fileSelected;

    private String imageFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll("Student", "Professor", "HeadDepartment", "EducationalAssistant");
        department.getItems().addAll("Chemical Eng", "Computer Eng", "Physics", "Mathematics", "Chemistry");
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
            SceneLoader.getInstance().changeScene("SignIn.fxml",actionEvent);
        }
        else
            alert.setContentText("unsuccessful! please fill each form correctly");
        alert.show();
    }
}
