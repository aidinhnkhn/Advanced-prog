package server.network;

import elements.people.Student;
import server.Server;
import server.university.University;
import shared.messages.message.Message;
import shared.messages.response.Response;
import shared.messages.response.ResponseStatus;
import shared.util.ImageSender;
import shared.util.JsonCaster;

import java.util.Random;

public class Handler {

    private static Handler handler;

    private Handler(){

    }

    public static Handler getInstance(){
        if (handler==null)
            handler = new Handler();
        return handler;
    }
    public void sendCaptcha(String authToken){
        String captchaName = handler.getCaptchaName();
        String path = System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\Captcha\\" + captchaName;
        String encodedImage = ImageSender.encode(path);
        Response response = new Response(ResponseStatus.Captcha);
        response.addData("image",encodedImage);
        response.addData("number",captchaName);
        Server.getServer().sendMessageToClient(authToken,Response.toJson(response));
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

        String id = (String)message.getData("id");
        String password = (String)message.getData("password");
        if (id.charAt(0)=='s')
            checkStudentLogin(authToken,id,password);
        else
            checkProfessorLogin(authToken,id,password);
    }

    private void checkProfessorLogin(String authToken, String id, String password) {

    }

    private void checkStudentLogin(String authToken, String id, String password) {
        Response response = new Response(ResponseStatus.Login);
        Student student = University.getInstance().getStudentById(id);
        if (student == null){
            response.addData("login",false);
            Server.getServer().sendMessageToClient(authToken,Response.toJson(response));
            return;
        }
        if (student.isEducating() && student.getPassword().equals(password)){
            response.addData("login",true);
            response.addData("user", JsonCaster.objectToJson(student));
            Server.getServer().getClientHandler(authToken).setUser(student);
        }
        else
            response.addData("login",false);
        Server.getServer().sendMessageToClient(authToken,Response.toJson(response));
    }
}
