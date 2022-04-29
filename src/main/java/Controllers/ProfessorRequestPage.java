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

import java.net.URL;
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
    private static Logger log = LogManager.getLogger(ProfessorRequestPage.class);
    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requestBox.getItems().addAll("Recommendations");
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (professor.getRole() == Role.EducationalAssistant)
            requestBox.getItems().addAll("Minors", "Freedoms");
        minorId = 0;
        recommendationId = 0;
        freedomId = 0;
        if (LogicalAgent.getInstance().getUser().isTheme()) {
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

    private void getNextMinorId() {
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (minorId < 0) minorId = 0;
        while (minorId < MinorRequest.getMinorRequests().size()) {
            MinorRequest minorRequest = MinorRequest.getMinorRequests().get(minorId);
            if (minorRequest.isPending()) {
                if (professor.getDepartmentId().equals(minorRequest.getDepartmentId()))
                    break;
                if (professor.getDepartmentId().equals(minorRequest.getSecondDepartmentId()))
                    break;
            }
            minorId++;
        }
    }

    private void getPreviousMinorId() {
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (minorId >= MinorRequest.getMinorRequests().size()) minorId = MinorRequest.getMinorRequests().size() - 1;
        while (minorId >= 0) {
            MinorRequest minorRequest = MinorRequest.getMinorRequests().get(minorId);
            if (minorRequest.isPending()) {
                if (professor.getDepartmentId().equals(minorRequest.getDepartmentId()))
                    break;
                if (professor.getDepartmentId().equals(minorRequest.getSecondDepartmentId()))
                    break;
            }
            minorId--;
        }
    }

    private void getNextFreedomId() {
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (freedomId < 0) freedomId = 0;
        while (freedomId < FreedomRequest.getFreedomRequests().size()) {
            FreedomRequest freedomRequest = FreedomRequest.getFreedomRequests().get(freedomId);
            if (freedomRequest.isPending())
                if (professor.getDepartmentId().equals(freedomRequest.getDepartmentId()))
                    break;
            freedomId++;
        }
    }

    private void getPreviousFreedomId() {
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (freedomId >= FreedomRequest.getFreedomRequests().size())
            freedomId = FreedomRequest.getFreedomRequests().size() - 1;
        while (freedomId >= 0) {
            FreedomRequest freedomRequest = FreedomRequest.getFreedomRequests().get(freedomId);
            if (freedomRequest.isPending())
                if (professor.getDepartmentId().equals(freedomRequest.getDepartmentId()))
                    break;
            freedomId--;
        }
    }

    private void getNextRecommendationId() {
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (recommendationId < 0) recommendationId = 0;
        while (recommendationId < RecommendationRequest.getRecommendationRequests().size()) {
            RecommendationRequest recommendationRequest = RecommendationRequest.getRecommendationRequests().get(recommendationId);
            if (recommendationRequest.isPending())
                if (professor.getId().equals(recommendationRequest.getDepartmentId()))
                    break;
            recommendationId++;
        }
    }

    private void getPreviousRecommendationId() {
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (recommendationId >= RecommendationRequest.getRecommendationRequests().size())
            recommendationId = RecommendationRequest.getRecommendationRequests().size() - 1;
        while (recommendationId >= 0) {
            RecommendationRequest recommendationRequest = RecommendationRequest.getRecommendationRequests().get(recommendationId);
            if (recommendationRequest.isPending())
                if (professor.getId().equals(recommendationRequest.getDepartmentId()))
                    break;
            recommendationId--;
        }
    }

    private void setRequestString() {
        StringBuilder requestMessage = new StringBuilder();
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (isMinor) {
            if (minorId >= MinorRequest.getMinorRequests().size() || minorId < 0)
                return;
            MinorRequest minorRequest = MinorRequest.getMinorRequests().get(minorId);
            requestMessage.append("student Id: " + minorRequest.getStudentId() + "\n");
            requestMessage.append("first department: " + minorRequest.getDepartmentId() + "\n");
            requestMessage.append("second department: " + minorRequest.getSecondDepartmentId() + "\n");
            requestMessage.append(minorRequest.getRequestText());
        }
        if (isRecommendation) {
            if (recommendationId < 0 || recommendationId >= RecommendationRequest.getRecommendationRequests().size())
                return;
            RecommendationRequest recommendationRequest = RecommendationRequest.getRecommendationRequests().get(recommendationId);
            requestMessage.append("student Id: " + recommendationRequest.getStudentId() + "\n");
            requestMessage.append(recommendationRequest.getRequestText() + "\n");
            requestMessage.append("your Recommendation text is:\n" + recommendationRequest.getAcceptedText());
        }
        if (isFreedom) {
            if (freedomId < 0 || freedomId >= FreedomRequest.getFreedomRequests().size())
                return;
            FreedomRequest freedomRequest = FreedomRequest.getFreedomRequests().get(freedomId);
            requestMessage.append("student Id: " + freedomRequest.getStudentId() + "\n");
            requestMessage.append(freedomRequest.getRequestText());
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
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (isMinor) {
            if (minorId >= MinorRequest.getMinorRequests().size() || minorId < 0)
                return;
            MinorRequest minorRequest = MinorRequest.getMinorRequests().get(minorId);
            if (minorRequest.isPending()) {
                if (professor.getDepartmentId().equals(minorRequest.getDepartmentId()))
                    minorRequest.setAccepted(true);
                if (professor.getDepartmentId().equals(minorRequest.getSecondDepartmentId()))
                    minorRequest.setSecondAccepted(true);
                log.info(professor.getId()+" accepted a minor request. "+ minorRequest.getId());
            }
        }
        if (isRecommendation) {
            if (recommendationId < 0 || recommendationId >= RecommendationRequest.getRecommendationRequests().size())
                return;
            RecommendationRequest recommendationRequest = RecommendationRequest.getRecommendationRequests().get(recommendationId);
            if (recommendationRequest.isPending())
                if (professor.getId().equals(recommendationRequest.getDepartmentId())){
                    recommendationRequest.setAccepted(true);
                    log.info(professor.getId()+" accepted a Recommendation request. "+recommendationRequest.getId());
                }
        }
        if (isFreedom) {
            if (freedomId < 0 || freedomId >= FreedomRequest.getFreedomRequests().size())
                return;
            FreedomRequest freedomRequest = FreedomRequest.getFreedomRequests().get(freedomId);
            if (freedomRequest.isPending())
                if (professor.getDepartmentId().equals(freedomRequest.getDepartmentId())) {
                    freedomRequest.setAccepted(true);
                    log.info(professor.getId()+" accepted a freedom request. "+freedomRequest.getId());
                }
        }
    }

    public void reject(ActionEvent actionEvent) {
        Professor professor = (Professor) (LogicalAgent.getInstance().getUser());
        if (isMinor) {
            if (minorId >= MinorRequest.getMinorRequests().size() || minorId < 0)
                return;
            MinorRequest minorRequest = MinorRequest.getMinorRequests().get(minorId);
            if (minorRequest.isPending()) {
                if (professor.getDepartmentId().equals(minorRequest.getDepartmentId()))
                    minorRequest.setAccepted(false);
                if (professor.getDepartmentId().equals(minorRequest.getSecondDepartmentId()))
                    minorRequest.setSecondAccepted(false);
                log.info(professor.getId()+" rejected a minor request. "+ minorRequest.getId());
            }
        }
        if (isRecommendation) {
            if (recommendationId < 0 || recommendationId >= RecommendationRequest.getRecommendationRequests().size())
                return;
            RecommendationRequest recommendationRequest = RecommendationRequest.getRecommendationRequests().get(recommendationId);
            if (recommendationRequest.isPending())
                if (professor.getId().equals(recommendationRequest.getDepartmentId())) {
                    recommendationRequest.setAccepted(false);
                    log.info(professor.getId()+" rejected a Recommendation request. "+recommendationRequest.getId());
                }
        }
        if (isFreedom) {
            if (freedomId < 0 || freedomId >= FreedomRequest.getFreedomRequests().size())
                return;
            FreedomRequest freedomRequest = FreedomRequest.getFreedomRequests().get(freedomId);
            if (freedomRequest.isPending())
                if (professor.getDepartmentId().equals(freedomRequest.getDepartmentId())) {
                    freedomRequest.setAccepted(false);
                    log.info(professor.getId()+" rejected a freedom request. "+freedomRequest.getId());
                }
        }
    }
}
