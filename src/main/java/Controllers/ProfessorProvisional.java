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
import logic.LogicalAgent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ProfessorProvisional implements Initializable {
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

    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    public void filter(ActionEvent actionEvent) {
        if (coursesId.getValue() == null)
            return;

        String courseId = coursesId.getValue();
        Course course = Course.getCourse(courseId);
        ObservableList<Grade> studentGrade = FXCollections.observableArrayList();
        ObservableList<Student> students = FXCollections.observableArrayList();
        for (String id : course.getStudentId()) {
            Student student = Student.getStudent(id);
            Grade grade = student.getGrade(courseId);
            if (grade==null) continue;
            grade.setGradeStatus();
            studentGrade.add(grade);
            students.add(student);
        }
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
        Professor professor = (Professor) LogicalAgent.getInstance().getUser();
        for (String courseId : professor.getCoursesId())
            coursesId.getItems().add(courseId);
    }

    public void submit(ActionEvent actionEvent) {
        String grade = gradeField.getText(), id = idField.getText();
        String courseId = coursesId.getValue();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went Wrong!");

        Student student = Student.getStudent(id);
        if (coursesId.getValue() == null) return;
        if (student==null){
            alert.setContentText("enter a correct student Id");
            alert.show();
            return;
        }
        Grade studentGrade = student.getGrade(courseId);
        Course course = Course.getCourse(courseId);
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
            studentGrade.setFinished(true);
            double professorGrade = Double.parseDouble(grade);
            professorGrade = Math.round(professorGrade / 0.25) * 0.25;
            studentGrade.setGrade(professorGrade);
            studentGrade.setGradeStatus();
        } else if (studentGrade == null) {
            alert.setContentText("this course grades are final.");
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
        Course course = Course.getCourse(courseId);
        boolean check = true;
        for (String id : course.getStudentId()) {
            Student student = Student.getStudent(id);
            Grade grade = student.getGrade(courseId);
            if (!grade.isFinished())
                check = false;
        }
        if (!check){
            alert.setContentText("you haven't submitted all of the grades");
        }
        else if (check){
            alert.setContentText("this course grades are final!");
            for (String id : course.getStudentId()) {
                Student student = Student.getStudent(id);
                Grade grade = student.getGrade(courseId);
                grade.setFinalGrade(true);
            }
        }
        alert.show();
    }

    public void answerObjection(ActionEvent actionEvent) {
        String courseId = coursesId.getValue(), id = idField.getText();
        Student student = Student.getStudent(id);
        Grade studentGrade = student.getGrade(courseId);
        Course course = Course.getCourse(courseId);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went Wrong!");
        if (coursesId.getValue() == null) return;
        if (student==null){
            alert.setContentText("enter a correct student Id");
            alert.show();
            return;
        }
        if (!course.getStudentId().contains(id))
            alert.setContentText("enter a correct student Id.");
        else if (studentGrade == null)
            alert.setContentText("this course grades are final.");
        else if (!studentGrade.isObjection())
            alert.setContentText("student has no objection yet");
        else if (studentGrade.isObjection()){
            studentGrade.setAnswered(true);
            studentGrade.setAnswerText(answerField.getText());
            alert.setTitle("SuccessFull");
            alert.setHeaderText("status");
            alert.setContentText("answer submitted");
        }
        alert.show();
    }
}
