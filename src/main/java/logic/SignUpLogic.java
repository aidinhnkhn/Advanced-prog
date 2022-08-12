package logic;

import server.Savers.Saver;
import com.google.common.hash.Hashing;
import elements.people.Professor;
import elements.people.Role;
import elements.people.Student;
import elements.university.Department;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.util.ImageSender;
import site.edu.Main;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SignUpLogic {
    private static SignUpLogic signUpLogic;
    private static Logger log = LogManager.getLogger(SignUpLogic.class);

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
                          String department, String degree, Image image, String supervisorId) {
        System.out.println("here");
        if (!allChecked(username, password, confirmPass, melicode, phoneNumber
                , email, profession, department, degree, image)) return false;
        if (profession.equals("Student") && checkSupervisor(supervisorId)) return false;

        String sha256hex = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();

        if (profession.equals("Student"))
            createStudent(username, sha256hex, melicode, phoneNumber, email, department, degree, image, supervisorId);

        else
            createProfessor(username, sha256hex, profession, melicode, phoneNumber, email, department, degree, image);

        return true;
    }

    private boolean checkSupervisor(String id) {
        Professor professor = Main.mainClient.getServerController().getProfessor(id);
        return (professor == null);
    }

    public void saveImage(Image image, String name) {
        String path = System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\test\\" + name + ".png";
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            log.info("image saved");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        String imageString = ImageSender.encode(path);
        Main.mainClient.getServerController().sendUserImage(imageString, name);
        file.delete();
    }

    private void createStudent(String username, String password, String melicode, String phoneNumber,
                               String email, String department, String degree, Image image, String supervisorId) {
        Student student = Main.mainClient.getServerController().makeStudent(username, password, Role.Student,
                melicode, phoneNumber, email, degree, department, "nothing", supervisorId);

        saveImage(image, student.getId());
        log.info("student created");
    }

    private void createProfessor(String username, String password, String profession, String melicode, String phoneNumber, String email, String department, String degree, Image image) {
        Professor professor = Main.mainClient.getServerController().makeProfessor(username, password, profession,
                melicode, phoneNumber, email, department, degree);

        saveImage(image, professor.getId());
        log.info("professor created");
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

        Department department1 = Main.mainClient.getServerController().getDepartmentById(department);

        if (department1.getEducationalAssistantId().equals("")) return false;

        if (department1.getHeadDepartmentId().equals("")) return false;
        return true;
    }
}
