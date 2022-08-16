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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import server.university.University;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EducationalStatusPage implements Initializable {
    private static Logger log = LogManager.getLogger(EducationalStatusPage.class);
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
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Main.mainClient.getUser() instanceof Student) {
            setupStudent();
        }
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    private void setupStudent(){
        student = (Student) Main.mainClient.getUser();
        studentBox.setVisible(false);
        nameField.setVisible(false);
        nameSearchButton.setVisible(false);
        idSearchButton.setVisible(false);
        idField.setVisible(false);
        setupTable();
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
        ArrayList<Student> students= requestStudents();
        //System.out.println(students);
        for (Student studentCheck: students){
            if (studentCheck.getId().contains(id))
                studentBox.getItems().add(studentCheck);
        }
        log.info(Main.mainClient.getUser().getId()+" filtered students by id");
    }

    public void searchName(ActionEvent actionEvent) {
        String name=nameField.getText();
        studentBox.getItems().clear();
        ArrayList<Student> students= requestStudents();
        //System.out.println(students);
        for (Student studentCheck:students){
            if (studentCheck.getUsername().contains(name))
                studentBox.getItems().add(studentCheck);
        }
        log.info(Main.mainClient.getUser().getId()+" filtered students by name");
    }

    public void pickStudent(ActionEvent actionEvent) {
        this.student=studentBox.getValue();
        log.info(Main.mainClient.getUser().getId()+" picked a student.");
        if (student == null) return;
        setupTable();
    }

    private ArrayList<Student> requestStudents(){
        Response response=Main.mainClient.getServerController().getStudents();
        String listString = (String)response.getData("list");
        return JsonCaster.studentArrayListCaster(listString);
    }
}
