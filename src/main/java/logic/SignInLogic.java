package logic;

import com.google.common.hash.Hashing;
import elements.people.Professor;
import elements.people.Student;
import javafx.scene.image.Image;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
        else
            return checkProfessor(id,password);
    }

    public boolean checkCaptcha(String captcha, String image) {
        return image.equals(captcha+".png");
    }
    public boolean checkStudent(String id,String password){
        String sha256hex = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        //TODO: sent the login request to the server, get the result
        Student student=Student.getStudent(id); // all of these lines turn into a return & set the logicalAgent user
        if (student==null) return false;
        if (sha256hex.equals(student.getPassword()) && student.isEducating()){
            LogicalAgent.getInstance().setUser(student);
            log.info(student.getId()+" singed in.");
            return true;
        }
        return false;
    }
    public boolean checkProfessor(String id,String password){
        String sha256hex = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        //TODO: sent the login request to the server, get the result
        Professor professor=Professor.getProfessor(id);
        if (professor==null) return false;
        if (sha256hex.equals(professor.getPassword())){
            LogicalAgent.getInstance().setUser(professor);
            log.info(professor.getId()+" singed in.");
            return true;
        }
        return false;
    }
}
