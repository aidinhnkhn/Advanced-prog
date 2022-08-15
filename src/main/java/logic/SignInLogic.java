package logic;

import com.google.common.hash.Hashing;
import elements.people.Manager;
import elements.people.Professor;
import elements.people.Student;
import javafx.scene.image.Image;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.message.Message;
import shared.messages.message.MessageStatus;
import shared.messages.response.Response;
import shared.util.JsonCaster;
import site.edu.Main;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class SignInLogic {
    private static SignInLogic signInLogic;
    private static Logger log = LogManager.getLogger(SignInLogic.class);
    private SignInLogic() {

    }

    public static SignInLogic getInstance() {
        if (signInLogic == null) {
            signInLogic = new SignInLogic();
        }
        return signInLogic;
    }

    public boolean signIn(String id, String password, String captcha, String image) {
        if (!checkCaptcha(captcha,image)) return false;
        if (id.charAt(0)=='s')
            return checkStudent(id, password);
        else if (id.charAt(0)=='p')
            return checkProfessor(id,password);
        else
            return checkManager(id,password);
    }

    private boolean checkManager(String id, String password) {
        String sha256hex = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        //sent the login request to the server, get the result
        Response response = Main.mainClient.getServerController().sendLoginRequest(id,sha256hex);
        if ((Boolean) response.getData("login")){

           Manager manager = JsonCaster.managerCaster((String)response.getData("user"));

            Main.mainClient.setUser(manager);

            return true;
        }
        return false;
    }

    public boolean checkCaptcha(String captcha, String image) {
        return image.equals(captcha+".png");
    }
    public boolean checkStudent(String id,String password){
        String sha256hex = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        //sent the login request to the server, get the result
        Response response = Main.mainClient.getServerController().sendLoginRequest(id,sha256hex);
        if ((Boolean) response.getData("login")){
            Student student = JsonCaster.studentCaster((String)response.getData("user"));
            //LogicalAgent.getInstance().setUser(student);
            Main.mainClient.setUser(student);
            return true;
        }
        return false;
    }
    public boolean checkProfessor(String id,String password){
        String sha256hex = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        //sent the login request to the server, get the result
        Response response = Main.mainClient.getServerController().sendLoginRequest(id,sha256hex);
        if ((Boolean) response.getData("login")){
            Professor professor = JsonCaster.professorCaster((String)response.getData("user"));
            //LogicalAgent.getInstance().setUser(professor);
            Main.mainClient.setUser(professor);
            return true;
        }
        return false;
    }
}
