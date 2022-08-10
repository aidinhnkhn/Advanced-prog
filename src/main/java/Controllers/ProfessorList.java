package Controllers;

import elements.courses.Course;
import elements.people.Professor;
import elements.people.Role;
import elements.people.Student;
import elements.university.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfessorList implements Initializable {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    ComboBox<String> departmentBox,degreeBox;
    @FXML
    ComboBox<Role> roleBox;
    @FXML
    CheckBox departmentCheck,degreeCheck,roleCheck;
    @FXML
    public TableColumn<Professor,String> professorId;
    @FXML
    private TableView<Professor> tableView;
    @FXML
    private TableColumn<Professor, String> degree;

    @FXML
    private TableColumn<Professor, String> department;

    @FXML
    private TableColumn<Professor, String> email;

    @FXML
    private TableColumn<Professor, String> name;

    @FXML
    private TableColumn<Professor, String> phoneNumber;

    @FXML
    private TableColumn<Professor, Role> role;
    private static Logger log = LogManager.getLogger(ProfessorList.class);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable(getItems());
        ArrayList<Department> departments = requestDepartments();
        for (Department department1:departments)
            departmentBox.getItems().add(department1.getName());
        degreeBox.getItems().addAll("Assistant Professor", "Associate Professor", "full Professor");
        roleBox.getItems().addAll(Role.Professor,Role.EducationalAssistant,Role.HeadDepartment);
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }
    public ObservableList<Professor> getItems(){
        ObservableList<Professor> professors= FXCollections.observableArrayList();
        ArrayList<Professor> professorArrayList = requestProfessors();
        for (Professor professor:professorArrayList)
            professors.add(professor);
        return professors;
    }
    public void setupTable(ObservableList<Professor> items) {
        tableView.setItems(items);
        role.setCellValueFactory(new PropertyValueFactory<Professor,Role>("role"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Professor,String>("phoneNumber"));
        name.setCellValueFactory(new PropertyValueFactory<Professor,String>("username"));
        email.setCellValueFactory(new PropertyValueFactory<Professor,String>("email"));
        degree.setCellValueFactory(new PropertyValueFactory<Professor,String>("degree"));
        department.setCellValueFactory(new PropertyValueFactory<Professor,String>("departmentId"));
        professorId.setCellValueFactory(new PropertyValueFactory<Professor,String>("id"));
    }
    public void homePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    public void filter(ActionEvent actionEvent) {
        ObservableList<Professor> professors= FXCollections.observableArrayList();
        ArrayList<Professor> professorArrayList = requestProfessors();
        for (Professor professor:professorArrayList){
            boolean canAdd=true;
            if (departmentCheck.isSelected() && departmentBox.getValue()!=null)
                if (!professor.getDepartmentId().equals(departmentBox.getValue()))
                    canAdd=false;
            if (degreeCheck.isSelected() && degreeBox.getValue()!=null)
                if (!professor.getDegree().equals(degreeBox.getValue()))
                    canAdd=false;
            if (roleCheck.isSelected() && roleBox.getValue()!=null)
                if (professor.getRole()!=roleBox.getValue())
                    canAdd=false;
            if (canAdd)
                professors.add(professor);
        }
        log.info(Main.mainClient.getUser().getId()+" changed the filters!");
        setupTable(professors);
    }

    private ArrayList<Professor> requestProfessors() {
        Response response= Main.mainClient.getServerController().getProfessors();
        String listString = (String)response.getData("list");
        return JsonCaster.professorArrayListCaster(listString);
    }

    private ArrayList<Department> requestDepartments() {
        Response response= Main.mainClient.getServerController().getDepartments();
        String listString = (String)response.getData("list");
        return JsonCaster.departmentArrayListCaster(listString);
    }
}
