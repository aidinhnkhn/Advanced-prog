package Controllers;

import elements.courses.Course;
import elements.courses.Grade;
import elements.people.Professor;
import elements.people.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.LogicalAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EducationalGrades implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    Button homePageButton, searchButton;
    @FXML
    ComboBox<String> studentBox;
    @FXML
    ComboBox<Professor> professorBox;
    @FXML
    ComboBox<String> courseBox;
    @FXML
    TextField nameField;
    @FXML
    Label dateTime;
    @FXML
    TextArea showText;
    private static Logger log = LogManager.getLogger(EducationalGrades.class);
    public void HomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList <Student> students = requestStudents();
        for (Student student : students)
            studentBox.getItems().add(student.getId());
        ArrayList <Course> courses = requestCourses();
        for (Course course : courses)
            courseBox.getItems().add(course.getId());
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }


    public void saveTextFile(String sampleText) {
        Text sample = new Text(sampleText);
        sample.setFont(new Font(14));
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        Stage stage = (Stage) (dateTime.getScene().getWindow());
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveTextToFile(sampleText, file);
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
            log.info("saved the file! "+file.getName());
        } catch (IOException ex) {
            log.error("couldn't save the file!");
        }
    }

    public void studentFile(ActionEvent actionEvent) {
        String id = studentBox.getValue();
        Student student = Main.mainClient.getServerController().getStudentById(id);
        StringBuilder gradeText = new StringBuilder(student.getUsername() + " grades:\n");
        for (Grade grade : student.getGrades()) {
            if (grade.isFinalGrade()) continue;
            gradeText.append(grade.getName() + ": " + grade.getGradeStatus() + "\n");
            gradeText.append("Objection: " + grade.getObjectionText() + "\n");
            gradeText.append("answered Objection: " + grade.getAnswerText() + "\n");
        }
        log.info(student.getId()+ " information were requested! by :"+Main.mainClient.getUser().getId());
        saveTextFile(gradeText.toString());
        showText.setText(gradeText.toString());
    }

    public void search(ActionEvent actionEvent) {
        String name = nameField.getText();
        professorBox.getItems().clear();
        //TODO: get professor list
        ArrayList<Professor> professors = requestProfessors();
        for (Professor professor : professors) {
            if (professor == null) System.out.println("chera nunll mide");
            if (professor.getUsername().contains(name))
                professorBox.getItems().add(professor);
        }
        log.info(Main.mainClient.getUser().getId()+ "filtered professor by name!");
    }

    public void courseFile(ActionEvent actionEvent) {
        int studentNumber=0,passedStudentNumber=0;
        double gradeSum=0,passedGradeSum=0;
        Course course = Main.mainClient.getServerController().getCourseById(courseBox.getValue());
        StringBuilder gradeText = new StringBuilder(course.getName() + " grades:\n");
        for (String id : course.getStudentId()) {
            Student student = Main.mainClient.getServerController().getStudentById(id);
            if (student == null) continue;
            Grade grade = student.getFinalGrades(course.getId());
            if (grade == null) continue;
            if(grade.isFinished()) {
                gradeSum += grade.getGrade();
                studentNumber++;
                if (grade.getGrade() >= 10) {
                    passedGradeSum += grade.getGrade();
                    passedStudentNumber++;
                }
            }
            gradeText.append(student.getUsername() + ": " + grade.getGradeStatus() + "\n");
            gradeText.append("Objection: " + grade.getObjectionText() + "\n");
            gradeText.append("answered Objection: " + grade.getAnswerText() + "\n");
        }
        if (passedStudentNumber!=0)
            gradeText.append("Course passed Average: "+(passedGradeSum/passedStudentNumber)+"\n");
        if (studentNumber!=0) {
            gradeText.append("Course Average: " + (gradeSum / studentNumber) + "\n");
            gradeText.append("passed number: "+ passedStudentNumber+"\n");
            gradeText.append("failed number: "+(studentNumber-passedStudentNumber)+"\n");
        }
        log.info(course.getId()+ " information were requested! by :"+Main.mainClient.getUser().getId());
        saveTextFile(gradeText.toString());
        showText.setText(gradeText.toString());
    }

    public void professorFile(ActionEvent actionEvent) {
        Professor professor = professorBox.getValue();
        StringBuilder gradeText = new StringBuilder(professor.getUsername() + " grades:\n");
        for (String courseId : professor.getCoursesId()) {
            Course course = Main.mainClient.getServerController().getCourseById(courseId);
            if (course == null) continue;
            gradeText.append(course.getName() + " has this final grades:\n");
            for (String studentId : course.getStudentId()) {
                Student student = Main.mainClient.getServerController().getStudentById(studentId);
                if (student == null) continue;
                Grade grade = student.getFinalGrades(courseId);
                if (grade == null) continue;
                if (!grade.isFinalGrade()) continue;
                gradeText.append(student.getUsername() + ": " + grade.getGradeStatus() + "\n");
                gradeText.append("Objection: " + grade.getObjectionText() + "\n");
                gradeText.append("answered Objection: " + grade.getAnswerText() + "\n");
            }
        }
        log.info(professor.getId()+ " information were requested! by :"+Main.mainClient.getUser().getId());
        saveTextFile(gradeText.toString());
        showText.setText(gradeText.toString());
    }

    private ArrayList<Student> requestStudents(){
        Response response=Main.mainClient.getServerController().getStudents();
        String listString = (String)response.getData("list");
        return JsonCaster.studentArrayListCaster(listString);
    }

    private ArrayList<Course> requestCourses() {
        Response response=Main.mainClient.getServerController().getCourses();
        String listString = (String)response.getData("list");
        return JsonCaster.courseArrayListCaster(listString);
    }

    private ArrayList<Professor> requestProfessors() {
        Response response=Main.mainClient.getServerController().getProfessors();
        String listString = (String)response.getData("list");
        return JsonCaster.professorArrayListCaster(listString);
    }
}
