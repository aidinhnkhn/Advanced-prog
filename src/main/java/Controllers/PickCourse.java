package Controllers;

import elements.courses.Course;
import elements.people.Student;
import elements.university.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.net.URL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PickCourse implements Initializable {
    @FXML
    public Button homePageButton;
    @FXML
    public ComboBox<String> departmentBox;
    @FXML
    public ComboBox<String> sortBox;
    @FXML
    public Button sortButton;
    @FXML
    public Button suggestionButton;
    @FXML
    public TableColumn<Course, String> courseId;

    @FXML
    public TableColumn<Course, ArrayList<String> > days;

    @FXML
    public TableColumn<Course, String> department;

    @FXML
    public TableColumn<Course, String> professorName;

    @FXML
    public TableView<Course> tableView;

    @FXML
    public TableColumn<Course, Integer> unit;
    @FXML
    public TableColumn<Course, String> name;
    @FXML
    public TableColumn<Course, String> degree;
    @FXML
    public TableColumn<Course, LocalDateTime> examDate;
    @FXML
    public TableColumn<Course, Integer> capacity;
    @FXML
    public TableColumn<Course, String> previous;
    @FXML
    public TableColumn<Course, String> now;
    @FXML
    private AnchorPane anchorPane;

    public void initialize(URL location, ResourceBundle resources) {
        setupTable(getItems());
        setupBoxes();
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        } else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    private void setupBoxes() {
        for (Department department1 : requestDepartments())
            departmentBox.getItems().add(department1.getName());
        sortBox.getItems().addAll("Bachelor","Masters","PHD","exam Date","alphabetic");
    }

    public ObservableList<Course> getItems(){
        ObservableList<Course> courses= FXCollections.observableArrayList();
        ArrayList<Course> availableCourses  = requestCourses();
        for (Course course:availableCourses) {
            courses.add(course);
            System.out.println(course.getDegree());
        }
        return courses;
    }
    public void setupTable(ObservableList<Course> items){
        tableView.setItems(items);
        degree.setCellValueFactory(new PropertyValueFactory<Course,String>("degree"));
        courseId.setCellValueFactory(new PropertyValueFactory<Course,String>("id"));
        professorName.setCellValueFactory(new PropertyValueFactory<Course,String>("professorName"));
        department.setCellValueFactory(new PropertyValueFactory<Course,String>("departmentId"));
        name.setCellValueFactory(new PropertyValueFactory<Course,String>("name"));
        unit.setCellValueFactory(new PropertyValueFactory<Course,Integer>("unit"));
        days.setCellValueFactory(new PropertyValueFactory<Course, ArrayList<String> >("days"));
        examDate.setCellValueFactory(new PropertyValueFactory<Course, LocalDateTime>("examDate"));
        capacity.setCellValueFactory(new PropertyValueFactory<Course, Integer>("studentNumber"));
        previous.setCellValueFactory(new PropertyValueFactory<Course,String>("previousCourse"));
        now.setCellValueFactory(new PropertyValueFactory<Course,String>("nowCourse"));
    }
    public void homePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }
    private ArrayList<Course> requestCourses() {
        Response response=Main.mainClient.getServerController().getCourses();
        String listString = (String)response.getData("list");
        return JsonCaster.courseArrayListCaster(listString);
    }
    private ArrayList<Department> requestDepartments(){
        Response response= Main.mainClient.getServerController().getDepartments();
        String listString = (String)response.getData("list");
        return JsonCaster.departmentArrayListCaster(listString);
    }
    public void suggest(ActionEvent actionEvent) {
    }

    public void sort(ActionEvent actionEvent) {
    }
}
