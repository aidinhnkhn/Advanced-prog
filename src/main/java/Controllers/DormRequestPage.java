package Controllers;

import elements.people.Student;
import elements.request.DormRequest;
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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class DormRequestPage implements Initializable {
    private static Logger log = LogManager.getLogger(DormRequestPage.class);
    @FXML
    public AnchorPane anchorPane;
    @FXML
    TableColumn<DormRequest, String> idColumn;
    @FXML
    TableColumn<DormRequest,String> statusColumn;
    @FXML
    TableView<DormRequest> tableView;
    @FXML
    Button homePageButton,dormButton;
    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    public void requestDorm(ActionEvent actionEvent) {
        Student student=(Student) (LogicalAgent.getInstance().getUser());
        boolean check=false;
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Dorm request status");
        alert.setTitle("Dorm");
        for (DormRequest dormRequest:DormRequest.getDormRequests()){
            if (dormRequest.getTotalAccepted()) check=true;
        }
        if (check)
            alert.setContentText("you already have a dorm");
        else{
            DormRequest dormRequest=new DormRequest(student.getId(),student.getDepartmentId());
            alert.setContentText("you applied!");
            setupTable();
            log.info(student.getId()+ " requested to have dorm!");
            log.info(student.getId()+ "dorm request was accepted status: "+dormRequest.getTotalAccepted());
        }
        alert.show();
    }
    public void setupTable(){
        Student student=(Student) (LogicalAgent.getInstance().getUser());
        ObservableList<DormRequest> dormRequests= FXCollections.observableArrayList();
        for (DormRequest dormRequest:DormRequest.getDormRequests()) {
            dormRequest.setStatusText();
            if ( dormRequest.getStudentId().equals(student.getId())) {
                dormRequests.add( dormRequest);
            }
        }
        tableView.setItems(dormRequests);
        idColumn.setCellValueFactory(new PropertyValueFactory<DormRequest,String>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<DormRequest,String>("statusText"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
