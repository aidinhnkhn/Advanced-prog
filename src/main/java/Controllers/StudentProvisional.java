package Controllers;

import elements.courses.Grade;
import elements.people.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.LogicalAgent;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentProvisional implements Initializable {
    @FXML
    Button homePageButton,objectButton;
    @FXML
    TableView<Grade> tableView;
    @FXML
    TableColumn<Grade,String> nameColumn;
    @FXML
    TableColumn<Grade,String> idColumn;
    @FXML
    TableColumn<Grade,String> gradeColumn;
    @FXML
    TableColumn<Grade,String> answeredColumn;
    @FXML
    TextField courseIdField,objectionField;
    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
    }
    public void setupTable(){
        ObservableList<Grade> grades= FXCollections.observableArrayList();
        Student student=(Student) LogicalAgent.getInstance().getUser();
        for (Grade grade:student.getGrades()){
            if (grade.isFinalGrade()) continue;
            grade.setGradeStatus();
            grades.add(grade);
        }
        tableView.setItems(grades);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Grade,String>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Grade,String>("courseId"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<Grade,String>("gradeStatus"));
        answeredColumn.setCellValueFactory(new PropertyValueFactory<Grade,String>("answerText"));
    }

    public void object(ActionEvent actionEvent) {
        String courseId=courseIdField.getText();
        Student student=(Student) LogicalAgent.getInstance().getUser();
        Grade grade=student.getGrade(courseId);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("objection");
        alert.setHeaderText("status");
        if (grade==null)
            alert.setContentText("enter a correct course id");
        else if (!grade.isFinished())
            alert.setContentText("Professor hasn't entered a grade");
        else if (grade.isObjection())
            alert.setContentText("you already have objected");
        else{
            alert.setContentText("you seriously think the professor is gonna answer???? :))");
            grade.setObjection(true);
            grade.setObjectionText(objectionField.getText());
        }
        alert.show();
    }
}
