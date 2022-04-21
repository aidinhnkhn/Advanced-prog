package elements.people;

import java.util.ArrayList;

public class Professor extends User{
    private boolean headDepartment,educationalAssistant;
    private static ArrayList<Professor> professors;
    public Professor(String username, String password, Role role, String melicode, String phoneNumber, String email, String degree,String departmentId) {
        super(username, password, role, melicode, phoneNumber, email, degree,departmentId);
        setEducationalAssistant();
        setHeadDepartment();
    }
    public void setHeadDepartment(){
        headDepartment = role == Role.HeadDepartment;
    }
    public void setEducationalAssistant(){
        educationalAssistant = role == Role.EducationalAssistant;
    }

    public boolean isHeadDepartment() {
        return headDepartment;
    }

    public boolean isEducationalAssistant() {
        return educationalAssistant;
    }

    public static ArrayList<Professor> getProfessors() {
        return professors;
    }
    public Professor getProfessor(String professorId){
        for (Professor professor: Professor.getProfessors())
            if (professor.getId().equals(professorId))
                return professor;
        return null;
    }
}
