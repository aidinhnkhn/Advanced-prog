package Controllers;


import elements.chat.Chat;
import elements.courses.Course;
import elements.people.Student;
import elements.request.PickCourseRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EnrollHour implements Initializable {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button homePageButton;
    @FXML
    public Button dateSetterButton;

    @FXML
    public ComboBox<String> universityDateBox;

    @FXML
    public DatePicker universityDate;

    @FXML
    public TextField hourField;
    
    @FXML
    public Button studentDateButton;
    @FXML
    public TextField studentHour;
    @FXML
    public DatePicker studentDate;
    @FXML
    public ComboBox<Student> studentBox;
    @FXML
    public CheckBox yearCheck;

    @FXML
    public TextField enterYear;
    @FXML
    public CheckBox degreeCheck;
    @FXML
    public ComboBox<String> degree;

    @FXML
    public Button filterButton;

    private static Logger log = LogManager.getLogger(EnrollHour.class);
    @FXML
    public CheckBox register;
    @FXML
    public TableView<PickCourseRequest> tableView;
    @FXML
    public TableColumn<PickCourseRequest,String> courseIdColumn;
    @FXML
    public TableColumn<PickCourseRequest,String> studentIdColumn;

    PickCourseRequest pickedRequest;
    boolean running;
    public void goHomePage(ActionEvent actionEvent) {
        running = false;
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       running = true;
        new Thread(() -> {

            while (running) {
                setupTable();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.debug("chat page thread stopped!");
                }
            }
        }).start();
        //setupTable();
        setupUniversityBox();
        setupStudentBox();
        pickedRequest = null;
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    private void setupStudentBox() {
        studentBox.getItems().clear();
        for (Student student : requestStudents()){
            boolean canAdd = true;
            if (degreeCheck.isSelected() && !degree.getValue().equals(""))
                if (!degree.getValue().equals(student.getDegree()))
                    canAdd=false;
            if (yearCheck.isSelected() && enterYear.getText().matches("[0-9]*"))
                if (!student.getEnterYear().equals(enterYear.getText()))
                    canAdd=false;
            if (canAdd)
                studentBox.getItems().add(student);
        }
    }

    private void setupUniversityBox() {
        universityDateBox.getItems().addAll("starting date","ending date");
        degree.getItems().addAll("Bachelor", "Masters", "PHD");
    }

    public void setUniversityDate(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("status");
        if (hourField.getText().equals(""))
            alert.setContentText("enter an hour");
        else if (universityDate.getValue() == null)
            alert.setContentText("select a date");
        else if (!hourField.getText().matches("[0-9]*"))
            alert.setContentText("enter only numbers");
        else if (Integer.parseInt(hourField.getText())<0 || Integer.parseInt(hourField.getText())>23)
            alert.setContentText("numbers should be between 0-23");
        else if (universityDateBox.getValue().equals(""))
            alert.setContentText("pick what you're choosing");
        else {
            int hour = Integer.parseInt(hourField.getText());
            LocalDateTime date = universityDate.getValue().atTime(hour,0,0);
            alert.setContentText("date submitted");
            if (universityDateBox.getValue().equals("starting date"))
                Main.mainClient.getServerController().sendStartingDate(date);
            else
                Main.mainClient.getServerController().sendEndingDate(date);
        }
        alert.show();
    }

    public void setStudentDate(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("status");
        if (studentBox.getValue() != null && !register.isSelected()) {
            Main.mainClient.getServerController().sendNotRegister(studentBox.getValue().getId());
            alert.setContentText("no register for him\\her");
        }
        else if (studentHour.getText().equals(""))
            alert.setContentText("enter an hour");
        else if (studentDate.getValue() == null)
            alert.setContentText("select a date");
        else if (!studentHour.getText().matches("[0-9]*"))
            alert.setContentText("enter only numbers");
        else if (Integer.parseInt(studentHour.getText())<0 || Integer.parseInt(studentHour.getText())>23)
            alert.setContentText("numbers should be between 0-23");
        else if (studentDate.getValue().equals(""))
            alert.setContentText("pick what you're choosing");
        else if (studentBox.getValue() == null)
            alert.setContentText("pick a student");
        else {
            int hour = Integer.parseInt(studentHour.getText());
            LocalDateTime date = studentDate.getValue().atTime(hour,0,0);
            Main.mainClient.getServerController().sendStudentDate(studentBox.getValue().getId(),date);
            alert.setContentText("date submitted");

        }
        alert.show();
    }

    private ArrayList<Student> requestStudents(){
        Response response=Main.mainClient.getServerController().getStudents();
        String listString = (String)response.getData("list");
        return JsonCaster.studentArrayListCaster(listString);
    }

    public void filter(ActionEvent actionEvent) {
        setupStudentBox();
        log.info("students were filtered");
    }

    public void setupTable(){
        Platform.runLater(()->{
            ObservableList<PickCourseRequest> requests = FXCollections.observableArrayList();
            for (PickCourseRequest pickCourseRequest:getPickCourses()){
                Student student = Main.mainClient.getServerController().getStudentById(pickCourseRequest.getStudentId());
                if (student.getDepartmentId().equals(Main.mainClient.getUser().getDepartmentId()) && pickCourseRequest.isPending())
                    requests.add(pickCourseRequest);
            }
            tableView.setItems(requests);
            studentIdColumn.setCellValueFactory(new PropertyValueFactory<PickCourseRequest, String>("studentId"));
            courseIdColumn.setCellValueFactory(new PropertyValueFactory<PickCourseRequest, String>("departmentId"));
        });
    }

    private ArrayList<PickCourseRequest> getPickCourses() {
        Response response = Main.mainClient.getServerController().getPickCourses();
        String listString = (String) response.getData("list");
        return JsonCaster.pickCourseArrayListCaster(listString);
    }
    public void accept(ActionEvent actionEvent) {
        if (pickedRequest == null) return;
        Main.mainClient.getServerController().acceptPickCourse(pickedRequest.getId());
    }

    public void reject(ActionEvent actionEvent) {
        if (pickedRequest == null) return;
        Main.mainClient.getServerController().rejectPickCourse(pickedRequest.getId());
    }

    public void setRequest(MouseEvent event) {
        pickedRequest = tableView.getSelectionModel().getSelectedItem();
    }
}
