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
import logic.StudentHomePageLogic;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class StudentHomePage implements Initializable {

    @FXML
    Label dateTime,lastEnter,email,name;
    @FXML
    Label education,supervisor,permission,enrollTime;
    @FXML
    ImageView imageView;
    @FXML
    MenuItem minor,thesisDefense,signOut;
    @FXML
    MenuItem courseList,professorList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClock();
        initUser(LogicalAgent.getInstance().getUser());
        initStudent((Student)(LogicalAgent.getInstance().getUser()));
    }
    public void initStudent(Student student){
        education.setText(student.isEducating()?"undergraduate":"educating");
        supervisor.setText("you supervisor is Dr."+Professor.getProfessor(student.getSupervisorId()).getUsername());
        permission.setText("you "+(student.isEnrollPermission()?"have":"don't have")+" permission to enroll");
        enrollTime.setText("enroll time: "+(student.isEnrollPermission()?student.getEnrollHour():"not set"));
        setVisiblity(student);
        Saver.getInstance().saveStudent(student);
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
        initImage(user);
    }
    public void initImage(User user){
        try {
            String filename=user.getId()+".png";
            InputStream stream = new FileInputStream(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename);
            Image image = new Image(stream);
            imageView.setImage(image);
            stream.close();
        } catch (IOException e){
            e.printStackTrace();
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
}
