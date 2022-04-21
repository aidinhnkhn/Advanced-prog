package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
        if (keyEvent.getCode()== KeyCode.TAB)
            passField.requestFocus();
    }
}
