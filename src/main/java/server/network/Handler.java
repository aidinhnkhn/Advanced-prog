package server.network;

import elements.chat.Chat;
import elements.chat.pm.Pm;
import elements.chat.pm.PmType;
import elements.courses.Course;
import elements.courses.Grade;
import elements.people.*;
import elements.request.*;
import elements.university.Department;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import server.Server;
import server.university.University;
import shared.messages.message.Message;
import shared.messages.response.Response;
import shared.messages.response.ResponseStatus;
import shared.util.ImageSender;
import shared.util.JsonCaster;
import site.edu.Main;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        else if (id.charAt(0) == 'p')
            checkProfessorLogin(authToken, id, password);
        else
            checkManagerLogin(authToken,id,password);
    }

    private void checkManagerLogin(String authToken, String id, String password) {
        Response response = new Response(ResponseStatus.Login);
        Manager manager = University.getInstance().getManagerById(id);
        if (manager == null) {
            response.addData("login", false);
            Server.getServer().sendMessageToClient(authToken, Response.toJson(response));
            return;
        }
        if (manager.getPassword().equals(password)) {
            response.addData("login", true);
            System.out.println(JsonCaster.objectToJson(manager));
            response.addData("user", JsonCaster.objectToJson(manager));
            Server.getServer().getClientHandler(authToken).setUser(manager);
            log.info(manager.getId() + " logged in.");
        } else
            response.addData("login", false);
        Server.getServer().sendMessageToClient(authToken, Response.toJson(response));
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
        Professor professor = University.getInstance().getProfessorById((String) message.getData("id"));
        String professorString = JsonCaster.objectToJson(professor);
        Response response = new Response(ResponseStatus.sentProfessor);
        response.addData("professor", professorString);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
    }

    public void setUserInfo(Message message) {
        Server.getServer().getClientHandler(message.getAuthToken()).getUser().setTheme((Boolean) message.getData("theme"));
        Server.getServer().getClientHandler(message.getAuthToken()).getUser().setPhoneNumber((String) message.getData("number"));
        Server.getServer().getClientHandler(message.getAuthToken()).getUser().setEmail((String) message.getData("email"));
        log.info(Server.getServer().getClientHandler(message.getAuthToken()).getUser().getId() + " changed the information.");
    }

    public void setGradeObjection(Message message) {
        String courseId = (String) message.getData("courseId");
        String objectionText = (String) message.getData("objectionText");
        Student student = (Student) Server.getServer().getClientHandler(message.getAuthToken()).getUser();
        Grade grade = student.getGrade(courseId);
        grade.setObjection(true);
        grade.setObjectionText(objectionText);
        log.info(student.getId() + " objected to course " + courseId);
    }

    public void sendStudentList(Message message) {
        Response response = new Response(ResponseStatus.StudentList);
        String list = JsonCaster.objectToJson(University.getInstance().getStudents());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
    }

    public void sendCourse(Message message) {
        Response response = new Response(ResponseStatus.Course);
        Course course = University.getInstance().getCourseById((String) message.getData("id"));
        response.addData("course", JsonCaster.objectToJson(course));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("course sent");
    }

    public void sendStudent(Message message) {
        Response response = new Response(ResponseStatus.Student);
        Student student = University.getInstance().getStudentById((String) message.getData("id"));
        response.addData("student", JsonCaster.objectToJson(student));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("sent the student");
    }

    public void setGrade(Message message) {
        Student student = University.getInstance().getStudentById((String) message.getData("id"));
        Grade grade = student.getGrade((String) message.getData("courseId"));
        grade.setFinished(true);
        grade.setGrade((Double) message.getData("givenGrade"));
        grade.setGradeStatus();
        log.info("grade submitted for " + student.getId());
    }

    public void finalizeGrades(Message message) {
        Course course = University.getInstance().getCourseById((String) message.getData("courseId"));
        boolean check = true;
        for (String id : course.getStudentId()) {
            Student student = University.getInstance().getStudentById(id);
            Grade grade = student.getGrade((String) message.getData("courseId"));
            if (!grade.isFinished())
                check = false;
        }
        if (check) {
            for (String id : course.getStudentId()) {
                Student student = University.getInstance().getStudentById(id);
                Grade grade = student.getGrade((String) message.getData("courseId"));
                grade.setFinalGrade(true);
            }
            log.info((String) message.getData("courseId") + " grades finalized.");
        }
        Response response = new Response(ResponseStatus.FinalGradeStatus);
        response.addData("check", check);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
    }

    public void AnswerObjection(Message message) {
        Student student = University.getInstance().getStudentById((String) message.getData("id"));
        Grade grade = student.getGrade((String) message.getData("courseId"));
        grade.setAnswered(true);
        grade.setAnswerText((String) message.getData("text"));
        log.info("objection answer submitted");
    }

    public void sendCourseList(Message message) {
        Response response = new Response(ResponseStatus.CourseList);
        String list = JsonCaster.objectToJson(University.getInstance().getCourses());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("send the course List");
    }

    public void sendProfessorList(Message message) {
        Response response = new Response(ResponseStatus.ProfessorList);
        String list = JsonCaster.objectToJson(University.getInstance().getProfessors());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("send the professor list");
    }

    public void pickCourse(Message message) {
        Response response = new Response(ResponseStatus.UserInfo);
        Course course = University.getInstance().getCourseById((String) message.getData("courseId"));
        Student student = University.getInstance().getStudentById((String) message.getData("studentId"));
        course.getStudentId().add(student.getId());
        course.setStudentNumber(course.getStudentNumber()-1);
        student.addGrade(new Grade(course.getId(), 0));
        response.addData("student", JsonCaster.objectToJson(student));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info(student.getId() + " picked the course " + course.getId());
    }

    public void sendDepartmentList(Message message) {
        Response response = new Response(ResponseStatus.DepartmentList);
        String list = JsonCaster.objectToJson(University.getInstance().getDepartments());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("send the Department list");
    }

    public void sendDepartment(Message message) {
        Response response = new Response(ResponseStatus.Student);
        Department department = University.getInstance().getDepartmentById((String) message.getData("id"));
        response.addData("department", JsonCaster.objectToJson(department));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("sent the department");
    }

    public void saveUserImage(Message message) {
        String imageString = (String) message.getData("image");
        byte[] bytes = ImageSender.decode(imageString);
        Image image = new Image(new ByteArrayInputStream(bytes));
        saveImage(image, (String) message.getData("name"));
    }

    public void saveImage(Image image, String name) {
        String path = System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\users\\pictures\\" + name + ".png";
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            log.info("image saved");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void createStudent(Message message) {
        String username = (String) message.getData("username");
        String password = (String) message.getData("password");
        String melicode = (String) message.getData("melicode");
        String phoneNumber = (String) message.getData("phoneNumber");
        String email = (String) message.getData("email");
        String degree = (String) message.getData("degree");
        String department = (String) message.getData("department");
        String supervisorId = (String) message.getData("supervisorId");
        Student student = new Student(username, password, Role.Student, melicode,
                phoneNumber, email, degree, department, "nothing", supervisorId);
        Response response = new Response(ResponseStatus.sendNewUser);
        response.addData("student", JsonCaster.objectToJson(student));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.warn(student.getId() + " was added to edu!");
    }

    private Role GetRole(String profession) {
        if (profession.equals("Professor")) return Role.Professor;
        else if (profession.equals("HeadDepartment")) return Role.HeadDepartment;
        else return Role.EducationalAssistant;
    }

    public void createProfessor(Message message) {
        String username = (String) message.getData("username");
        String password = (String) message.getData("password");
        String melicode = (String) message.getData("melicode");
        String phoneNumber = (String) message.getData("phoneNumber");
        String email = (String) message.getData("email");
        String degree = (String) message.getData("degree");
        String department = (String) message.getData("department");
        String profession = (String) message.getData("profession");
        Professor professor = new Professor(username, password, GetRole(profession),
                melicode, phoneNumber, email, degree, department, "nothing");
        Response response = new Response(ResponseStatus.sendNewUser);
        response.addData("professor", JsonCaster.objectToJson(professor));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.warn(professor.getId() + " was added to edu!");
    }

    public void createCourse(Message message) {
        String name = (String) message.getData("name");
        String professorId = (String) message.getData("professorId");
        String departmentId = (String) message.getData("departmentId");
        ArrayList<String> days = JsonCaster.StringArrayListCaster((String) message.getData("days"));
        String hour = (String) message.getData("hour");
        String length = (String) message.getData("length");
        LocalDateTime localDateTime = JsonCaster.dateCaster((String) message.getData("localDateTime"));
        String degree = (String) message.getData("degree");
        String unit = (String) message.getData("unit");
        ArrayList<String> taIds=JsonCaster.StringArrayListCaster((String)message.getData("taIds"));
        String courseId = (String)message.getData("courseId");
        String previousCourse=(String)message.getData("previousCourse");
        String nowCourse=(String)message.getData("nowCourse");
        String studentNumber = (String)message.getData("studentNumber");
        Course course = new Course(name, professorId, departmentId, Integer.parseInt(unit), days,
                Integer.parseInt(hour), Integer.parseInt(length), localDateTime, degree,taIds,
                courseId,previousCourse,nowCourse,Integer.parseInt(studentNumber));

        log.warn(course.getId() + " was added to courses.");
    }

    public void editCourse(Message message) {
        String courseString = (String) message.getData("course");
        Course receivedCourse = JsonCaster.courseCaster(courseString);
        Course course = University.getInstance().getCourseById(receivedCourse.getId());
        course.setName(receivedCourse.getName());
        course.setProfessorId(receivedCourse.getProfessorId());
        course.setUnit(receivedCourse.getUnit());
        course.setLength(receivedCourse.getLength());
        course.setHour(receivedCourse.getHour());
        course.setDays(receivedCourse.getDays());
        course.setExamDate(receivedCourse.getExamDate());
        course.setDegree(receivedCourse.getDegree());
        log.info(course.getId() + " was edited!");
    }

    public void deleteCourse(Message message) {
        String id = (String) message.getData("id");
        Course course = Main.mainClient.getServerController().getCourseById(id);
        Professor professor = University.getInstance().getProfessorById(course.getProfessorId());
        professor.getCoursesId().remove(id);
        Department department = University.getInstance().getDepartmentById(course.getDepartmentId());
        department.getCourses().remove(id);
        University.getInstance().getCourses().remove(course);
        for (String studentId : course.getStudentId()) {
            Student student = University.getInstance().getStudentById(studentId);
            Grade grade = student.getGrade(id);
            if (grade == null) continue;
            if (grade.showGrade().equals("N/A") || !grade.isFinalGrade()) {
                grade.setGrade(20);
                grade.setFinished(true);
                grade.setFinalGrade(true);
            }
        }
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\course\\" + course.getId() + ".txt");
        if (file.delete())
            log.info(file.getName() + " got deleted.");
        else
            log.warn(" couldn't delete " + file.getName());
    }

    public void sendRecommendationList(Message message) {
        Response response = new Response(ResponseStatus.RecommendationList);
        String list = JsonCaster.objectToJson(University.getInstance().getRecommendationRequests());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("send the Recommendation list");
    }

    public void createRecommendation(Message message) {
        String studentId = (String) message.getData("studentId");
        String professorId = (String) message.getData("professorId");
        RecommendationRequest recommendationRequest = new RecommendationRequest(studentId, professorId);
        log.info(studentId + " requested a Recommendation to professor: " + professorId);
    }

    public void createCertificate(Message message) {
        String studentId = (String) message.getData("studentId");
        String departmentId = (String) message.getData("departmentId");
        CertificateStudentRequest certificateStudentRequest = new CertificateStudentRequest(studentId, departmentId);
        Response response = new Response(ResponseStatus.Certificate);
        response.addData("certificate", JsonCaster.objectToJson(certificateStudentRequest));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("created certifcate for:" + studentId);
    }

    public void sendFreedomList(Message message) {
        Response response = new Response(ResponseStatus.FreedomList);
        String list = JsonCaster.objectToJson(University.getInstance().getFreedomRequests());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("send the FreeDom list");
    }

    public void setHimFree(Message message) {
        String studentId = (String) message.getData("studentId");
        String departmentId = (String) message.getData("departmentId");
        FreedomRequest freedomRequest = new FreedomRequest(studentId, departmentId);
    }

    public void sendDormList(Message message) {
        Response response = new Response(ResponseStatus.DormList);
        String list = JsonCaster.objectToJson(University.getInstance().getDormRequests());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("send the Dorm list");
    }

    public void createDorm(Message message) {
        String studentId = (String) message.getData("studentId");
        String departmentId = (String) message.getData("departmentId");
        DormRequest dormRequest = new DormRequest(studentId, departmentId);
        log.info(studentId + " requested a dorm");
    }

    public void sendMinorList(Message message) {
        Response response = new Response(ResponseStatus.MinorList);
        String list = JsonCaster.objectToJson(University.getInstance().getMinorRequests());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("send the Minor list");
    }

    public void createMinor(Message message) {
        String studentId = (String) message.getData("studentId");
        String departmentId = (String) message.getData("departmentId");
        String secondDepartmentId = (String) message.getData("secondDepartmentId");
        MinorRequest minorRequest = new MinorRequest(studentId, departmentId, secondDepartmentId);
    }

    public void sendThesisList(Message message) {
        Response response = new Response(ResponseStatus.ThesisList);
        String list = JsonCaster.objectToJson(University.getInstance().getThesisDefenseRequests());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("send the Thesis Defenses list");
    }

    public void createThesis(Message message) {
        String studentId = (String) message.getData("studentId");
        String departmentId = (String) message.getData("departmentId");
        LocalDateTime date = JsonCaster.dateCaster((String) message.getData("date"));
        ThesisDefenseRequest thesisDefenseRequest = new ThesisDefenseRequest(studentId, departmentId, date);
    }

    public void acceptMinor1st(Message message) {
        String id = (String) message.getData("id");
        boolean accept = (Boolean) message.getData("accept");
        University.getInstance().getMinorById(id).setAccepted(accept);
        log.info(id + " 1st accepted: " + accept);
    }

    public void acceptMinor2nd(Message message) {
        String id = (String) message.getData("id");
        boolean accept = (Boolean) message.getData("accept");
        University.getInstance().getMinorById(id).setSecondAccepted(accept);
        log.info(id + " 2nd accepted: " + accept);
    }

    public void acceptRecommendation(Message message) {
        String id = (String) message.getData("id");
        boolean accept = (Boolean) message.getData("accept");
        University.getInstance().getRecommendationById(id).setAccepted(accept);
        log.info(id + " accepted: " + accept);
    }

    public void acceptFreedom(Message message) {
        String id = (String) message.getData("id");
        boolean accept = (Boolean) message.getData("accept");
        University.getInstance().getFreedomById(id).setAccepted(accept);
        log.info(id + " accepted: " + accept);
    }

    public void sendUpdate(Message message) {
        Response response = new Response(ResponseStatus.Update);
        String id = (String) message.getData("id");
        if (id.charAt(0) == 's') {
            Student student = University.getInstance().getStudentById(id);
            response.addData("student", JsonCaster.objectToJson(student));
        }
        else if (id.charAt(0) == 'p'){
            Professor professor = University.getInstance().getProfessorById(id);
            response.addData("professor", JsonCaster.objectToJson(professor));
        }
        response.addData("chats", University.getInstance().getUserChats(id));
        response.addData("courses",JsonCaster.objectToJson(University.getInstance().getCourses()));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
    }

    public void checkUser(Message message) {
        Response response = new Response(ResponseStatus.checkUser);
        boolean check = false;
        String id = (String) message.getData("id");
        for (Student student : University.getInstance().getStudents())
            if (student.getId().equals(id))
                check = true;
        for (Professor professor : University.getInstance().getProfessors())
            if (professor.getId().equals(id))
                check = true;
        response.addData("check", check);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("user checked");
    }

    public void createChat(Message message) {
        ArrayList<String> chatIds = JsonCaster.StringArrayListCaster((String) message.getData("chat"));
        String id = (String) message.getData("id");
        String text = (String) message.getData("text");
        for (String id2 : chatIds) {
            Chat chat = new Chat(id, University.getInstance().getUsername(id), id2, University.getInstance().getUsername(id2));
            Pm pm = new Pm(PmType.Text, University.getInstance().getUsername(id));
            pm.setContent(text);
            chat.addPm(pm);
            log.info(id + " started chatting with " + id2);
        }
    }

    public void addPm(Message message) {
        Pm pm = JsonCaster.pmCaster((String) message.getData("pm"));
        String chatId = (String) message.getData("id");
        University.getInstance().getChatById(chatId).addPm(pm);
        log.info("added a Pm to "+chatId);
    }

    public void sendAdmin(Message message) {
        String admin = JsonCaster.objectToJson(University.getInstance().getAdmin());
        Response response = new Response(ResponseStatus.SendAdmin);
        response.addData("user",admin);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("admin sent");
    }

    public void setClientHandlerUser(Message message) {
        User user = JsonCaster.userCaster((String)message.getData("user"));
        Server.getServer().getClientHandler(message.getAuthToken()).setUser(user);
        log.info("client handler is back Online");
    }

    public void setStartingDate(Message message) {
        LocalDateTime dateTime = JsonCaster.dateCaster((String)message.getData("date"));
        University.getInstance().setStartPicking(dateTime);
        log.warn("starting date changed into"+dateTime.toString());
    }
    public void setEndingDate(Message message) {
        LocalDateTime dateTime = JsonCaster.dateCaster((String)message.getData("date"));
        University.getInstance().setEndPicking(dateTime);
        log.warn("ending date changed into"+dateTime.toString());
    }

    public void setStudentDate(Message message) {
        LocalDateTime dateTime = JsonCaster.dateCaster((String)message.getData("date"));
        Student student = University.getInstance().getStudentById((String)message.getData("id"));
        student.setEnrollDate(dateTime);
        student.setEnrollPermission(true);
        log.info(student.getId()+" date was set!");
    }

    public void setStudentRegister(Message message) {
        Student student = University.getInstance().getStudentById((String)message.getData("id"));
        student.setEnrollPermission(false);
    }

    public void sendStartingDate(Message message) {
        Response response = new Response(ResponseStatus.StartingDate);
        response.addData("date",JsonCaster.objectToJson(University.getInstance().getStartPicking()));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
    }

    public void sendEndingDate(Message message) {
        Response response = new Response(ResponseStatus.StartingDate);
        response.addData("date",JsonCaster.objectToJson(University.getInstance().getEndPicking()));
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
    }

    public void starCourse(Message message) {
        String id = (String)message.getData("id");
        String courseId = (String)message.getData("courseId");
        University.getInstance().getStudentById(id).getFavoriteCourse().add(courseId);
    }

    public void UnStarCourse(Message message) {
        String id = (String)message.getData("id");
        String courseId = (String)message.getData("courseId");
        University.getInstance().getStudentById(id).getFavoriteCourse().remove(courseId);
    }

    public void requestCourse(Message message) {
        String id = (String)message.getData("id");
        String courseId = (String)message.getData("courseId");
        PickCourseRequest pickCourseRequest = new PickCourseRequest(id,courseId);
        log.info(courseId+" was requested by "+id);
    }

    public void deleteGrade(Message message) {
        String id = (String)message.getData("id");
        String courseId = (String)message.getData("courseId");
        Course course =  University.getInstance().getCourseById(courseId);
        course.getStudentId().remove(id);
        course.setStudentNumber(course.getStudentNumber()+1);
        Grade grade = University.getInstance().getStudentById(id).getGrade(courseId);
        University.getInstance().getStudentById(id).getGrades().remove(grade);

    }

    public void changeGrade(Message message) {
        String id = (String)message.getData("id");
        String courseId = (String)message.getData("mainCourseId");
        Course course =  University.getInstance().getCourseById(courseId);
        course.getStudentId().remove(id);
        course.setStudentNumber(course.getStudentNumber()+1);
        Grade grade = University.getInstance().getStudentById(id).getGrade(courseId);
        University.getInstance().getStudentById(id).getGrades().remove(grade);
        changeGroup(message);
    }

    private void changeGroup(Message message) {
        Course course = University.getInstance().getCourseById((String) message.getData("courseId"));
        Student student = University.getInstance().getStudentById((String) message.getData("id"));
        course.getStudentId().add(student.getId());
        course.setStudentNumber(course.getStudentNumber()-1);
        student.addGrade(new Grade(course.getId(), 0));
    }

    public void sendPickCourseList(Message message) {
        Response response = new Response(ResponseStatus.PickCourseList);
        String list = JsonCaster.objectToJson(University.getInstance().getPickCourseRequests());
        response.addData("list", list);
        Server.getServer().sendMessageToClient(message.getAuthToken(), Response.toJson(response));
        log.info("send the pick Course list");
    }

    public void acceptPickCourse(Message message) {
        PickCourseRequest request = University.getInstance().getPickCourseById((String)message.getData("id"));
        request.setAccepted(true);
        Course course = University.getInstance().getCourseById(request.getDepartmentId());
        Student student = University.getInstance().getStudentById(request.getStudentId());
        course.getStudentId().add(student.getId());
        course.setStudentNumber(course.getStudentNumber()-1);
        student.addGrade(new Grade(course.getId(), 0));
    }

    public void rejectPickCourse(Message message) {
        PickCourseRequest request = University.getInstance().getPickCourseById((String)message.getData("id"));
        request.setAccepted(false);
    }
}
