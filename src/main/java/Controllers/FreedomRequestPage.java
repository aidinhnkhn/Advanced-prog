package Controllers;

import elements.people.Student;
import elements.request.FreedomRequest;
import elements.request.MinorRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;

import java.net.URL;
import java.util.ResourceBundle;


public class FreedomRequestPage implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    Button homePageButton,freedomRequestButton;
    @FXML
    TableColumn<FreedomRequest, String> idColumn;
    @FXML
    TableColumn<FreedomRequest,String> statusColumn;
    @FXML
    TableView<FreedomRequest> tableView;
    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    public void requestFreedom(ActionEvent actionEvent) {
        Student student=(Student) (LogicalAgent.getInstance().getUser());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Freedom apply status");
        alert.setTitle("FreeDom");
        FreedomRequest studentfreedomRequest=null;
        for (FreedomRequest freedomCheck:FreedomRequest.getFreedomRequests())
            if (freedomCheck.getStudentId().equals(student.getId()) && studentfreedomRequest.isPending())
                studentfreedomRequest=freedomCheck;
        if (studentfreedomRequest!=null)
            alert.setContentText("You have already requested and it's pending!");
        else{
            alert.setContentText("we sent your request.");
            FreedomRequest freedomRequest=new FreedomRequest(student.getId(),student.getDepartmentId());
            setupTable();
        }
        alert.show();
    }
    public void setupTable(){
        Student student=(Student) (LogicalAgent.getInstance().getUser());
        ObservableList<FreedomRequest> freedomRequests= FXCollections.observableArrayList();
        for (FreedomRequest freedomRequest:FreedomRequest.getFreedomRequests()) {
            freedomRequest.setStatusText();
            if (freedomRequest.getStudentId().equals(student.getId())) {
                freedomRequests.add(freedomRequest);
            }
        }
        tableView.setItems(freedomRequests);
        idColumn.setCellValueFactory(new PropertyValueFactory<FreedomRequest,String>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<FreedomRequest,String>("statusText"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
}
