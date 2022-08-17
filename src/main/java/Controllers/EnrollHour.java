package Controllers;


import elements.people.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EnrollHour implements Initializable {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button homePageButton;
    @FXML
    public Button dateSetterButton;

    @FXML
    public ComboBox<String> universityDateBox;

    @FXML
    public DatePicker universityDate;

    @FXML
    public TextField hourField;
    
    @FXML
    public Button studentDateButton;
    @FXML
    public TextField studentHour;
    @FXML
    public DatePicker studentDate;
    @FXML
    public ComboBox<Student> studentBox;
    @FXML
    public CheckBox yearCheck;

    @FXML
    public TextField enterYear;
    @FXML
    public CheckBox degreeCheck;
    @FXML
    public ComboBox<String> degree;

    @FXML
    public Button filterButton;

    private static Logger log = LogManager.getLogger(EnrollHour.class);
    @FXML
    public CheckBox register;

    public void goHomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupUniversityBox();
        setupStudentBox();
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    private void setupStudentBox() {
        studentBox.getItems().clear();
        for (Student student : requestStudents()){
            boolean canAdd = true;
            if (degreeCheck.isSelected() && !degree.getValue().equals(""))
                if (!degree.getValue().equals(student.getDegree()))
                    canAdd=false;
            if (yearCheck.isSelected() && enterYear.getText().matches("[0-9]*"))
                if (!student.getEnterYear().equals(enterYear.getText()))
                    canAdd=false;
            if (canAdd)
                studentBox.getItems().add(student);
        }
    }

    private void setupUniversityBox() {
        universityDateBox.getItems().addAll("starting date","ending date");
        degree.getItems().addAll("Bachelor", "Masters", "PHD");
    }

    public void setUniversityDate(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("status");
        if (hourField.getText().equals(""))
            alert.setContentText("enter an hour");
        else if (universityDate.getValue() == null)
            alert.setContentText("select a date");
        else if (!hourField.getText().matches("[0-9]*"))
            alert.setContentText("enter only numbers");
        else if (Integer.parseInt(hourField.getText())<0 || Integer.parseInt(hourField.getText())>23)
            alert.setContentText("numbers should be between 0-23");
        else if (universityDateBox.getValue().equals(""))
            alert.setContentText("pick what you're choosing");
        else {
            int hour = Integer.parseInt(hourField.getText());
            LocalDateTime date = universityDate.getValue().atTime(hour,0,0);
            alert.setContentText("date submitted");
            if (universityDateBox.getValue().equals("starting date"))
                Main.mainClient.getServerController().sendStartingDate(date);
            else
                Main.mainClient.getServerController().sendEndingDate(date);
        }
        alert.show();
    }

    public void setStudentDate(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("status");
        if (studentBox.getValue() != null && !register.isSelected()) {
            Main.mainClient.getServerController().sendNotRegister(studentBox.getValue().getId());
            alert.setContentText("no register for him\\her");
        }
        else if (studentHour.getText().equals(""))
            alert.setContentText("enter an hour");
        else if (studentDate.getValue() == null)
            alert.setContentText("select a date");
        else if (!studentHour.getText().matches("[0-9]*"))
            alert.setContentText("enter only numbers");
        else if (Integer.parseInt(studentHour.getText())<0 || Integer.parseInt(studentHour.getText())>23)
            alert.setContentText("numbers should be between 0-23");
        else if (studentDate.getValue().equals(""))
            alert.setContentText("pick what you're choosing");
        else if (studentBox.getValue() == null)
            alert.setContentText("pick a student");
        else {
            int hour = Integer.parseInt(studentHour.getText());
            LocalDateTime date = studentDate.getValue().atTime(hour,0,0);
            Main.mainClient.getServerController().sendStudentDate(studentBox.getValue().getId(),date);
            alert.setContentText("date submitted");

        }
        alert.show();
    }

    private ArrayList<Student> requestStudents(){
        Response response=Main.mainClient.getServerController().getStudents();
        String listString = (String)response.getData("list");
        return JsonCaster.studentArrayListCaster(listString);
    }

    public void filter(ActionEvent actionEvent) {
        setupStudentBox();
        log.info("students were filtered");
    }
}
