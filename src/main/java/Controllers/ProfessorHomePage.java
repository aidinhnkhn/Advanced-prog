package Controllers;

import server.Savers.Saver;
import elements.people.Professor;
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
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import logic.LogicalAgent;
import logic.ProfessorHomePageLogic;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProfessorHomePage implements Initializable {
    @FXML
    public MenuItem courseList;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    ImageView imageView;
    @FXML
    Label dateTime, lastEnter, email, name;
    @FXML
    MenuItem educationalStatus,signUp,editCourse,editProfessor,educationalAssistancePage;
    @FXML
    MenuItem professorList,examlistItem,provisionalItem;

    @FXML
    MenuItem profileItem;
    private static Logger log = LogManager.getLogger(ProfessorHomePage.class);
    public void initialize(URL location, ResourceBundle resources) {
        initClock();
        initUser(LogicalAgent.getInstance().getUser());
        setVisibility((Professor) (LogicalAgent.getInstance().getUser()));
        Saver.getInstance().saveProfessor((Professor) (LogicalAgent.getInstance().getUser()));
        if (LogicalAgent.getInstance().getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");


        LogicalAgent.getInstance().getUser().setLastEnter(LocalDateTime.now());
    }
    public void setVisibility(Professor professor){
        if (professor.isEducationalAssistant()) {
            educationalStatus.setVisible(true);
            signUp.setVisible(true);
            editCourse.setVisible(true);
            educationalAssistancePage.setVisible(true);
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
        log.info("User loaded.");
    }

    public void initImage(User user) {
        try {
            String filename = user.getId() + ".png";
            File file=new File(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename);
            if (!file.exists()){
                log.warn("image couldn't be loaded");
                return;
            }
            InputStream stream = new FileInputStream(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename);
            Image image = new Image(stream);
            imageView.setImage(image);
            stream.close();
        } catch (IOException e) {
            log.error("couldn't write the image");
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

    public void OpenRequestPage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("ProfessorRequestPage.fxml",dateTime);
    }

    public void openExamList(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("examList.fxml",dateTime);
    }

    public void openSchedulePage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("SchedulePage.fxml",dateTime);
    }

    public void OpenProvisionalPage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("ProfessorProvisional.fxml",dateTime);
    }

    public void openEducationalGrades(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("EducationalGrades.fxml",dateTime);
    }

    public void openEducationalStatusPage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("EducationalStatusPage.fxml",dateTime);
    }

    public void openProfilePage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("ProfilePage.fxml",dateTime);
    }
}
