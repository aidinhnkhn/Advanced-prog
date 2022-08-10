package elements.people;

import elements.university.Department;
import server.university.University;

import java.util.ArrayList;

public class Professor extends User {
    private boolean headDepartment, educationalAssistant, supervisor;
    private static ArrayList<Professor> professors = new ArrayList<>();
    private ArrayList<String> coursesId = new ArrayList<>();

    public Professor(String username, String password, Role role, String melicode, String phoneNumber, String email, String degree, String departmentId, String id) {
        super(username, password, role, melicode, phoneNumber, email, degree, departmentId, id);
        setEducationalAssistant();
        setHeadDepartment();
        professors.add(this);
        supervisor = true;
        University.getInstance().getDepartmentById(this.departmentId).getProfessors().add(this.id);
        University.getInstance().getProfessors().add(this);
    }

    public void setRole(Role role) {
        this.role = role;
        setHeadDepartment();
        setEducationalAssistant();
    }

    public boolean isSupervisor() {
        return supervisor;
    }

    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
        if (!supervisor)
            for (Student student : Student.getStudents())
                if (student.getSupervisorId().equals(this.id))
                    student.setSupervisorId("");
    }

    public void setHeadDepartment(boolean headDepartment) {
        this.headDepartment = headDepartment;
        if (headDepartment) this.role=Role.HeadDepartment;
    }

    public void setEducationalAssistant(boolean educationalAssistant) {
        this.educationalAssistant = educationalAssistant;
        if (educationalAssistant) this.role=Role.EducationalAssistant;
    }

    public static void setProfessors(ArrayList<Professor> professors) {
        Professor.professors = professors;
    }

    public ArrayList<String> getCoursesId() {
        if (coursesId == null)
            coursesId = new ArrayList<>();
        return coursesId;
    }

    public void setCoursesId(ArrayList<String> coursesId) {
        this.coursesId = coursesId;
    }

    public void setHeadDepartment() {
        headDepartment = role == Role.HeadDepartment;
    }

    public void setEducationalAssistant() {
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

    public static Professor getProfessor(String professorId) {
        for (Professor professor : Professor.getProfessors())
            if (professor.getId().equals(professorId))
                return professor;
        return null;
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
