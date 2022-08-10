package site.edu.network;

import elements.courses.Course;
import elements.people.Professor;
import elements.people.Student;
import elements.people.User;
import elements.university.Department;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.message.Message;
import shared.messages.message.MessageStatus;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Client;
import site.edu.Main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerController {
    private static Logger log = LogManager.getLogger(ServerController.class);
    private PrintStream out;
    private Scanner in;

    private boolean serverOnline;
    private Client client;
    private final Socket socket;

    public ServerController(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.serverOnline = true;
    }

    public boolean isServerOnline() {
        return serverOnline;
    }

    public Client getClient() {
        return client;
    }

    public void connectToServer() {
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new Scanner(socket.getInputStream());
            log.info("Client connected!");
            //this.sendMessage("Client connected.\nover");
            setAuthToken();
        } catch (IOException e) {
            log.error("serverController couldn't connect.");
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public String receiveMessage() {
        try {
            String messageFromServer = in.nextLine();
            while (true) {
                String nextLine = in.nextLine();
                if (nextLine.equals("over")) break;
                messageFromServer += nextLine;
            }
            //log.info(messageFromServer);
            return messageFromServer;
        } catch (NoSuchElementException e) {
            if (serverOnline) {
                log.info("Server is offline.");
            }
            serverOnline = false;
        }
        return "";
    }

    private void setAuthToken() {
        String message = receiveMessage();
        Response response = Response.fromJson(message);
        String authToken = (String) (response.getData("authToken"));
        this.client.setAuthToken(authToken);
        log.info(authToken);
    }

    public Response getCaptcha() {
        Message message = new Message(MessageStatus.Captcha, client.getAuthToken());
        this.sendMessage(Message.toJson(message));
        String responseString = receiveMessage();
        Response response = Response.fromJson(responseString);
        log.info("received captcha");
        return response;
    }

    public Response sendLoginRequest(String id, String password) {
        Message message = new Message(MessageStatus.Login, client.getAuthToken());
        message.addData("id", id);
        message.addData("password", password);
        Main.mainClient.getServerController().sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        log.info("received login response");
        return response;
    }

    public void sendNewPassword(String password) {
        Message message = new Message(MessageStatus.NewPassword, client.getAuthToken());
        message.addData("newPassword", password);
        sendMessage(Message.toJson(message));
    }


    public void sendLastEnter() {
        Message message = new Message(MessageStatus.LastEnter, client.getAuthToken());
        sendMessage(Message.toJson(message));
    }

    public Response getUserImage() {
        Message message = new Message(MessageStatus.UserImage, client.getAuthToken());
        message.addData("id", Main.mainClient.getUser().getId());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        log.info("received user Image");
        return response;
    }

    public Professor getProfessor(String supervisorId) {
        Message message = new Message(MessageStatus.getProfessor, client.getAuthToken());
        message.addData("id", supervisorId);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        log.info("professor received!");
        Professor professor = JsonCaster.professorCaster((String) response.getData("professor"));
        return professor;
    }

    public void sendNewUserInfo(User user) {
        Message message = new Message(MessageStatus.NewUserInfo, client.getAuthToken());
        message.addData("email", user.getEmail());
        message.addData("number", user.getPhoneNumber());
        message.addData("theme", user.isTheme());
        sendMessage(Message.toJson(message));
    }

    public void sendObjection(String courseId, String text) {
        Message message = new Message(MessageStatus.Objection, client.getAuthToken());
        message.addData("courseId", courseId);
        message.addData("objectionText", text);
        sendMessage(Message.toJson(message));
    }

    public Response getStudents() {
        Message message = new Message(MessageStatus.StudentList, client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return response;
    }

    public Course getCourseById(String courseId) {
        Message message = new Message(MessageStatus.Course, client.getAuthToken());
        message.addData("id", courseId);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        Course course = JsonCaster.courseCaster((String) response.getData("course"));
        return course;
    }

    public Student getStudentById(String id) {
        Message message = new Message(MessageStatus.Student, client.getAuthToken());
        message.addData("id", id);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        Student student = JsonCaster.studentCaster((String) response.getData("student"));
        return student;
    }

    public void sendStudentGrade(String id, String courseId, double professorGrade) {
        Message message = new Message(MessageStatus.setGrade, client.getAuthToken());
        message.addData("id", id);
        message.addData("courseId", courseId);
        message.addData("givenGrade", professorGrade);
        sendMessage(Message.toJson(message));
    }


    public boolean finalizeGrades(String courseId, String id) {
        Message message = new Message(MessageStatus.FinalizeGrades, client.getAuthToken());
        message.addData("id", id);
        message.addData("courseId", courseId);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return (Boolean) response.getData("check");
    }

    public void answerObjection(String text, String id, String courseId) {
        Message message = new Message(MessageStatus.AnswerObjection, client.getAuthToken());
        message.addData("text", text);
        message.addData("id", id);
        message.addData("courseId", courseId);
        sendMessage(Message.toJson(message));
    }

    public Response getCourses() {
        Message message = new Message(MessageStatus.CourseList, client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return response;
    }

    public Response getProfessors() {
        Message message = new Message(MessageStatus.ProfessorList, client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return response;
    }

    public Student pickCourse(String courseId, String studentId) {
        Message message = new Message(MessageStatus.PickCourse, client.getAuthToken());
        message.addData("courseId", courseId);
        message.addData("studentId", studentId);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        String studentString = (String) response.getData("student");
        return JsonCaster.studentCaster(studentString);
    }

    public Response getDepartments() {
        Message message = new Message(MessageStatus.DepartmentList, client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return response;
    }

    public Department getDepartmentById(String id) {
        Message message = new Message(MessageStatus.Department, client.getAuthToken());
        message.addData("id", id);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        Department department = JsonCaster.departmentCaster((String) response.getData("department"));
        return department;
    }

    public void sendUserImage(String imageString, String name) {
        Message message = new Message(MessageStatus.SendUserImage,client.getAuthToken());
        message.addData("image",imageString);
        message.addData("name",name);
        sendMessage(Message.toJson(message));
    }
}
