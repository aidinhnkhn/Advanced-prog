package Controllers;

import elements.people.Student;
import elements.request.ThesisDefenseRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import logic.LogicalAgent;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ThesisDefensePage implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    DatePicker datePicker;
    @FXML
    Button applyButton;
    @FXML
    TextField hourField;
    @FXML
    Button homePageButton;
    @FXML
    Label status;
    public void HomePage(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }

    public void apply(ActionEvent actionEvent) {
        ThesisDefenseRequest thesis=null;
        Student student=(Student) (LogicalAgent.getInstance().getUser());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Defense status");
        alert.setTitle("Defense request");
        for (ThesisDefenseRequest thesisDefenseRequest:ThesisDefenseRequest.getThesisDefenseRequests())
            if (thesisDefenseRequest.getStudentId().equals(student.getId()))
                thesis=thesisDefenseRequest;
        if (thesis!=null)
            alert.setContentText("you already have picked a date.");
        else if (!hourField.getText().matches("[0-9]+"))
            alert.setContentText("type only numbers");
        else if(Integer.parseInt(hourField.getText())>16 || Integer.parseInt(hourField.getText())<8)
            alert.setContentText("hour should be between 8 and 16");
        else{
            LocalDate requestedDate=datePicker.getValue();
            int hour=Integer.parseInt(hourField.getText());
            LocalDateTime finalDate=requestedDate.atTime(hour,0);
            if (LocalDateTime.now().isAfter(finalDate))
                alert.setContentText("pick a right date!");
            else {
                ThesisDefenseRequest thesisDefenseRequest = new ThesisDefenseRequest(student.getId(), student.getDepartmentId(), finalDate);
                alert.setContentText("successful");
            }
        }
        alert.show();
        setupLabel();
    }
    public void setupLabel(){
        status.setText("you haven't picked a date");
        Student student=(Student) (LogicalAgent.getInstance().getUser());
        for (ThesisDefenseRequest thesisDefenseRequest:ThesisDefenseRequest.getThesisDefenseRequests())
            if (thesisDefenseRequest.getStudentId().equals(student.getId()))
                status.setText(thesisDefenseRequest.getAcceptedText());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLabel();
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
