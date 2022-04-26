package Controllers;

import elements.courses.Course;
import elements.people.Professor;
import elements.people.Role;
import elements.people.Student;
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
import logic.LogicalAgent;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfessorList implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable(getItems());
        departmentBox.getItems().addAll("Chemical Eng", "Computer Eng", "Physics", "Mathematics", "Chemistry");
        degreeBox.getItems().addAll("Assistant Professor", "Associate Professor", "full Professor");
        roleBox.getItems().addAll(Role.Professor,Role.EducationalAssistant,Role.HeadDepartment);
    }
    public ObservableList<Professor> getItems(){
        ObservableList<Professor> professors= FXCollections.observableArrayList();
        for (Professor professor:Professor.getProfessors())
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
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    public void filter(ActionEvent actionEvent) {
        ObservableList<Professor> professors= FXCollections.observableArrayList();
        for (Professor professor:Professor.getProfessors()){
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
        setupTable(professors);
    }
}
