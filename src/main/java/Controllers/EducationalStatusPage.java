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
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;

import java.net.URL;
import java.util.ResourceBundle;

public class EducationalStatusPage implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    Button homePageButton;
    @FXML
    ComboBox<Student> studentBox;
    @FXML
    TextField nameField,idField;
    @FXML
    Button nameSearchButton,idSearchButton;
    @FXML
    TableView<Grade> tableView;
    @FXML
    TableColumn<Grade,String> nameColumn;
    @FXML
    TableColumn<Grade,Integer> unitColumn;
    @FXML
    TableColumn<Grade,String> gradeColumn;
    @FXML
    Label statusLabel;
    private Student student;
    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (LogicalAgent.getInstance().getUser() instanceof Student) {
            student = (Student) LogicalAgent.getInstance().getUser();
            studentBox.setVisible(false);
            nameField.setVisible(false);
            nameSearchButton.setVisible(false);
            idSearchButton.setVisible(false);
            idField.setVisible(false);
            setupTable();
        }
        if (LogicalAgent.getInstance().getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }
    public void setupTable(){
        ObservableList<Grade> grades= FXCollections.observableArrayList();
        int passed=0;
        for (Grade grade:student.getGrades()){
            if (!grade.isFinalGrade()) continue;
            if (grade.getGrade()>=10) passed+=grade.getUnit();
            grades.add(grade);
        }
        double average=Math.round(student.getَAverage() / 0.01) * 0.01;
        statusLabel.setText("Average: "+average+"       units passed: "+passed);
        tableView.setItems(grades);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Grade,String>("name"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<Grade,Integer>("unit"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<Grade,String>("gradeStatus"));
    }

    public void searchId(ActionEvent actionEvent) {
        String id=idField.getText();
        studentBox.getItems().clear();
        for (Student studentCheck:Student.getStudents()){
            if (studentCheck.getId().contains(id))
                studentBox.getItems().add(student);
        }
    }

    public void searchName(ActionEvent actionEvent) {
        String name=nameField.getText();
        studentBox.getItems().clear();
        for (Student studentCheck:Student.getStudents()){
            if (studentCheck.getUsername().contains(name))
                studentBox.getItems().add(student);
        }
    }

    public void pickStudent(ActionEvent actionEvent) {
        this.student=studentBox.getValue();
        setupTable();
    }
}
