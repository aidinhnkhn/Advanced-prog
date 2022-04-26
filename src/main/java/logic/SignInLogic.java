package logic;

import com.google.common.hash.Hashing;
import elements.people.Professor;
import elements.people.Student;
import javafx.scene.image.Image;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class SignInLogic {
    private static SignInLogic signInLogic;

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
        Student student=Student.getStudent(id);
        if (student==null) return false;
        String sha256hex = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        if (sha256hex.equals(student.getPassword()) && student.isEducating()){
            LogicalAgent.getInstance().setUser(student);
            student.setLastEnter(LocalDateTime.now());
            return true;
        }
        return false;
    }
    public boolean checkProfessor(String id,String password){
        Professor professor=Professor.getProfessor(id);
        if (professor==null) return false;
        String sha256hex = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        if (sha256hex.equals(professor.getPassword())){
            LogicalAgent.getInstance().setUser(professor);
            professor.setLastEnter(LocalDateTime.now());
            return true;
        }
        return false;
    }
}
