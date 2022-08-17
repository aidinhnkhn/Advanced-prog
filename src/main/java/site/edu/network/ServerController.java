package site.edu.network;

import elements.chat.pm.Pm;
import elements.courses.Course;
import elements.people.Professor;
import elements.people.Role;
import elements.people.Student;
import elements.people.User;
import elements.request.CertificateStudentRequest;
import elements.university.Department;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.message.Message;
import shared.messages.message.MessageStatus;
import shared.messages.response.Response;
import shared.util.Config;
import shared.util.JsonCaster;
import site.edu.Client;
import site.edu.Main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerController {
    private static Logger log = LogManager.getLogger(ServerController.class);
    private PrintStream out;
    private Scanner in;

    private boolean serverOnline;
    private Client client;
    private  Socket socket;

    public ServerController(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.serverOnline = true;
    }

    public void setServerOnline(boolean serverOnline) {
        this.serverOnline = serverOnline;
        if (serverOnline){
            try {
                this.socket = new Socket(InetAddress.getLocalHost(), Config.getConfig().getProperty(Integer.class,"serverPort"));
                connectToServer();
                getBackOnline();
            } catch (IOException e) {
                log.fatal("server controller could not reconnect");
            }
        }
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
                log.info("Server is offline.");
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
        sendMessage(Message.toJson(message));
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

    public Response getUserImage(String id){
        Message message = new Message(MessageStatus.UserImage, client.getAuthToken());
        message.addData("id", id);
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
        if (Main.mainClient.getServerController().isServerOnline()){
        Message message = new Message(MessageStatus.Course, client.getAuthToken());
        message.addData("id", courseId);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        Course course = JsonCaster.courseCaster((String) response.getData("course"));
        return course;
        }
        else
            return offlineCourse(courseId);
    }

    private Course offlineCourse(String courseId) {
        for (Course course : Main.mainClient.getCourses())
            if (course.getId().equals(courseId))
                return course;
        return null;
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

    public Student makeStudent(String username, String password, Role student, String melicode, String phoneNumber,
                               String email, String degree, String department, String nothing, String supervisorId) {
        Message message = new Message(MessageStatus.CreateStudent,client.getAuthToken());
        message.addData("username",username);
        message.addData("password",password);
        message.addData("melicode",melicode);
        message.addData("phoneNumber",phoneNumber);
        message.addData("email",email);
        message.addData("degree",degree);
        message.addData("department",department);
        message.addData("supervisorId",supervisorId);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return JsonCaster.studentCaster((String) response.getData("student"));
    }

    public Professor makeProfessor(String username, String password, String profession, String melicode,
                                   String phoneNumber, String email, String department, String degree) {
        Message message = new Message(MessageStatus.CreateProfessor,client.getAuthToken());
        message.addData("username",username);
        message.addData("password",password);
        message.addData("melicode",melicode);
        message.addData("phoneNumber",phoneNumber);
        message.addData("email",email);
        message.addData("degree",degree);
        message.addData("department",department);
        message.addData("profession",profession);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return JsonCaster.professorCaster((String) response.getData("professor"));
    }

    public void createCourse(String name, String professorId, String departmentId, String unit, ArrayList<String> days,
                             String hour, String length, LocalDateTime localDateTime, String degree, ArrayList<String> taIds,
                             String courseId, String previousCourse, String nowCourse, String studentNumber) {

        Message message = new Message(MessageStatus.CreateCourse,client.getAuthToken());
        message.addData("name",name);
        message.addData("professorId",professorId);
        message.addData("departmentId",departmentId);
        message.addData("days",JsonCaster.objectToJson(days));
        message.addData("hour",hour);
        message.addData("length",length);
        message.addData("localDateTime",JsonCaster.objectToJson(localDateTime));
        message.addData("degree",degree);
        message.addData("unit",unit);
        message.addData("taIds",JsonCaster.objectToJson(taIds));
        message.addData("courseId",courseId);
        message.addData("previousCourse",previousCourse);
        message.addData("nowCourse",nowCourse);
        message.addData("studentNumber",studentNumber);
        sendMessage(Message.toJson(message));
    }

    public void editCourse(Course course) {
        Message message = new Message(MessageStatus.EditCourse,client.getAuthToken());
        message.addData("course",JsonCaster.objectToJson(course));
        sendMessage(Message.toJson(message));
    }

    public void deleteCourse(String id) {
        Message message = new Message(MessageStatus.DeleteCourse,client.getAuthToken());
        message.addData("id",id);
        sendMessage(Message.toJson(message));
    }

    public Response getRecommendations() {
        Message message = new Message(MessageStatus.RecommendationList, client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return response;
    }

    public void requestRecommendation(String id, String text) {
        Message message = new Message(MessageStatus.Recommendation,client.getAuthToken());
        message.addData("studentId",id);
        message.addData("professorId",text);
        sendMessage(Message.toJson(message));
    }

    public CertificateStudentRequest getCertificate(String id, String departmentId) {
        Message message = new Message(MessageStatus.Certificate,client.getAuthToken());
        message.addData("studentId",id);
        message.addData("departmentId",departmentId);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return JsonCaster.certificateCaster((String)response.getData("certificate"));
    }

    public Response getFreedoms() {
        Message message = new Message(MessageStatus.FreedomList, client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return response;
    }

    public void requestFreedom(String id, String departmentId) {
        Message message = new Message(MessageStatus.Freedom,client.getAuthToken());
        message.addData("studentId",id);
        message.addData("departmentId",departmentId);
        sendMessage(Message.toJson(message));
    }

    public Response getDorms() {
        Message message = new Message(MessageStatus.DormList, client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return response;
    }

    public void createDorm(String id, String departmentId) {
        Message message = new Message(MessageStatus.Dorm,client.getAuthToken());
        message.addData("studentId",id);
        message.addData("departmentId",departmentId);
        sendMessage(Message.toJson(message));
    }

    public Response getMinors() {
        Message message = new Message(MessageStatus.MinorList, client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return response;
    }

    public void createMinor(String id, String departmentId, String secondDepartmentId) {
        Message message = new Message(MessageStatus.Minor,client.getAuthToken());
        message.addData("studentId",id);
        message.addData("departmentId",departmentId);
        message.addData("secondDepartmentId",secondDepartmentId);
        sendMessage(Message.toJson(message));
    }

    public Response getThesisDefenses() {
        Message message = new Message(MessageStatus.ThesisList, client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return response;
    }

    public void createThesis(String id, String departmentId, LocalDateTime finalDate) {
        Message message = new Message(MessageStatus.Thesis,client.getAuthToken());
        message.addData("studentId",id);
        message.addData("departmentId",departmentId);
        message.addData("date",JsonCaster.objectToJson(finalDate));
        sendMessage(Message.toJson(message));
    }

    public void acceptMinor1st(String id,Boolean accept) {
        Message message = new Message(MessageStatus.Minor1stAccept,client.getAuthToken());
        message.addData("id",id);
        message.addData("accept",accept);
        sendMessage(Message.toJson(message));
    }

    public void acceptMinor2nd(String id, boolean accept) {
        Message message = new Message(MessageStatus.Minor2ndAccept,client.getAuthToken());
        message.addData("id",id);
        message.addData("accept",accept);
        sendMessage(Message.toJson(message));
    }

    public void acceptRecommendation(String id, boolean accept) {
        Message message = new Message(MessageStatus.RecommendationAccept,client.getAuthToken());
        message.addData("id",id);
        message.addData("accept",accept);
        sendMessage(Message.toJson(message));
    }

    public void acceptFreedom(String id, boolean accept) {
        Message message = new Message(MessageStatus.FreedomAccept,client.getAuthToken());
        message.addData("id",id);
        message.addData("accept",accept);
        sendMessage(Message.toJson(message));
    }

    public boolean UserExist(String id) {
        Message message = new Message(MessageStatus.checkUser,client.getAuthToken());
        message.addData("id",id);
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return (Boolean)response.getData("check");
    }

    public void createChat(ArrayList<String> sending, String text) {
        Message message = new Message(MessageStatus.CreateChat,client.getAuthToken());
        message.addData("chat",JsonCaster.objectToJson(sending));
        message.addData("text",text);
        message.addData("id",Main.mainClient.getUser().getId());
        sendMessage(Message.toJson(message));
    }
    public void sendPm(Pm pm,String chatId){
        Message message = new Message(MessageStatus.AddPm,client.getAuthToken());
        message.addData("id",chatId);
        message.addData("pm",JsonCaster.objectToJson(pm));
        sendMessage(Message.toJson(message));
    }

    public User getAdmin() {
        Message message = new Message(MessageStatus.GetAdmin,client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return JsonCaster.managerCaster((String)response.getData("user"));
    }

    public void getBackOnline(){
        Message message = new Message(MessageStatus.OnlineAgain,client.getAuthToken());
        message.addData("user",JsonCaster.objectToJson(client.getUser()));
        sendMessage(Message.toJson(message));
    }

    public void sendStartingDate(LocalDateTime date) {
        Message message = new Message(MessageStatus.StartingDate,client.getAuthToken());
        message.addData("date",JsonCaster.objectToJson(date));
        sendMessage(Message.toJson(message));
    }

    public void sendEndingDate(LocalDateTime date){
        Message message = new Message(MessageStatus.EndingDate,client.getAuthToken());
        message.addData("date",JsonCaster.objectToJson(date));
        sendMessage(Message.toJson(message));
    }

    public void sendStudentDate(String id, LocalDateTime date) {
        Message message = new Message(MessageStatus.StudentDate,client.getAuthToken());
        message.addData("date",JsonCaster.objectToJson(date));
        message.addData("id",id);
        sendMessage(Message.toJson(message));
    }

    public void sendNotRegister(String id) {
        Message message = new Message(MessageStatus.StudentNoRegister,client.getAuthToken());
        message.addData("id",id);
        sendMessage(Message.toJson(message));
    }

    public LocalDateTime getStartingDate() {
        Message message = new Message(MessageStatus.GetStartingDate,client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return JsonCaster.dateCaster((String)response.getData("date"));
    }
    public LocalDateTime getEndingDate() {
        Message message = new Message(MessageStatus.GetEndingDate,client.getAuthToken());
        sendMessage(Message.toJson(message));
        Response response = Response.fromJson(receiveMessage());
        return JsonCaster.dateCaster((String)response.getData("date"));
    }

    public void starCourse(String id, String courseId) {
        Message message = new Message(MessageStatus.StarCourse,client.getAuthToken());
        message.addData("id",id);
        message.addData("courseId",courseId);
        sendMessage(Message.toJson(message));
    }

    public void unStarCourse(String id, String courseId) {
        Message message = new Message(MessageStatus.UnStarCourse,client.getAuthToken());
        message.addData("id",id);
        message.addData("courseId",courseId);
        sendMessage(Message.toJson(message));
    }

    public void pickCourseRequest(String id, String courseId) {
        Message message = new Message(MessageStatus.CourseRequest,client.getAuthToken());
        message.addData("id",id);
        message.addData("courseId",courseId);
        sendMessage(Message.toJson(message));
    }

    public void deleteGrade(String id, String courseId) {
        Message message = new Message(MessageStatus.DeleteGrade,client.getAuthToken());
        message.addData("id",id);
        message.addData("courseId",courseId);
        sendMessage(Message.toJson(message));
    }

    public void changeGroup(String id, String courseId, String mainCourseId) {
        Message message = new Message(MessageStatus.ChangeGrade,client.getAuthToken());
        message.addData("id",id);
        message.addData("courseId",courseId);
        message.addData("mainCourseId",mainCourseId);
        sendMessage(Message.toJson(message));
    }
}
