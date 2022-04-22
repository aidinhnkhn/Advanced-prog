package elements.people;

import java.util.ArrayList;

public class Professor extends User{
    private boolean headDepartment,educationalAssistant;
    private static ArrayList<Professor> professors=new ArrayList<>();
    private ArrayList<String> coursesId;
    public Professor(String username, String password, Role role, String melicode, String phoneNumber, String email, String degree,String departmentId,String id) {
        super(username, password, role, melicode, phoneNumber, email, degree,departmentId,id);
        setEducationalAssistant();
        setHeadDepartment();
        professors.add(this);
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
