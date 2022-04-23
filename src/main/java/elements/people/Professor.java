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

    public void setHeadDepartment(boolean headDepartment) {
        this.headDepartment = headDepartment;
    }

    public void setEducationalAssistant(boolean educationalAssistant) {
        this.educationalAssistant = educationalAssistant;
    }

    public static void setProfessors(ArrayList<Professor> professors) {
        Professor.professors = professors;
    }

    public ArrayList<String> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(ArrayList<String> coursesId) {
        this.coursesId = coursesId;
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
    public static Professor getProfessor(String professorId){
        for (Professor professor: Professor.getProfessors())
            if (professor.getId().equals(professorId))
                return professor;
        return null;
    }
}
