package Controllers;

import elements.people.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import logic.EditCourseLogic;
import logic.LogicalAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditCourse implements Initializable {
    private static Logger log = LogManager.getLogger(DormRequestPage.class);
    @FXML
    public AnchorPane anchorPane;
    @FXML
    DatePicker datePicker;
    @FXML
    Button homePageButton;
    @FXML
    CheckMenuItem Monday, Tuesday, Wednesday, Thursday, Friday;
    @FXML
    TextField name, professorId, departmentId, examHour;
    @FXML
    TextField unit, hour, length;
    @FXML
    TextField courseId;

    @FXML
    ComboBox<String> degreeBox;
    public void goBack(ActionEvent actionEvent) {
        if (LogicalAgent.getInstance().getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml", actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml", actionEvent);
    }

    public ArrayList<String> getDays() {
        ArrayList<String> days = new ArrayList<>();
        if (Monday.isSelected()) days.add("Monday");
        if (Thursday.isSelected()) days.add("Thursday");
        if (Wednesday.isSelected()) days.add("Wednesday");
        if (Thursday.isSelected()) days.add("Thursday");
        if (Friday.isSelected()) days.add("Friday");

        return days;
    }

    public void reset() {
        Monday.setSelected(false);
        Thursday.setSelected(false);
        Wednesday.setSelected(false);
        Thursday.setSelected(false);
        Friday.setSelected(false);
    }

    public void createCourse(ActionEvent actionEvent) {
        boolean successful = EditCourseLogic.getInstance().createCourse(name.getText(), professorId.getText(),
                departmentId.getText(), unit.getText(), length.getText(), hour.getText()
                , getDays(), datePicker.getValue(), examHour.getText(),degreeBox.getValue());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Create Course status");
        alert.setTitle("Create Course");
        if (successful) {
            alert.setContentText("successful!");
            reset();
        } else
            alert.setContentText("unsuccessful! please fill each form correctly");
        alert.show();
    }

    public void editCourse(ActionEvent actionEvent) {
        boolean successful = EditCourseLogic.getInstance().editCourse(name.getText(), professorId.getText(),
                unit.getText(), length.getText(), hour.getText(), getDays(),
                courseId.getText(), datePicker.getValue(), examHour.getText(),degreeBox.getValue());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("edit Course status");
        alert.setTitle("edit Course");
        if (successful) {
            alert.setContentText("successful!");
            reset();
        } else
            alert.setContentText("unsuccessful! please fill each form correctly");
        alert.show();
    }

    public void deleteCourse(ActionEvent actionEvent) {
        boolean successful = EditCourseLogic.getInstance().deleteCourse(courseId.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("delete Course status");
        alert.setTitle("delete Course");
        if (successful) {
            alert.setContentText("successful!");
        } else
            alert.setContentText("unsuccessful! please fill each form correctly");
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        degreeBox.getItems().addAll("Bachelor", "Masters", "PHD");
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
