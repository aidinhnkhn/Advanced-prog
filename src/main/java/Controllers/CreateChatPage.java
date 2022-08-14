package Controllers;

import elements.chat.Chat;
import elements.people.Professor;
import elements.people.Role;
import elements.people.Student;
import elements.people.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateChatPage implements Initializable {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button homePageButton;
    @FXML
    public TableColumn<User, String> idColumn;

    @FXML
    public TableColumn<User, String> nameColumn;

    @FXML
    public TableView<User> tableView;

    @FXML
    public TextField idField;
    @FXML
    public Button sendButton,addButton;
    @FXML
    public TextArea messageText,IdText;
    @FXML
    public Label statusLabel;
    private ArrayList<String> sending= new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sending.clear();
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

    private void setupTable() {
        if (Main.mainClient.getUser() instanceof Student){
            tableView.setItems(setupStudent());
        }
        else{
           tableView.setItems(setupProfessor());
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
    }

    private ObservableList<User> setupProfessor() {
        ObservableList<User> users= FXCollections.observableArrayList();
        Professor professor = (Professor) Main.mainClient.getUser();
        for (Student checkStudent : requestStudents()){
            if (professor.getDepartmentId().equals(checkStudent.getDepartmentId()) && professor.getRole() != Role.Professor)
                users.add(checkStudent);
            else if (checkStudent.getSupervisorId().equals(professor.getId()))
                users.add(checkStudent);
        }
        return users;
    }

    private ObservableList<User> setupStudent(){
        ObservableList<User> users= FXCollections.observableArrayList();
        Student student = (Student) Main.mainClient.getUser();
        Professor professor = Main.mainClient.getServerController().getProfessor(student.getSupervisorId());
        users.add(professor);
        for (Student checkStudent : requestStudents()){
            if (student.getDepartmentId().equals(checkStudent.getDepartmentId()))
                users.add(checkStudent);
            else if (student.getEnterYear().equals(checkStudent.getEnterYear()))
                users.add(checkStudent);
        }
        return users;
    }
    public void goHomePage(ActionEvent actionEvent) {
        if (Main.mainClient.getUser() instanceof Student)
            SceneLoader.getInstance().changeScene("StudentHomePage.fxml",actionEvent);
        else
            SceneLoader.getInstance().changeScene("ProfessorHomePage.fxml",actionEvent);
    }
    private ArrayList<Student> requestStudents(){
        Response response=Main.mainClient.getServerController().getStudents();
        String listString = (String)response.getData("list");
        return JsonCaster.studentArrayListCaster(listString);
    }
    private ArrayList<Professor> requestProfessors() {
        Response response=Main.mainClient.getServerController().getProfessors();
        String listString = (String)response.getData("list");
        return JsonCaster.professorArrayListCaster(listString);
    }

    public void add(ActionEvent actionEvent) {
        String id = idField.getText();
        if (hasChat(id)){
            statusLabel.setText("Status: you already have a chat!");
            statusLabel.setStyle("-fx-text-fill: red");
            return;
        }
        if (Main.mainClient.getServerController().UserExist(id) && !sending.contains(id)) {
            sending.add(id);
            statusLabel.setText("Status: user added!");
            statusLabel.setStyle("-fx-text-fill: green");
            setupIdText();
        }
        else if(sending.contains(id)){
            statusLabel.setText("Status: user was already added!");
            statusLabel.setStyle("-fx-text-fill: red");
        }
        else {
            statusLabel.setText("Status: user doesn't exist!");
            statusLabel.setStyle("-fx-text-fill: red");
        }
    }

    private boolean hasChat(String id) {
        for (Chat chat : Main.mainClient.getChats())
            if (chat.getStudentId1().equals(id) || chat.getStudentId2().equals(id) || sending.contains(id))
                return true;
        return false;
    }

    private void setupIdText() {
        IdText.clear();
        for (String id : sending)
            IdText.appendText(id+'\n');
    }

    public void send(ActionEvent actionEvent) {
        String text = messageText.getText();
        if (text.equals("")){
            statusLabel.setText("Status: type something");
            statusLabel.setStyle("-fx-text-fill: red");
            return;
        }
        if (sending.size()==0){
            statusLabel.setText("Status: select someone");
            statusLabel.setStyle("-fx-text-fill: red");
            return;
        }
        statusLabel.setText("Status: chat created");
        statusLabel.setStyle("-fx-text-fill: green");
        Main.mainClient.getServerController().createChat(sending,text);
        sending.clear();
        messageText.clear();
        IdText.clear();
    }

    public void pickUser(MouseEvent event) {
        if (event.getClickCount() <= 1) return;
        if (tableView.getSelectionModel().getSelectedItem() != null){
            User selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (hasChat(selectedUser.getId())){
                statusLabel.setText("Status: you already have a chat!");
                statusLabel.setStyle("-fx-text-fill: red");
                return;
            }
            if (sending.contains(selectedUser.getId())){
                statusLabel.setText("Status: user was already added!");
                statusLabel.setStyle("-fx-text-fill: red");
            }
            else {
                sending.add(selectedUser.getId());
                statusLabel.setText("Status: user added!");
                statusLabel.setStyle("-fx-text-fill: green");
                setupIdText();
            }
        }
    }
}
