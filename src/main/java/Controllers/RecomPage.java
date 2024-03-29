package Controllers;

import elements.people.Professor;
import elements.people.Student;
import elements.request.RecommendationRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.LogicalAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class RecomPage implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    Button homePageButton,requestButton,downloadButton;
    @FXML
    TextField professorId,requestId;
    @FXML
    TableColumn<RecommendationRequest, String> nameColumn;
    @FXML
    TableColumn<RecommendationRequest,String> statusColumn;
    @FXML
    TableColumn<RecommendationRequest,String> idColumn;
    @FXML
    TableView<RecommendationRequest> tableView;

    private static Logger log = LogManager.getLogger(RecomPage.class);
    public void HomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    public void sendRequest(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Recommendation apply status");
        alert.setTitle("Recommendation");
        Student student=(Student) (Main.mainClient.getUser());
        RecommendationRequest studentRecommendation=null;
        for (RecommendationRequest recommendationRequest:getRecommendations())
            if (recommendationRequest.getStudentId().equals(student.getId()) && recommendationRequest.isPending())
                studentRecommendation=recommendationRequest;
        if (isProfessorIdInCorrect(professorId.getText()))
            alert.setContentText("professor Id is incorrect");
        else if (studentRecommendation!=null)
            alert.setContentText("you have already requested a Recommendation");
        else{
            //send this to server
            Main.mainClient.getServerController().requestRecommendation(student.getId(),professorId.getText());
            alert.setContentText("we sent your request");
            log.info(student.getId()+" requested a Recommendation to professor: "+professorId.getText());
            setupTable();
        }
        alert.show();
    }
    private void setupTable(){
        Student student=(Student) (Main.mainClient.getUser());
        ObservableList<RecommendationRequest> recommendationRequests= FXCollections.observableArrayList();
        for (RecommendationRequest recommendationRequest:getRecommendations()) {
            recommendationRequest.setStatusText();
            if (recommendationRequest.getStudentId().equals(student.getId())) {
                recommendationRequests.add(recommendationRequest);
            }
        }
        tableView.setItems(recommendationRequests);
        nameColumn.setCellValueFactory(new PropertyValueFactory<RecommendationRequest,String>("professorName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<RecommendationRequest,String>("statusText"));
        idColumn.setCellValueFactory(new PropertyValueFactory<RecommendationRequest,String>("id"));
    }
    private boolean isProfessorIdInCorrect(String id){
        return Main.mainClient.getServerController().getProfessor(id)==null;
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

    public void download(ActionEvent actionEvent) {
        Student student=(Student) (Main.mainClient.getUser());
       for (RecommendationRequest recommendationRequest:getRecommendations()){
           if (recommendationRequest.getStudentId().equals(student.getId()) && recommendationRequest.getTotalAccepted()){
               String sampleText = recommendationRequest.getAcceptedText();
               Text sample = new Text(sampleText);
               sample.setFont(new Font(14));
               FileChooser fileChooser = new FileChooser();

               //Set extension filter for text files
               FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
               fileChooser.getExtensionFilters().add(extFilter);

               //Show save file dialog
               Stage stage = (Stage) (((Node) (actionEvent.getSource())).getScene().getWindow());
               File file = fileChooser.showSaveDialog(stage);

               if (file != null) {
                   saveTextToFile(sampleText, file);
               }
           }
       }
    }
    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            log.error("couldn't save the file"+file.getName());
        }
    }

    private ArrayList<RecommendationRequest> getRecommendations() {
        Response response= Main.mainClient.getServerController().getRecommendations();
        String listString = (String)response.getData("list");
        return JsonCaster.RecommendationArrayListCaster(listString);
    }
}
