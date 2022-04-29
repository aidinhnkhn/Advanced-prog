package Controllers;

import Savers.Saver;
import elements.people.Student;
import elements.request.MinorRequest;
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

import java.net.URL;
import java.util.ResourceBundle;

public class MinorRequestPage implements Initializable {
    public AnchorPane anchorPane;
    @FXML
    Button homePageButton,applyButton;
    @FXML
    ComboBox<String> departmentBox;
    @FXML
     TableColumn<MinorRequest, String> departmentColumn;
    @FXML
    TableColumn<MinorRequest,String> statusColumn;
    @FXML
    TableView<MinorRequest> tableView;
    private static Logger log = LogManager.getLogger(MinorRequestPage.class);
    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Department department:Department.getDepartments())
            departmentBox.getItems().add(department.getName());
        setupTable();
        if (LogicalAgent.getInstance().getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }
    private void setupTable(){
        Student student=(Student) (LogicalAgent.getInstance().getUser());
        ObservableList<MinorRequest> minorRequests= FXCollections.observableArrayList();
        for (MinorRequest minorRequest:MinorRequest.getMinorRequests()) {
            minorRequest.setStatusText();
            if (minorRequest.getStudentId().equals(student.getId())) {
                minorRequests.add(minorRequest);
            }
        }
        tableView.setItems(minorRequests);
        departmentColumn.setCellValueFactory(new PropertyValueFactory<MinorRequest,String>("secondDepartmentId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<MinorRequest,String>("statusText"));
    }
    public void apply(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Minor apply status");
        alert.setTitle("Minor");
        Student student=(Student) (LogicalAgent.getInstance().getUser());
        MinorRequest studentMinorRequest = null;
        for (MinorRequest minorRequest:MinorRequest.getMinorRequests()){
            if (minorRequest.getStudentId().equals(student.getId()) && (minorRequest.isPending() || minorRequest.getTotalAccepted()))
                studentMinorRequest=minorRequest;
        }
        if (student.getَAverage()<17 && student.getَAverage()!=0)
            alert.setContentText("your not eligible! Average<17 :)");
        else if (studentMinorRequest!=null && studentMinorRequest.isPending())
            alert.setContentText("you already have requested or have a minor with a department!");
        else if (departmentBox.getValue()==null)
            alert.setContentText("choose a department");
        else if (departmentBox.getValue().equals(student.getDepartmentId()))
            alert.setContentText("you can minor with your own department.");
        else{
            MinorRequest minorRequest=new MinorRequest(student.getId(),student.getDepartmentId(),departmentBox.getValue());
//            Saver.getInstance().saveMinorRequest(minorRequest);
            alert.setContentText("minor request has been sent");
            log.info(student.getId()+" requested a minor with: "+departmentBox.getValue());
            setupTable();
        }
        alert.show();
    }
}
