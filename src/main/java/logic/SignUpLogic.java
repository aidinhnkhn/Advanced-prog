package logic;

import elements.people.Professor;
import elements.people.Role;
import elements.people.Student;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SignUpLogic {
    private static SignUpLogic signUpLogic;

    private SignUpLogic(){

    }

    public static SignUpLogic getInstance(){
        if (signUpLogic==null){
            signUpLogic=new SignUpLogic();
        }
        return signUpLogic;
    }
    public boolean SignUp(String username, String password, String confirmPass,
                          String melicode, String phoneNumber, String email, String profession,
                          String department, String degree, Image image){
        if (!allChecked(username,password,confirmPass,melicode,phoneNumber
                ,email,profession,department,degree,image)) return false;
        //TODO: signup and save the user file:
        Student student;
        Professor professor;
        if (profession.equals("Student"))
            student=createStudent(username,password,melicode,phoneNumber,email,department,degree,image);

        else
            professor=createProfessor(username,password,profession,melicode,phoneNumber,email,department,degree,image);

        return true;
    }
    public void saveImage(Image image,String name){
        File file = new File(System.getProperty("user.dir")+
                "\\src\\main\\resources\\eData\\users\\pictures\\"+name+".png");
        System.out.println(file.getPath());
        try{
            ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private Student createStudent(String username, String password,String melicode,String phoneNumber,String email,String department,String degree,Image image){
        Student student=new Student(username,password,Role.Student,melicode,phoneNumber,email,degree,department,"nothing",0);
        saveImage(image, student.getId());
        return student;
    }
    private Professor createProfessor(String username,String password,String profession,String melicode,String phoneNumber,String email,String department,String degree,Image image){
        Professor professor=new Professor(username,password,GetRole(profession),melicode,phoneNumber,email,degree,department,"nothing");
        saveImage(image, professor.getId());
        return professor;
    }
    private Role GetRole(String profession){
        if (profession.equals("Professor")) return Role.Professor;
        else if (profession.equals("HeadDepartment")) return Role.HeadDepartment;
        else return Role.EducationalAssistant;
    }
    public boolean allChecked(String username, String password, String confirmPass,
                              String melicode, String phoneNumber, String email,String profession,
                              String department, String degree, Image image){
        if (username.equals("")) return false;
        if (password.equals("")) return false;
        if (!password.equals(confirmPass)) return false;
        if (melicode.equals("")) return false;
        if (phoneNumber.equals("")) return false;
        if (email.equals("")) return false;
        if (profession==null) return false;
        if (department==null) return false;
        if (degree==null) return false;
        if (image==null) return false;
        return true;
    }
}
