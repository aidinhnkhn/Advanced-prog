package Controllers;

import elements.courses.Course;
import elements.courses.CourseSchedule;
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
import java.util.ResourceBundle;

public class SchedulePage implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    Button homePageButton;
    @FXML
    public TableColumn<CourseSchedule, String> name;
    @FXML
    public TableColumn<CourseSchedule, String> monday;
    @FXML
    public TableColumn<CourseSchedule, String> tuesday;
    @FXML
    public TableColumn<CourseSchedule, String> wednesday;
    @FXML
    public TableColumn<CourseSchedule, String> thursday;
    @FXML
    public TableColumn<CourseSchedule, String> friday;

    @FXML
    public TableView<CourseSchedule> tableView;

    private static Logger log = LogManager.getLogger(SchedulePage.class);
    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
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

    public void setupTable(ObservableList<CourseSchedule> items) {
        tableView.setItems(items);
        name.setCellValueFactory(new PropertyValueFactory<CourseSchedule,String >("name"));
        monday.setCellValueFactory(new PropertyValueFactory<CourseSchedule,String >("monday"));
        tuesday.setCellValueFactory(new PropertyValueFactory<CourseSchedule,String >("tuesday"));
        wednesday.setCellValueFactory(new PropertyValueFactory<CourseSchedule,String >("wednesday"));
        thursday.setCellValueFactory(new PropertyValueFactory<CourseSchedule,String >("thursday"));
        friday.setCellValueFactory(new PropertyValueFactory<CourseSchedule,String >("friday"));
    }

    public ObservableList<CourseSchedule> getItems() {
        ObservableList<CourseSchedule> courses = FXCollections.observableArrayList();
        if (LogicalAgent.getInstance().getUser() instanceof Student) {
            Student student = (Student) (LogicalAgent.getInstance().getUser());
            for (Grade grade : student.getGrades())
                if (!grade.isFinished())
                    courses.add(new CourseSchedule(Course.getCourse(grade.getCourseId())));
        } else {
            Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
            for (String courseId : professor.getCoursesId())
                courses.add(new CourseSchedule(Course.getCourse(courseId)));
        }
        return courses;
    }
}
