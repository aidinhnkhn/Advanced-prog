package Controllers;

import com.google.common.hash.Hashing;
import elements.people.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import site.edu.Main;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class ChangePasswordPage implements Initializable {
    private static Logger log = LogManager.getLogger(ChangePasswordPage.class);
    @FXML
    Button submitButton;
    @FXML
    AnchorPane anchorPane;
    @FXML
    PasswordField oldPassword,newPassword,confirmPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else{
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
        }
    }
    public void submit(ActionEvent actionEvent) {
        String sha256hex = Hashing.sha256()
                .hashString(oldPassword.getText(), StandardCharsets.UTF_8)
                .toString();
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("password Change");
        alert.setHeaderText("Status");
        if (!sha256hex.equals(Main.mainClient.getUser().getPassword()))
            alert.setContentText("old password is incorrect");
        if (!confirmPassword.getText().equals(newPassword.getText()))
            alert.setContentText("make sure that you entered new passwords correctly!");
        else{
            String newSha256hex = Hashing.sha256()
                    .hashString(newPassword.getText(), StandardCharsets.UTF_8)
                    .toString();
           Main.mainClient.getUser().setPassword(newSha256hex);
            Main.mainClient.getServerController().sendNewPassword(newSha256hex);
            log.info(Main.mainClient.getUser().getId()+" changed his password!");
            if (Main.mainClient.getUser() instanceof Student)
                SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
            else
                SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
            alert.setContentText("succesfull");
        }
        alert.show();
    }
}
