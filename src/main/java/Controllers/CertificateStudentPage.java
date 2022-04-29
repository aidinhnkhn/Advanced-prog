package Controllers;

import elements.people.Student;
import elements.request.CertificateStudentRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import java.net.URL;
import java.util.ResourceBundle;

public class CertificateStudentPage implements Initializable {
    private static Logger log = LogManager.getLogger(CertificateStudentPage.class);
    @FXML
    public AnchorPane anchorPane;
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
        log.info(LogicalAgent.getInstance().getUser().getId()+ " request a certificate");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (LogicalAgent.getInstance().getUser().isTheme()) {
            anchorPane.setStyle("    -fx-background-color:\n" +
                    "            linear-gradient(#4568DC, #B06AB3),\n" +
                    "            repeating-image-pattern(\"Stars_128.png\"),\n" +
                    "            radial-gradient(center 50% 50%, radius 50%, #FFFFFF33, #00000033);\n");
        }
        else{
            anchorPane.setStyle("-fx-background-color: CORNFLOWERBLUE");
        }
    }
}
