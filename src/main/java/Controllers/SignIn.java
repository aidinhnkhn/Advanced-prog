package Controllers;

import server.Savers.Loader;
import elements.people.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import logic.LogicalAgent;
import logic.SignInLogic;
import shared.messages.response.Response;
import shared.util.ImageSender;
import site.edu.Main;


import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SignIn implements Initializable {

    @FXML
    TextField usernameField, captcha;
    @FXML
    PasswordField passField;
    @FXML
    Button SignInButton;
    @FXML
    Button SignUPButton, refreshButton;
    @FXML
    ImageView imageView;
    private final Map<Image, String> imageFileNames = new IdentityHashMap<>();

    public void nextField(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.TAB)
            passField.requestFocus();
    }

    public void singUpAction(ActionEvent actionEvent) {
        SceneLoader.getInstance().changeScene("SingUp.fxml", actionEvent);
    }

    public void refresh(ActionEvent actionEvent) {
        Response response= Main.mainClient.getServerController().getCaptcha();
        byte[] bytes = ImageSender.decode((String) response.getData("image"));
        Image image = new Image(new ByteArrayInputStream(bytes));
        String filename = (String)response.getData("number");
        if (!imageFileNames.containsKey(image))
            imageFileNames.put(image, filename);
        imageView.setImage(image);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh(new ActionEvent());
        Loader.getInstance().initializeEdu();
    }

    public void enter(ActionEvent actionEvent) {
        boolean successful = SignInLogic.getInstance().signIn
                (usernameField.getText(), passField.getText(), captcha.getText(), imageFileNames.get(imageView.getImage()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Sign in status");
        alert.setTitle("Sign in");
        if (successful) {
            LocalDateTime nowTime = LocalDateTime.now().minusHours(3);
            alert.setContentText("successful!");
            if (Main.mainClient.getUser().getLastEnter().isBefore(nowTime))
                SceneLoader.getInstance().changeScene("ChangePasswordPage.fxml", actionEvent);
            else
                goToHomePage(actionEvent);
        } else {
            alert.setContentText("unsuccessful! please fill each form correctly");
            refresh(new ActionEvent());
        }
        alert.show();
    }

    private void goToHomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }
}
