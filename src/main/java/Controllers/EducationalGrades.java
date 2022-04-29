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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
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

    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Student student : Student.getStudents())
            studentBox.getItems().add(student.getId());
        for (Course course : Course.getCourses())
            courseBox.getItems().add(course.getId());
        if (LogicalAgent.getInstance().getUser().isTheme()) {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void studentFile(ActionEvent actionEvent) {
        Student student = Student.getStudent(studentBox.getValue());
        StringBuilder gradeText = new StringBuilder(student.getUsername() + " grades:\n");
        for (Grade grade : student.getGrades()) {
            if (grade.isFinalGrade()) continue;
            gradeText.append(grade.getName() + ": " + grade.getGradeStatus() + "\n");
            gradeText.append("Objection: " + grade.getObjectionText() + "\n");
            gradeText.append("answered Objection: " + grade.getAnswerText() + "\n");
        }
        saveTextFile(gradeText.toString());
        showText.setText(gradeText.toString());
    }

    public void search(ActionEvent actionEvent) {
        String name = nameField.getText();
        professorBox.getItems().clear();
        for (Professor professor : Professor.getProfessors()) {
            if (professor.getUsername().contains(name))
                professorBox.getItems().add(professor);
        }
    }

    public void courseFile(ActionEvent actionEvent) {
        int studentNumber=0,passedStudentNumber=0;
        double gradeSum=0,passedGradeSum=0;
        Course course = Course.getCourse(courseBox.getValue());
        StringBuilder gradeText = new StringBuilder(course.getName() + " grades:\n");
        for (String id : course.getStudentId()) {
            Student student = Student.getStudent(id);
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
        saveTextFile(gradeText.toString());
        showText.setText(gradeText.toString());
    }

    public void professorFile(ActionEvent actionEvent) {
        Professor professor = professorBox.getValue();
        StringBuilder gradeText = new StringBuilder(professor.getUsername() + " grades:\n");
        for (String courseId : professor.getCoursesId()) {
            Course course = Course.getCourse(courseId);
            if (course == null) continue;
            gradeText.append(course.getName() + " has this final grades:\n");
            for (String studentId : course.getStudentId()) {
                Student student = Student.getStudent(studentId);
                if (student == null) continue;
                Grade grade = student.getFinalGrades(courseId);
                if (grade == null) continue;
                if (!grade.isFinalGrade()) continue;
                gradeText.append(student.getUsername() + ": " + grade.getGradeStatus() + "\n");
                gradeText.append("Objection: " + grade.getObjectionText() + "\n");
                gradeText.append("answered Objection: " + grade.getAnswerText() + "\n");
            }
        }
        saveTextFile(gradeText.toString());
        showText.setText(gradeText.toString());
    }
}
