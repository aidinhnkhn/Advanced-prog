package Controllers;

import elements.chat.pm.Pm;
import elements.courses.Course;
import elements.courses.Grade;
import elements.people.Student;
import elements.university.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;
import site.edu.network.ServerController;

import javax.swing.text.StyledEditorKit;
import java.net.URL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    public TableColumn<Course, String> courseId2;
    @FXML
    public TableColumn<Course, ArrayList<String>> days;

    @FXML
    public TableColumn<Course, String> department;

    @FXML
    public TableColumn<Course, String> professorName;

    @FXML
    public TableView<Course> tableView;
    @FXML
    public TableView<Course> tableView2;
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
    public TableColumn<Course, Boolean> stared;

    @FXML
    public TextField idField;
    @FXML
    public Button pickButton, starButton;
    @FXML
    private AnchorPane anchorPane;


    public void initialize(URL location, ResourceBundle resources) {
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
        sortBox.getItems().addAll("Bachelor", "Masters", "PHD", "exam Date", "alphabetic");
    }

    public void setupTable(ObservableList<Course> items) {
        tableView.setItems(items);
        degree.setCellValueFactory(new PropertyValueFactory<Course, String>("degree"));
        courseId.setCellValueFactory(new PropertyValueFactory<Course, String>("id"));
        professorName.setCellValueFactory(new PropertyValueFactory<Course, String>("professorName"));
        department.setCellValueFactory(new PropertyValueFactory<Course, String>("departmentId"));
        name.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
        unit.setCellValueFactory(new PropertyValueFactory<Course, Integer>("unit"));
        days.setCellValueFactory(new PropertyValueFactory<Course, ArrayList<String>>("days"));
        examDate.setCellValueFactory(new PropertyValueFactory<Course, LocalDateTime>("examDate"));
        capacity.setCellValueFactory(new PropertyValueFactory<Course, Integer>("studentNumber"));
        previous.setCellValueFactory(new PropertyValueFactory<Course, String>("previousCourse"));
        now.setCellValueFactory(new PropertyValueFactory<Course, String>("nowCourse"));
        stared.setCellValueFactory(new PropertyValueFactory<Course, Boolean>("stared"));
    }

    public void homePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    private ArrayList<Course> requestCourses() {
        Response response = Main.mainClient.getServerController().getCourses();
        String listString = (String) response.getData("list");
        return JsonCaster.courseArrayListCaster(listString);
    }

    private ArrayList<Department> requestDepartments() {
        Response response = Main.mainClient.getServerController().getDepartments();
        String listString = (String) response.getData("list");
        return JsonCaster.departmentArrayListCaster(listString);
    }

    public void suggest(ActionEvent actionEvent) {
        ObservableList<Course> suggestedCourses = FXCollections.observableArrayList();
        Student student = (Student) Main.mainClient.getUser();
        for (Course course : requestCourses()) {
            if (!course.getDepartmentId().equals(student.getDepartmentId())) continue;
            if (student.getAnyGrade(course.getId()) == null) {
                if (course.getNowCourse().equals("") && course.getPreviousCourse().equals("")) {
                    suggestedCourses.add(course);
                    if (student.hasFavorite(course.getId()))
                        course.setStared(true);
                    else
                        course.setStared(false);
                    continue;
                }
                if (course.getPreviousCourse().equals("")) {
                    String courseId = course.getNowCourse();
                    if (student.getAnyGrade(courseId) == null) continue;
                    if (student.getAnyGrade(courseId).isPassed()) {
                        suggestedCourses.add(course);
                        if (student.hasFavorite(course.getId()))
                            course.setStared(true);
                        else
                            course.setStared(false);
                    }

                }
                if (course.getNowCourse().equals("")) {
                    String courseId = course.getPreviousCourse();
                    if (student.getAnyGrade(courseId) == null) continue;
                    if (student.getAnyGrade(courseId).isPassed()) {
                        suggestedCourses.add(course);
                        if (student.hasFavorite(course.getId()))
                            course.setStared(true);
                        else
                            course.setStared(false);
                    }
                }
            }
        }
        for (String courseId : student.getFavoriteCourse()) {
            Course course = Main.mainClient.getServerController().getCourseById(courseId);
            if (suggestedCourses.contains(course)) continue;
            course.setStared(true);
            suggestedCourses.add(course);
        }
        setupTable(suggestedCourses);
        setupTable2(FXCollections.observableArrayList());
    }

    public void sort(ActionEvent actionEvent) {
        ObservableList<Course> nowCourses = tableView.getItems();
        ObservableList<Course> courses = FXCollections.observableArrayList();
        if (sortBox.getValue().equals("")) return;
        else if (sortBox.getValue().equals("PHD")) {
            for (Course course : nowCourses)
                if (course.getDegree().equals("PHD"))
                    courses.add(course);
        } else if (sortBox.getValue().equals("Masters")) {
            for (Course course : nowCourses)
                if (course.getDegree().equals("Masters"))
                    courses.add(course);
        } else if (sortBox.getValue().equals("Bachelor")) {
            for (Course course : nowCourses)
                if (course.getDegree().equals("Bachelor"))
                    courses.add(course);
        } else if (sortBox.getValue().equals("exam Date")) {
            courses.addAll(nowCourses);
            courses.sort(Comparator.comparing(Course::getExamDate));
        } else if (sortBox.getValue().equals("alphabetic")) {
            courses.addAll(nowCourses);
            courses.sort(Comparator.comparing(Course::getName));
        }
        setupTable(courses);
        setupTable2(FXCollections.observableArrayList());
    }

    public void showDepartmentCourses(ActionEvent actionEvent) {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        Student student = (Student) Main.mainClient.getUser();
        for (Course course : requestCourses())
            if (course.getDepartmentId().equals(departmentBox.getValue())) {
                courses.add(course);
                if (student.hasFavorite(course.getId()))
                    course.setStared(true);
                else
                    course.setStared(false);
            }
        setupTable(courses);
    }

    public void pick(ActionEvent actionEvent) {
        Course course = tableView.getSelectionModel().getSelectedItem();
        if (course == null) return;
        boolean bo = checkCourse(course,false);
        if (bo)
            Main.mainClient.getServerController().pickCourse(course.getId(), Main.mainClient.getUser().getId());
    }

    public boolean checkCourse(Course course,boolean bo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Course checkCourse = Main.mainClient.getServerController().getCourseById(course.getId());
        Student student = (Student) Main.mainClient.getUser();
        if (checkCourse.getStudentNumber() == 0) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("capacity is full");
            alert.show();
            return false;
        }
        if (course.getDepartmentId().equals("Maaref")) {
            for (Grade grade : student.getGrades()) {
                if (grade.isFinalGrade()) continue;
                if (Main.mainClient.getServerController().getCourseById(grade.getCourseId()).getDepartmentId().equals("Maaref")) {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setContentText("only one Maaref course per term");
                    alert.show();
                    return false;
                }
            }
        }
        if (!course.getPreviousCourse().equals("")) {
            String need = course.getPreviousCourse();
            boolean check = false;
            for (Grade grade : student.getGrades())
                if (grade.isFinalGrade() && grade.isPassed() && grade.getCourseId().equals(need))
                    check = true;
            if (!check) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("pick & pass previous courses first");
                alert.show();
                return false;
            }
        }
        if (!course.getNowCourse().equals("")){
            String need = course.getNowCourse();
            boolean check = false;
            for (Grade grade : student.getGrades()) {
                if (grade.getCourseId().equals(need))
                    check = true;
            }
            if (!check) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("pick now courses first");
                alert.show();
                return false;
            }
        }
        //class and exam interference:
        for (Grade grade:student.getGrades()){
            if (grade.isFinalGrade()) continue;
            Course alakiCourse = Main.mainClient.getServerController().getCourseById(grade.getCourseId());
            LocalDateTime examDate = alakiCourse.getExamDate();
            ArrayList<String> days = alakiCourse.getDays();
            int hour = alakiCourse.getHour();
            int length = alakiCourse.getLength();
            if (alakiCourse.getName().equals(course.getName()) && !alakiCourse.getId().equals(course.getId()) && bo)
                continue;
            if (examDate.equals(course.getExamDate())){
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("exam dates interfere");
                alert.show();
                return false;
            }
            for (String day: days){
                if (course.getDays().contains(day)){
                    boolean check = true;
                    if (course.getHour()>=hour && course.getHour()<hour+length)
                        check = false;
                    if (hour>=course.getHour() && hour<course.getHour()+course.getLength())
                        check = false;
                    if (!check){
                        alert.setAlertType(Alert.AlertType.WARNING);
                        alert.setContentText("class time interfere");
                        alert.show();
                        return false;
                    }
                }
            }
        }
        alert.setContentText("successful");
        alert.show();
        return true;
    }

    public void star(ActionEvent actionEvent) {
        Course course = tableView.getSelectionModel().getSelectedItem();
        if (course == null) return;
        Student student = (Student) Main.mainClient.getUser();
        if (!student.hasFavorite(course.getId())) {
            course.setStared(true);
            Main.mainClient.getServerController().starCourse(student.getId(), course.getId());
        } else {
            course.setStared(false);
            Main.mainClient.getServerController().unStarCourse(student.getId(), course.getId());
        }
        setupTable(tableView.getItems());
    }

    public void showCourse(MouseEvent event) {
        Course course = tableView.getSelectionModel().getSelectedItem();
        setupTable2(FXCollections.observableArrayList());
        if (course == null) {
            idField.setText("");
            return;
        }
        idField.setText(course.getId());
    }

    public void request(ActionEvent actionEvent) {
        Course course = tableView.getSelectionModel().getSelectedItem();
        if (course == null) return;
        boolean bo = checkCourse(course,false);
        if (bo)
            Main.mainClient.getServerController().pickCourseRequest(Main.mainClient.getUser().getId(),course.getId());
    }

    public void delete(ActionEvent actionEvent) {
        Student student = (Student) Main.mainClient.getUser();
        Course course = tableView.getSelectionModel().getSelectedItem();
        if (course == null) return;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        Grade grade=student.getGrade(course.getId());
        if (grade==null)
            alert.setContentText("you don't have this course");

        else{
            Main.mainClient.getServerController().deleteGrade(student.getId(),course.getId());
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("successful");
        }
        alert.show();
    }

    public void change(ActionEvent actionEvent) {
        Student student = (Student) Main.mainClient.getUser();
        Course course = tableView2.getSelectionModel().getSelectedItem();
        Course mainCourse = tableView.getSelectionModel().getSelectedItem();
        if (course == null) return;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        Grade grade=student.getGrade(course.getId());
        if (grade==null)
            alert.setContentText("you don't have this course");
        boolean bo = checkCourse(course,true);
        if (bo) {
            Main.mainClient.getServerController().changeGroup(student.getId(), course.getId(), mainCourse.getId());
            setupTable2(FXCollections.observableArrayList());
        }
    }

    public void showGroups(ActionEvent actionEvent) {
        Course course2 = tableView.getSelectionModel().getSelectedItem();
        if (course2 == null) return;
        ObservableList<Course> courses = FXCollections.observableArrayList();
        for (Course course:requestCourses())
            if (course.getName().equals(course2.getName()))
                courses.add(course);
        setupTable2(courses);
    }

    private void setupTable2(ObservableList<Course> courses) {
        tableView2.setItems(courses);
        courseId2.setCellValueFactory(new PropertyValueFactory<Course, String>("id"));
    }
}
