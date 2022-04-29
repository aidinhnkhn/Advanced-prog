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
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import logic.LogicalAgent;
import logic.StudentHomePageLogic;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class StudentHomePage implements Initializable {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    Label dateTime,lastEnter,email,name;
    @FXML
    Label education,supervisor,permission,enrollTime;
    @FXML
    ImageView imageView;
    @FXML
    MenuItem minor,thesisDefense,signOut;
    @FXML
    MenuItem courseList,professorList,Dorm,scheduleItem;
    @FXML
    MenuItem certificateStudent,freedom,Recommendation,examlistItem;
    @FXML
    MenuItem provisional,educationalStatus;
    @FXML
    MenuItem profileItem;

    static Logger log = LogManager.getLogger(StudentHomePage.class);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClock();
        initUser(LogicalAgent.getInstance().getUser());
        initStudent((Student)(LogicalAgent.getInstance().getUser()));
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
    public void initStudent(Student student){
        education.setText(student.isEducating()?"undergraduate":"educating");
        try {
            supervisor.setText("you supervisor is Dr." + Professor.getProfessor(student.getSupervisorId()).getUsername());
        } catch (NullPointerException e){
            log.error("user name is null");
        }
        permission.setText("you "+(student.isEnrollPermission()?"have":"don't have")+" permission to enroll");
        enrollTime.setText("enroll time: "+(student.isEnrollPermission()?student.getEnrollHour():"not set"));
        setVisiblity(student);
        Saver.getInstance().saveStudent(student);
        log.info("student info loaded!");
    }
    private void setVisiblity(Student student){
        if (student.getDegree().equals("Bachelor"))
            minor.setVisible(true);
        if (student.getDegree().equals("PHD"))
            thesisDefense.setVisible(true);
    }
    public void initUser(User user){
        lastEnter.setText("last enter: "+user.getDateString());
        name.setText("name: "+user.getUsername());
        email.setText("email: "+user.getEmail());
        log.info("user info loaded!");
        initImage(user);
    }
    public void initImage(User user){
        try {
            String filename=user.getId()+".png";
            File file=new File(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename);
            if (!file.exists()){
                log.warn("could not load image");
                return;
            }
            InputStream stream = new FileInputStream(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename);
            Image image = new Image(stream);
            imageView.setImage(image);
            stream.close();
            log.trace("user image loaded!");
        } catch (IOException e){
            e.printStackTrace();
            log.error("couldn't load image");
        }
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

    public void exit(ActionEvent actionEvent) {

       StudentHomePageLogic.getInstance().exit(education);
    }

    public void GoCourseList(ActionEvent actionEvent) {

        SceneLoader.getInstance().ChangeSceneByNode("CourseList.fxml",dateTime);
    }

    public void openProfessorList(ActionEvent actionEvent) {

        SceneLoader.getInstance().ChangeSceneByNode("ProfessorList.fxml",dateTime);
    }

    public void openCertificateStudent(ActionEvent actionEvent) {

        SceneLoader.getInstance().ChangeSceneByNode("CertificateStudentPage.fxml",dateTime);
    }

    public void openMinorPage(ActionEvent actionEvent) {

        SceneLoader.getInstance().ChangeSceneByNode("MinorRequestPage.fxml",dateTime);
    }


    public void OpenFreedomRequest(ActionEvent actionEvent) {

        SceneLoader.getInstance().ChangeSceneByNode("FreedomRequestPage.fxml",dateTime);
    }

    public void OpenRecomPage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("RecomPage.fxml",dateTime);
    }

    public void openDormPage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("DormRequestPage.fxml",dateTime);
    }

    public void openThesisDefensePage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("ThesisDefensePage.fxml",dateTime);
    }

    public void openExamList(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("examList.fxml",dateTime);
    }

    public void openSchedulePage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("SchedulePage.fxml",dateTime);
    }

    public void openStudentProvisional(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("StudentProvisional.fxml",dateTime);
    }

    public void openEducationalStatusPage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("EducationalStatusPage.fxml",dateTime);
    }

    public void openProfilePage(ActionEvent actionEvent) {
        SceneLoader.getInstance().ChangeSceneByNode("ProfilePage.fxml",dateTime);
    }
}
