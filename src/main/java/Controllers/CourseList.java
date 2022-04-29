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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CourseList implements Initializable {
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
    Button HomePage,filterButton,coursePicker;
    @FXML
    ComboBox<String> departmentBox,dayBox;
    @FXML
    ComboBox<Integer> unitBox;
    @FXML
    public TableColumn<Course, LocalDateTime> examDate;
    @FXML
    TextField coursePickerId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable(getItems());
        departmentBox.getItems().addAll("Chemical Eng", "Computer Eng", "Physics", "Mathematics", "Chemistry");
        dayBox.getItems().addAll("Monday","Tuesday","Wednesday","Thursday","Friday");
        unitBox.getItems().addAll(0,1,2,3,4);
        if (LogicalAgent.getInstance().getUser() instanceof Student){
            coursePicker.setVisible(true);
            coursePickerId.setVisible(true);
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

    public ObservableList<Course> getItems(){
        ObservableList<Course> courses= FXCollections.observableArrayList();
        for (Course course:Course.getCourses()) {
            course.setProfessorName();
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
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    public void filter(ActionEvent actionEvent) {
        ObservableList<Course> courses= FXCollections.observableArrayList();
        for (Course course:Course.getCourses()){
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
        setupTable(courses);
    }

    public void pickCourse(ActionEvent actionEvent) {
        String id=coursePickerId.getText();
        Course course=Course.getCourse(id);
        Student student=((Student)(LogicalAgent.getInstance().getUser()));
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("taking a course????");
        alert.setHeaderText("your status:");
        if (course==null){
            alert.setContentText("you can't even copy a text and you are taking a course??");
        }
        else{
            course.getStudentId().add(student.getId());
            student.addGrade(new Grade(course.getId(),0));
            alert.setContentText("I really like to see your face before the final exam!");
        }
        alert.show();
    }
}
