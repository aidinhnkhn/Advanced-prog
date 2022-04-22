package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class SignIn {
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passField;
    @FXML
    Button SignInButton;
    @FXML
    Button SignUPButton;


    public void nextField(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.TAB)
            passField.requestFocus();
    }

    public void singUpAction(ActionEvent actionEvent) throws IOException {
        SceneLoader.getInstance().changeScene("SingUp.fxml", actionEvent);
    }
}
