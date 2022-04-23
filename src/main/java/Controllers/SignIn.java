package Controllers;

import Savers.Loader;
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


import java.io.*;
import java.net.URL;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

public class SignIn implements Initializable {
    @FXML
    TextField usernameField,captcha;
    @FXML
    PasswordField passField;
    @FXML
    Button SignInButton;
    @FXML
    Button SignUPButton,refreshButton;
    @FXML
    ImageView imageView;
    private final Map<Image, String> imageFileNames = new IdentityHashMap<>();
    public void nextField(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.TAB)
            passField.requestFocus();
    }

    public void singUpAction(ActionEvent actionEvent){
        SceneLoader.getInstance().changeScene("SingUp.fxml", actionEvent);
    }

    public void refresh(ActionEvent actionEvent){
        try {
            String filename=getCaptchaName();
            InputStream stream = new FileInputStream(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\Captcha\\" + filename);
            Image image = new Image(stream);
            if (!imageFileNames.containsKey(image))
                imageFileNames.put(image,filename);
            imageView.setImage(image);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    private String getCaptchaName(){
        Random random=new Random();
        int captchaNumber= random.nextInt(5);
        if(captchaNumber==0)
            return "5720.png";
        else if(captchaNumber==1)
            return "6280.png";
        else if(captchaNumber==2)
            return "8217.png";
        else if(captchaNumber==3)
            return "8387.png";
        else
            return "8612.png";
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh(new ActionEvent());
        Loader.getInstance().initializeEdu();
    }

    public void enter(ActionEvent actionEvent) {
        boolean successful=SignInLogic.getInstance().signIn
                (usernameField.getText(),passField.getText(),captcha.getText(),imageFileNames.get(imageView.getImage()));
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Sign in status");
        alert.setTitle("Sign in");
        if(successful) {
            alert.setContentText("successful!");
            goToHomePage(actionEvent);
        }
        else
            alert.setContentText("unsuccessful! please fill each form correctly");
        alert.show();
        refresh(new ActionEvent());
    }
    private void goToHomePage(ActionEvent actionEvent){
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }
}
