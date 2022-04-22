package logic;

import Savers.Saver;
import com.google.common.hash.Hashing;
import elements.people.Professor;
import elements.people.Role;
import elements.people.Student;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SignUpLogic {
    private static SignUpLogic signUpLogic;

    private SignUpLogic() {

    }

    public static SignUpLogic getInstance() {
        if (signUpLogic == null) {
            signUpLogic = new SignUpLogic();
        }
        return signUpLogic;
    }

    public boolean SignUp(String username, String password, String confirmPass,
                          String melicode, String phoneNumber, String email, String profession,
                          String department, String degree, Image image) {
        if (!allChecked(username, password, confirmPass, melicode, phoneNumber
                , email, profession, department, degree, image)) return false;
        //TODO save the user file:
        Student student;
        Professor professor;
        String sha256hex = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        if (profession.equals("Student"))
            student = createStudent(username, sha256hex, melicode, phoneNumber, email, department, degree, image);

        else
            professor = createProfessor(username, sha256hex, profession, melicode, phoneNumber, email, department, degree, image);

        return true;
    }

    public void saveImage(Image image, String name) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\users\\pictures\\" + name + ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Student createStudent(String username, String password, String melicode, String phoneNumber,
                                  String email, String department, String degree, Image image) {
        Student student = new Student(username, password, Role.Student, melicode, phoneNumber, email, degree, department, "nothing");
        saveImage(image, student.getId());
        Saver.getInstance().saveStudent(student);
        return student;
    }

    private Professor createProfessor(String username, String password, String profession, String melicode, String phoneNumber, String email, String department, String degree, Image image) {
        Professor professor = new Professor(username, password, GetRole(profession), melicode, phoneNumber, email, degree, department, "nothing");
        saveImage(image, professor.getId());
        Saver.getInstance().saveProfessor(professor);
        return professor;
    }

    private Role GetRole(String profession) {
        if (profession.equals("Professor")) return Role.Professor;
        else if (profession.equals("HeadDepartment")) return Role.HeadDepartment;
        else return Role.EducationalAssistant;
    }

    public boolean allChecked(String username, String password, String confirmPass,
                              String melicode, String phoneNumber, String email, String profession,
                              String department, String degree, Image image) {
        if (username.equals("")) return false;
        if (password.equals("")) return false;
        if (!password.equals(confirmPass)) return false;
        if (melicode.equals("")) return false;
        if (phoneNumber.equals("")) return false;
        if (email.equals("")) return false;
        if (profession == null) return false;
        if (department == null) return false;
        if (degree == null) return false;
        if (image == null) return false;
        return true;
    }
}
