package logic;

import com.google.common.hash.Hashing;
import elements.people.Student;
import javafx.scene.image.Image;

import java.nio.charset.StandardCharsets;

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
        if (!checkStudent(id,password)) return false;
        return true;
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
        return sha256hex.equals(student.getPassword());
    }
}
