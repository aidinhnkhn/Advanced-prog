package Controllers;

import elements.courses.Course;
import elements.courses.Grade;
import elements.people.Professor;
import elements.people.Student;
import elements.request.MinorRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import site.edu.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ProfessorProvisional implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    Button homePageButton, filterButton;
    @FXML
    ComboBox<String> coursesId;

    @FXML
    TableView<Grade> gradeTable;
    @FXML
    TableView<Student> studentTable;
    @FXML
    TableColumn<Student, String> nameColumn;
    @FXML
    TableColumn<Student, String> idColumn;
    @FXML
    TableColumn<Grade, String> gradeColumn;
    @FXML
    TableColumn<Grade, String> objectionColumn;
    @FXML
    TableColumn<Grade, Boolean> answeredColumn;

    @FXML
    Button submitButton, finalizeButton,answerButton;
    @FXML
    TextField gradeField, idField,answerField;
    private static Logger log = LogManager.getLogger(ProfessorProvisional.class);
    public void HomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    public void filter(ActionEvent actionEvent) {
        if (coursesId.getValue() == null)
            return;

        String courseId = coursesId.getValue();

        Course course = Main.mainClient.getServerController().getCourseById(courseId);

        ObservableList<Grade> studentGrade = FXCollections.observableArrayList();
        ObservableList<Student> students = FXCollections.observableArrayList();
        for (String id : course.getStudentId()) {

            Student student = Main.mainClient.getServerController().getStudentById(id);
            Grade grade = student.getGrade(courseId);
            if (grade==null) continue;
            grade.setGradeStatus();
            studentGrade.add(grade);
            students.add(student);
        }
        log.info("user changed the filter!");
        setupTable(studentGrade, students);
    }

    public void setupTable(ObservableList<Grade> grades, ObservableList<Student> students) {
        studentTable.setItems(students);
        gradeTable.setItems(grades);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("username"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<Grade, String>("gradeStatus"));
        objectionColumn.setCellValueFactory(new PropertyValueFactory<Grade, String>("objectionText"));
        answeredColumn.setCellValueFactory(new PropertyValueFactory<Grade, Boolean>("answered"));
        studentTable.refresh();
        gradeTable.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Professor professor = (Professor) Main.mainClient.getUser();
        for (String courseId : professor.getCoursesId())
            coursesId.getItems().add(courseId);
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    public void submit(ActionEvent actionEvent) {
        String grade = gradeField.getText(), id = idField.getText();
        String courseId = coursesId.getValue();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went Wrong!");

        Student student = Main.mainClient.getServerController().getStudentById(id);
        if (coursesId.getValue() == null) return;
        if (student==null){
            alert.setContentText("enter a correct student Id");
            alert.show();
            return;
        }
        Grade studentGrade = student.getGrade(courseId);
        Course course = Main.mainClient.getServerController().getCourseById(courseId);
        String regExp = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
        if (!course.getStudentId().contains(id))
            alert.setContentText("enter a correct student Id.");
        else if (!grade.matches(regExp))
            alert.setContentText("grade should be all Integer");
        else if (Double.parseDouble(grade) < 0 || Double.parseDouble(grade) > 20)
            alert.setContentText("grade should be between 0 and 20");
        else if (studentGrade != null) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("SuccessFull");
            alert.setHeaderText("status");
            alert.setContentText("grade submitted");

            double professorGrade = Double.parseDouble(grade);
            professorGrade = Math.round(professorGrade / 0.25) * 0.25;

            Main.mainClient.getServerController().sendStudentGrade(id,courseId,professorGrade);
            log.info("grade submitted");
        } else if (studentGrade == null) {
            alert.setContentText("this course grades are final.");
            log.warn("student grade is null");
        }
        alert.show();
        filter(actionEvent);
    }

    public void finalize(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Submit All");
        alert.setHeaderText("Status");
        if (coursesId.getValue() == null)
            return;

        String courseId = coursesId.getValue();


        boolean check = Main.mainClient.getServerController().finalizeGrades(courseId,Main.mainClient.getUser().getId());

        if (!check)
            alert.setContentText("you haven't submitted all of the grades");

        else if (check)
            alert.setContentText("this course grades are final!");

        alert.show();
        filter(actionEvent);
    }

    public void answerObjection(ActionEvent actionEvent) {
        String courseId = coursesId.getValue(), id = idField.getText();
        Student student = Main.mainClient.getServerController().getStudentById(id);
        Grade studentGrade = student.getGrade(courseId);
        Course course = Main.mainClient.getServerController().getCourseById(courseId);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went Wrong!");
        if (coursesId.getValue() == null) return;
        if (student==null){
            alert.setContentText("enter a correct student Id");
            alert.show();
            log.warn("student is null");
            return;
        }
        if (!course.getStudentId().contains(id))
            alert.setContentText("enter a correct student Id.");
        else if (studentGrade == null)
            alert.setContentText("this course grades are final.");
        else if (!studentGrade.isObjection())
            alert.setContentText("student has no objection yet");
        else if (studentGrade.isObjection()){
            Main.mainClient.getServerController().answerObjection(answerField.getText(),id,courseId);

            alert.setTitle("SuccessFull");
            alert.setHeaderText("status");
            alert.setContentText("answer submitted");
            log.info(student.getId()+" objection to "+course.getId()+" got answered!");
        }
        alert.show();
        filter(actionEvent);
    }
}
