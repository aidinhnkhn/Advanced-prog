package Controllers;

import Savers.Saver;
import elements.people.Professor;
import elements.people.Student;
import elements.people.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import logic.LogicalAgent;
import logic.ProfessorHomePageLogic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProfessorHomePage implements Initializable {
    @FXML
    public MenuItem courseList;
    @FXML
    ImageView imageView;
    @FXML
    Label dateTime, lastEnter, email, name;
    @FXML
    MenuItem educationalStatus,signUp,editCourse,editProfessor;
    @FXML
    MenuItem professorList;

    public void initialize(URL location, ResourceBundle resources) {
        initClock();
        initUser(LogicalAgent.getInstance().getUser());
        setVisibility((Professor) (LogicalAgent.getInstance().getUser()));
        Saver.getInstance().saveProfessor((Professor) (LogicalAgent.getInstance().getUser()));
    }
    public void setVisibility(Professor professor){
        if (professor.isEducationalAssistant()) {
            educationalStatus.setVisible(true);
            signUp.setVisible(true);
            editCourse.setVisible(true);
        }
        if (professor.isHeadDepartment())
            editProfessor.setVisible(true);
    }
    private void initClock() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e ->
                dateTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        ),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void initUser(User user) {
        lastEnter.setText("last enter: " + user.getDateString());
        name.setText("name: " + user.getUsername());
        email.setText("email: " + user.getEmail());
        initImage(user);
    }

    public void initImage(User user) {
        try {
            String filename = user.getId() + ".png";
            InputStream stream = new FileInputStream(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename);
            Image image = new Image(stream);
            imageView.setImage(image);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent actionEvent) {
        ProfessorHomePageLogic.getInstance().exit(dateTime);
    }

    public void signUp(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("SingUp.fxml",dateTime);
    }

    public void openCourse(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("editCourse.fxml",dateTime);
    }

    public void openCourseList(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("CourseList.fxml",dateTime);
    }

    public void openProfessorList(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("ProfessorList.fxml",dateTime);
    }
}
