package Controllers;

import elements.people.Student;
import elements.request.FreedomRequest;
import elements.request.MinorRequest;
import elements.request.RecommendationRequest;
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
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.net.URL;
import java.util.ArrayList;
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
    private static Logger log = LogManager.getLogger(FreedomRequestPage.class);
    public void HomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    public void requestFreedom(ActionEvent actionEvent) {
        Student student=(Student) (Main.mainClient.getUser());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Freedom apply status");
        alert.setTitle("FreeDom");
        FreedomRequest studentfreedomRequest=null;
        for (FreedomRequest freedomCheck:getFreedoms())
            if (freedomCheck.getStudentId().equals(student.getId()) && freedomCheck.isPending())
                studentfreedomRequest=freedomCheck;
        if (studentfreedomRequest!=null)
            alert.setContentText("You have already requested and it's pending!");
        else{
            //send the request:
            Main.mainClient.getServerController().requestFreedom(student.getId(),student.getDepartmentId());
            alert.setContentText("we sent your request.");

            log.info(student.getId()+" wants to be free!");
            setupTable();
        }
        alert.show();
    }
    public void setupTable(){
        Student student=(Student) (Main.mainClient.getUser());
        ObservableList<FreedomRequest> freedomRequests= FXCollections.observableArrayList();
        for (FreedomRequest freedomRequest:getFreedoms()) {
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
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    private ArrayList<FreedomRequest> getFreedoms() {
        Response response= Main.mainClient.getServerController().getFreedoms();
        String listString = (String)response.getData("list");
        return JsonCaster.freedomArrayListCaster(listString);
    }
}
