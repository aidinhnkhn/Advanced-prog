package Controllers;

import elements.people.Professor;
import elements.people.Role;
import elements.people.Student;
import elements.request.FreedomRequest;
import elements.request.MinorRequest;
import elements.request.RecommendationRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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

public class ProfessorRequestPage implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    private int minorId = 0, recommendationId = 0, freedomId = 0;
    private boolean isMinor = false, isRecommendation = false, isFreedom = false;
    @FXML
    Button homePageButton;

    @FXML
    Button nextButton, previousButton,acceptButton,rejectButton;
    @FXML
    TextArea textArea;
    @FXML
    ComboBox<String> requestBox;

    private MinorRequest selectedMinor;
    private FreedomRequest selectedFreedom;
    private RecommendationRequest selectedRecommendation;
    private static Logger log = LogManager.getLogger(ProfessorRequestPage.class);
    public void HomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requestBox.getItems().addAll("Recommendations");
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (professor.getRole() == Role.EducationalAssistant)
            requestBox.getItems().addAll("Minors", "Freedoms");
        minorId = 0;
        recommendationId = 0;
        freedomId = 0;
        selectedRecommendation = null;
        selectedMinor=null;
        selectedFreedom=null;
        if (Main.mainClient.getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
    }

    public void show(ActionEvent actionEvent) {
        if (requestBox.getValue()==null)
            return;
        if (requestBox.getValue().equals("Minors")) {
            isMinor = true;
            isFreedom = false;
            isRecommendation = false;
            goNext(new ActionEvent());
        } else if (requestBox.getValue().equals("Recommendations")) {
            isRecommendation = true;
            isFreedom = false;
            isMinor = false;
            goNext(new ActionEvent());
        } else if (requestBox.getValue().equals("Freedoms")) {
            isMinor = false;
            isFreedom = true;
            isRecommendation = false;
            goNext(new ActionEvent());
        }
    }
    private ArrayList<MinorRequest> requestMinors(){
        Response response= Main.mainClient.getServerController().getMinors();
        String listString = (String)response.getData("list");
        return JsonCaster.minorRequestArrayListCaster(listString);
    }
    private void getNextMinorId() {
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (minorId < 0) minorId = 0;
        ArrayList<MinorRequest> minorRequests = requestMinors();
        while (minorId < minorRequests.size()) {
            MinorRequest minorRequest = minorRequests.get(minorId);
            if (minorRequest.isPending()) {
                if (professor.getDepartmentId().equals(minorRequest.getDepartmentId())) {
                    selectedMinor = minorRequests.get(minorId);
                    break;
                }
                if (professor.getDepartmentId().equals(minorRequest.getSecondDepartmentId())) {
                    selectedMinor = minorRequests.get(minorId);
                    break;
                }
            }
            minorId++;
        }

    }

    private void getPreviousMinorId() {
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (minorId >= MinorRequest.getMinorRequests().size()) minorId = MinorRequest.getMinorRequests().size() - 1;
        ArrayList<MinorRequest> minorRequests = requestMinors();
        while (minorId >= 0) {
            MinorRequest minorRequest = minorRequests.get(minorId);
            if (minorRequest.isPending()) {
                if (professor.getDepartmentId().equals(minorRequest.getDepartmentId())) {
                    selectedMinor = minorRequest;
                    break;
                }
                if (professor.getDepartmentId().equals(minorRequest.getSecondDepartmentId())) {
                    selectedMinor = minorRequest;
                    break;
                }
            }
            minorId--;
        }
    }
    private ArrayList<FreedomRequest> getFreedoms() {
        Response response= Main.mainClient.getServerController().getFreedoms();
        String listString = (String)response.getData("list");
        return JsonCaster.freedomArrayListCaster(listString);
    }
    private void getNextFreedomId() {
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (freedomId < 0) freedomId = 0;
        ArrayList<FreedomRequest> freedomRequests = getFreedoms();
        while (freedomId < freedomRequests.size()) {
            FreedomRequest freedomRequest = freedomRequests.get(freedomId);
            if (freedomRequest.isPending())
                if (professor.getDepartmentId().equals(freedomRequest.getDepartmentId())) {
                    selectedFreedom = freedomRequest;
                    break;
                }
            freedomId++;
        }
    }

    private void getPreviousFreedomId() {
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (freedomId >= FreedomRequest.getFreedomRequests().size())
            freedomId = FreedomRequest.getFreedomRequests().size() - 1;
        ArrayList<FreedomRequest> freedomRequests = getFreedoms();
        while (freedomId >= 0) {
            FreedomRequest freedomRequest = freedomRequests.get(freedomId);
            if (freedomRequest.isPending())
                if (professor.getDepartmentId().equals(freedomRequest.getDepartmentId())){
                    selectedFreedom = freedomRequest;
                    break;
                }
            freedomId--;
        }
    }
    private ArrayList<RecommendationRequest> getRecommendations() {
        Response response= Main.mainClient.getServerController().getRecommendations();
        String listString = (String)response.getData("list");
        return JsonCaster.RecommendationArrayListCaster(listString);
    }
    private void getNextRecommendationId() {
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (recommendationId < 0) recommendationId = 0;
        ArrayList<RecommendationRequest> recommendations = getRecommendations();
        while (recommendationId < recommendations.size()) {
            RecommendationRequest recommendationRequest = recommendations.get(recommendationId);
            if (recommendationRequest.isPending())
                if (professor.getId().equals(recommendationRequest.getDepartmentId())) {
                    selectedRecommendation = recommendationRequest;
                    break;
                }
            recommendationId++;
        }
    }

    private void getPreviousRecommendationId() {
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (recommendationId >= RecommendationRequest.getRecommendationRequests().size())
            recommendationId = RecommendationRequest.getRecommendationRequests().size() - 1;
        ArrayList<RecommendationRequest> recommendations = getRecommendations();
        while (recommendationId >= 0) {
            RecommendationRequest recommendationRequest = recommendations.get(recommendationId);
            if (recommendationRequest.isPending())
                if (professor.getId().equals(recommendationRequest.getDepartmentId())){
                    selectedRecommendation = recommendationRequest;
                    break;
                }
            recommendationId--;
        }
    }

    private String minorString(MinorRequest minorRequest){
        StringBuilder requestMessage = new StringBuilder();
        requestMessage.append("student Id: " + minorRequest.getStudentId() + "\n");
        requestMessage.append("first department: " + minorRequest.getDepartmentId() + "\n");
        requestMessage.append("second department: " + minorRequest.getSecondDepartmentId() + "\n");
        requestMessage.append(minorRequest.getRequestText());
        return requestMessage.toString();
    }

    private String RecommendationString (RecommendationRequest recommendationRequest){
        StringBuilder requestMessage = new StringBuilder();
        requestMessage.append("student Id: " + recommendationRequest.getStudentId() + "\n");
        requestMessage.append(recommendationRequest.getRequestText() + "\n");
        requestMessage.append("your Recommendation text is:\n" + recommendationRequest.getAcceptedText());
        return requestMessage.toString();
    }

    private String freedomString(FreedomRequest freedomRequest){
        StringBuilder requestMessage = new StringBuilder();
        requestMessage.append("student Id: " + freedomRequest.getStudentId() + "\n");
        requestMessage.append(freedomRequest.getRequestText());
        return requestMessage.toString();
    }
    private void setRequestString() {
        StringBuilder requestMessage = new StringBuilder();
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (isMinor) {
            if (selectedMinor == null)
                return;
            requestMessage.append(minorString(selectedMinor));
        }
        if (isRecommendation) {
            if (selectedRecommendation == null)
                return;
            requestMessage.append(RecommendationString(selectedRecommendation));
        }
        if (isFreedom) {
            if (selectedFreedom == null)
                return;
            requestMessage.append(freedomString(selectedFreedom));
        }
        textArea.setText(requestMessage.toString());
    }

    public void goNext(ActionEvent actionEvent) {
        if (isMinor) {
            getNextMinorId();
            setRequestString();
        }
        if (isFreedom) {
            getNextFreedomId();
            setRequestString();
        }
        if (isRecommendation) {
            getNextRecommendationId();
            setRequestString();
        }
    }

    public void goBack(ActionEvent actionEvent) {
        if (isMinor) {
            getPreviousMinorId();
            setRequestString();
        }
        if (isFreedom) {
            getPreviousFreedomId();
            setRequestString();
        }
        if (isRecommendation) {
            getPreviousRecommendationId();
            setRequestString();
        }
    }

    public void accept(ActionEvent actionEvent) {
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (isMinor) {
            if (selectedMinor == null)
                return;
            //send the accept minor request to the server:
            if (selectedMinor.isPending()) {
                if (professor.getDepartmentId().equals(selectedMinor.getDepartmentId()))
                    Main.mainClient.getServerController().acceptMinor1st(selectedMinor.getId(),true);

                if (professor.getDepartmentId().equals(selectedMinor.getSecondDepartmentId()))
                    Main.mainClient.getServerController().acceptMinor2nd(selectedMinor.getId(),true);

                log.info(professor.getId()+" accepted a minor request. "+ selectedMinor.getId());
            }
        }
        if (isRecommendation) {
            if (selectedRecommendation == null)
                return;

            if (selectedRecommendation.isPending())
                if (professor.getId().equals(selectedRecommendation.getDepartmentId())){
                    Main.mainClient.getServerController().acceptRecommendation(selectedRecommendation.getId(),true);
                    //recommendationRequest.setAccepted(true);
                    log.info(professor.getId()+" accepted a Recommendation request. "+selectedRecommendation.getId());
                }
        }
        if (isFreedom) {
            if (selectedFreedom == null)
                return;
            if (selectedFreedom.isPending())
                if (professor.getDepartmentId().equals(selectedFreedom.getDepartmentId())) {
                    Main.mainClient.getServerController().acceptFreedom(selectedFreedom.getId(),false);

                    log.info(professor.getId()+" accepted a freedom request. "+selectedFreedom.getId());
                }
        }
    }

    public void reject(ActionEvent actionEvent) {
        Professor professor = (Professor) (Main.mainClient.getUser());
        if (isMinor) {
            if (selectedMinor == null)
                return;

            if (selectedMinor.isPending()) {
                if (professor.getDepartmentId().equals(selectedMinor.getDepartmentId()))
                    Main.mainClient.getServerController().acceptMinor1st(selectedMinor.getId(),false);

                if (professor.getDepartmentId().equals(selectedMinor.getSecondDepartmentId()))
                    Main.mainClient.getServerController().acceptMinor2nd(selectedMinor.getId(),false);

                log.info(professor.getId()+" accepted a minor request. "+ selectedMinor.getId());
            }
        }
        if (isRecommendation) {
            if (selectedRecommendation == null)
                return;

            if (selectedRecommendation.isPending())
                if (professor.getId().equals(selectedRecommendation.getDepartmentId())){
                    Main.mainClient.getServerController().acceptRecommendation(selectedRecommendation.getId(),false);

                    log.info(professor.getId()+" rejected a Recommendation request. "+selectedRecommendation.getId());
                }
        }
        if (isFreedom) {
            if (selectedFreedom == null)
                return;
            if (selectedFreedom.isPending())
                if (professor.getDepartmentId().equals(selectedFreedom.getDepartmentId())) {
                    Main.mainClient.getServerController().acceptFreedom(selectedFreedom.getId(),false);
                    //freedomRequest.setAccepted(false);
                    log.info(professor.getId()+" rejected a freedom request. "+selectedFreedom.getId());
                }
        }
    }
}
