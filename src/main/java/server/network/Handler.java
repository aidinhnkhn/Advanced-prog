package server.network;

import elements.courses.Grade;
import elements.people.Professor;
import elements.people.Student;
import elements.people.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import server.Server;
import server.university.University;
import shared.messages.message.Message;
import shared.messages.response.Response;
import shared.messages.response.ResponseStatus;
import shared.util.ImageSender;
import shared.util.JsonCaster;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Random;

public class Handler {

    private static Logger log = LogManager.getLogger(Handler.class);
    private static Handler handler;

    private Handler() {

    }

    public static Handler getInstance() {
        if (handler == null)
            handler = new Handler();
        return handler;
    }

    public void sendCaptcha(String authToken) {
        String captchaName = handler.getCaptchaName();
        String path = System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\Captcha\\" + captchaName;
        String encodedImage = ImageSender.encode(path);
        Response response = new Response(ResponseStatus.Captcha);
        response.addData("image", encodedImage);
        response.addData("number", captchaName);
        Server.getServer().sendMessageToClient(authToken, Response.toJson(response));
    }

    private String getCaptchaName() {
        Random random = new Random();
        int captchaNumber = random.nextInt(5);
        if (captchaNumber == 0)
            return "5720.png";
        else if (captchaNumber == 1)
            return "6280.png";
        else if (captchaNumber == 2)
            return "8217.png";
        else if (captchaNumber == 3)
            return "8387.png";
        else
            return "8612.png";
    }

    public void checkLogin(String authToken, Message message) {

        String id = (String) message.getData("id");
        String password = (String) message.getData("password");
        if (id.charAt(0) == 's')
            checkStudentLogin(authToken, id, password);
        else
            checkProfessorLogin(authToken, id, password);
    }

    private void checkProfessorLogin(String authToken, String id, String password) {
        Response response = new Response(ResponseStatus.Login);
        Professor professor = University.getInstance().getProfessorById(id);
        if (professor == null) {
            response.addData("login", false);
            Server.getServer().sendMessageToClient(authToken, Response.toJson(response));
            return;
        }
        if (professor.getPassword().equals(password)) {
            response.addData("login", true);
            response.addData("user", JsonCaster.objectToJson(professor));
            Server.getServer().getClientHandler(authToken).setUser(professor);
            log.info(professor.getId() + " logged in.");
        } else
            response.addData("login", false);
        Server.getServer().sendMessageToClient(authToken, Response.toJson(response));
    }

    private void checkStudentLogin(String authToken, String id, String password) {
        Response response = new Response(ResponseStatus.Login);
        Student student = University.getInstance().getStudentById(id);
        if (student == null) {
            response.addData("login", false);
            Server.getServer().sendMessageToClient(authToken, Response.toJson(response));
            return;
        }
        if (student.isEducating() && student.getPassword().equals(password)) {
            response.addData("login", true);
            response.addData("user", JsonCaster.objectToJson(student));
            Server.getServer().getClientHandler(authToken).setUser(student);
            log.info(student.getId() + " logged in.");
        } else
            response.addData("login", false);
        Server.getServer().sendMessageToClient(authToken, Response.toJson(response));
    }

    public void setNewPassword(Message message) {
        User user = Server.getServer().getClientHandler(message.getAuthToken()).getUser();
        user.setPassword((String) message.getData("newPassword"));
    }

    public void setLastEnter(String authToken) {
        User user = Server.getServer().getClientHandler(authToken).getUser();
        user.setLastEnter(LocalDateTime.now());
    }

    public void sendUserImage(Message message) {
        String filename = message.getData("id") + ".png";
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\users\\pictures\\" + filename);
        String path = "";
        if (!file.exists())
            path = System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\every.png";
        else
            path = System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename;
        String encodedImage = ImageSender.encode(path);
        Response response = new Response(ResponseStatus.UserImage);
        response.addData("image", encodedImage);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
    }

    public void sendProfessor(Message message) {
        Professor professor =University.getInstance().getProfessorById((String)message.getData("id"));
        String professorString = JsonCaster.objectToJson(professor);
        Response response = new Response(ResponseStatus.sentProfessor);
        response.addData("professor",professorString);
        Server.getServer().sendMessageToClient(message.getAuthToken(),Response.toJson(response));
    }

    public void setUserInfo(Message message) {
        Server.getServer().getClientHandler(message.getAuthToken()).getUser().setTheme((Boolean)message.getData("theme"));
        Server.getServer().getClientHandler(message.getAuthToken()).getUser().setPhoneNumber((String)message.getData("number"));
        Server.getServer().getClientHandler(message.getAuthToken()).getUser().setEmail((String)message.getData("email"));
        log.info(Server.getServer().getClientHandler(message.getAuthToken()).getUser().getId()+" changed the information.");
    }

    public void setGradeObjection(Message message) {
        String courseId = (String)message.getData("courseId");
        String objectionText=(String)message.getData("objectionText");
        Student student = (Student) Server.getServer().getClientHandler(message.getAuthToken()).getUser();
        Grade grade=student.getGrade(courseId);
        grade.setObjection(true);
        grade.setObjectionText(objectionText);
        log.info(student.getId()+" objected to course "+courseId);
    }

    public void sendStudentList(Message message) {
        Response response = new Response(ResponseStatus.StudentList);
        String list = JsonCaster.objectToJson(University.getInstance().getStudents());
        response.addData("list",list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
    }
}
