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
import site.edu.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentProvisional implements Initializable {
    @FXML
    public AnchorPane anchorPane;
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
    private static Logger log = LogManager.getLogger(StudentProvisional.class);
    public void HomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        if (Main.mainClient.getUser().isTheme()) {
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
        Student student=(Student) Main.mainClient.getUser();
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
        Student student=(Student) Main.mainClient.getUser();
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
            Main.mainClient.getServerController().sendObjection(courseId,objectionField.getText());
            log.info(Main.mainClient.getUser().getId()+ " objected for the course: "+ grade.getCourseId());
        }
        alert.show();
    }
}
