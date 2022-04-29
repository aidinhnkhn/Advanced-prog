package Controllers;

import elements.courses.Course;
import elements.courses.Grade;
import elements.people.Professor;
import elements.people.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ExamList implements Initializable {

    private static Logger log = LogManager.getLogger(ExamList.class);

    public AnchorPane anchorPane;
    @FXML
     Button homePageButton;

    @FXML
     TableColumn<Course, String> nameColumn;

    @FXML
    TableView<Course> tableView;

    @FXML
    TableColumn<Course, LocalDateTime> dateColumn;

    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupTable(getItems());

        if (LogicalAgent.getInstance().getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }
    public void setupTable(ObservableList<Course> items){
        tableView.setItems(items);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Course,String>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Course, LocalDateTime>("examDate"));
    }
    public ObservableList<Course> getItems(){
        ObservableList<Course> courses= FXCollections.observableArrayList();
        List<Course> courseList = new ArrayList();
        if (LogicalAgent.getInstance().getUser() instanceof Student) {
            Student student=(Student)(LogicalAgent.getInstance().getUser());
            for (Grade grade:student.getGrades())
                courseList.add(Course.getCourse(grade.getCourseId()));
        }
        else{
            Professor professor=(Professor) (LogicalAgent.getInstance().getUser());
            for (String courseId:professor.getCoursesId())
                courseList.add(Course.getCourse(courseId));
        }
        courseList.sort(Comparator.comparing(Course::getExamDate));
        courses.addAll(courseList);
        return courses;
    }
}
