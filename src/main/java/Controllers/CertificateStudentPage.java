package Controllers;

import elements.people.Student;
import elements.request.CertificateStudentRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import logic.LogicalAgent;

public class CertificateStudentPage {
    @FXML
    Button HomePageButton;

    @FXML
    Button certificateButton;

    @FXML
    TextArea certificateText;
    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    public void getCertificate(ActionEvent actionEvent) {
        CertificateStudentRequest certificateStudentRequest=new CertificateStudentRequest
                (LogicalAgent.getInstance().getUser().getId(),LogicalAgent.getInstance().getUser().getDepartmentId());
        certificateText.setText(certificateStudentRequest.getAcceptedText());
    }
}
