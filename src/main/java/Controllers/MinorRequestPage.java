package Controllers;

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
import shared.messages.response.Response;
import shared.util.Config;
import shared.util.JsonCaster;
import site.edu.Main;

import java.net.URL;
import java.util.ArrayList;
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
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Department department:requestDepartments())
            departmentBox.getItems().add(department.getName());
        setupTable();
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }
    private void setupTable(){
        Student student=(Student) (Main.mainClient.getUser());
        ObservableList<MinorRequest> minorRequests= FXCollections.observableArrayList();
        for (MinorRequest minorRequest:requestMinors()) {
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
        Student student=(Student) (Main.mainClient.getUser());
        MinorRequest studentMinorRequest = null;
        for (MinorRequest minorRequest:requestMinors()){
            if (minorRequest.getStudentId().equals(student.getId()) && (minorRequest.isPending() || minorRequest.getTotalAccepted()))
                studentMinorRequest=minorRequest;
        }
        Integer minAverage = Config.getConfig().getProperty(Integer.class, "minorMinAverage");
        if (student.getَAverage()<minAverage && student.getَAverage()!=0)
            alert.setContentText("your not eligible! Average<17 :)");
        else if (studentMinorRequest!=null && studentMinorRequest.isPending())
            alert.setContentText("you already have requested or have a minor with a department!");
        else if (departmentBox.getValue()==null)
            alert.setContentText("choose a department");
        else if (departmentBox.getValue().equals(student.getDepartmentId()))
            alert.setContentText("you can't minor with your own department.");
        else{
            //send this request to server
            Main.mainClient.getServerController().createMinor(student.getId(),student.getDepartmentId(),departmentBox.getValue());
            alert.setContentText("minor request has been sent");
            log.info(student.getId()+" requested a minor with: "+departmentBox.getValue());
            setupTable();
        }
        alert.show();
    }

    private ArrayList<Department> requestDepartments(){
        Response response= Main.mainClient.getServerController().getDepartments();
        String listString = (String)response.getData("list");
        return JsonCaster.departmentArrayListCaster(listString);
    }

    private ArrayList<MinorRequest> requestMinors(){
        Response response= Main.mainClient.getServerController().getMinors();
        String listString = (String)response.getData("list");
        return JsonCaster.minorRequestArrayListCaster(listString);
    }
}
