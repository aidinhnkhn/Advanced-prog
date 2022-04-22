package elements.university;

import elements.people.Professor;
import elements.people.Student;

import java.util.ArrayList;

public class Department {
    private String name;
    private static ArrayList<Department> departments=new ArrayList<>();
    private ArrayList<String> students;
    private ArrayList<String> professors;
    private ArrayList<String> courses;
    private String headDepartmentId;
    private String educationalAssistantId;

    public Department(String name, String headDepartmentId, String educationalAssistantId) {
        this.name = name;
        this.headDepartmentId = headDepartmentId;
        this.educationalAssistantId = educationalAssistantId;
    }

    public String getName() {
        return name;
    }


    public static ArrayList<Department> getDepartments() {
        return departments;
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public ArrayList<String> getProfessors() {
        return professors;
    }

    public String getHeadDepartmentId() {
        return headDepartmentId;
    }

    public String getEducationalAssistantId() {
        return educationalAssistantId;
    }

    public static Department getDepartmentByName(String name){
        for (Department department:Department.getDepartments())
            if (department.getName().equals(name))
                return department;
        return null;
    }
}
