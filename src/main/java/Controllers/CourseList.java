package Controllers;

import elements.courses.Course;
import elements.courses.Grade;
import elements.people.Professor;
import elements.people.Student;
import elements.university.Department;
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
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CourseList implements Initializable {
    private static Logger log = LogManager.getLogger(CourseList.class);
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
    public CheckBox dayCheck,departmentCheck,unitCheck;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    Button HomePage,filterButton;
    @FXML
    ComboBox<String> departmentBox,dayBox;
    @FXML
    ComboBox<Integer> unitBox;
    @FXML
    public TableColumn<Course, LocalDateTime> examDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable(getItems());


        for (Department department1:requestDepartments())
            departmentBox.getItems().add(department1.getName());

        dayBox.getItems().addAll("Monday","Tuesday","Wednesday","Thursday","Friday");
        unitBox.getItems().addAll(0,1,2,3,4);
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");

    }

    public ObservableList<Course> getItems(){
        ObservableList<Course> courses= FXCollections.observableArrayList();
        ArrayList<Course> availableCourses  = requestCourses();
        for (Course course:availableCourses) {
            courses.add(course);
        }
        return courses;
    }

    public void setupTable(ObservableList<Course> items){
        tableView.setItems(items);
        courseId.setCellValueFactory(new PropertyValueFactory<Course,String>("id"));
        professorName.setCellValueFactory(new PropertyValueFactory<Course,String>("professorName"));
        department.setCellValueFactory(new PropertyValueFactory<Course,String>("departmentId"));
        name.setCellValueFactory(new PropertyValueFactory<Course,String>("name"));
        unit.setCellValueFactory(new PropertyValueFactory<Course,Integer>("unit"));
        days.setCellValueFactory(new PropertyValueFactory<Course, ArrayList<String> >("days"));
        degree.setCellValueFactory(new PropertyValueFactory<Course,String>("degree"));
        examDate.setCellValueFactory(new PropertyValueFactory<Course, LocalDateTime>("examDate"));
    }

    public void homePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    public void filter(ActionEvent actionEvent) {
        ObservableList<Course> courses= FXCollections.observableArrayList();
        ArrayList<Course> availableCourses  = requestCourses();
        for (Course course:availableCourses){
            boolean canAdd=true;
            if (dayCheck.isSelected() && dayBox.getValue()!=null)
                    if (!course.getDays().contains(dayBox.getValue()))
                        canAdd=false;
            if (departmentCheck.isSelected() && departmentBox.getValue()!=null)
                if (!course.getDepartmentId().equals(departmentBox.getValue()))
                    canAdd=false;
            if (unitCheck.isSelected() && unitBox.getValue()!=null)
                if (course.getUnit()!=unitBox.getValue())
                    canAdd=false;
            if (canAdd) courses.add(course);
        }
        log.info("course list was filtered!");
        setupTable(courses);
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
}
