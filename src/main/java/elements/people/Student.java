package elements.people;

import elements.courses.Course;
import elements.courses.Grade;

import java.util.ArrayList;

public class Student extends User {
    public String supervisorId;
    private String minorDepartment;
    private String secondDepartment;
    private ArrayList<Grade> grades;
    private static ArrayList<Student> students = new ArrayList<>();

    public Student(String username, String password, Role role, String melicode, String phoneNumber, String email, String degree, String departmentId, String id) {
        super(username, password, role, melicode, phoneNumber, email, degree, departmentId, id);
        students.add(this);
        this.grades = new ArrayList<>();
    }


    public String getSupervisorId() {
        return supervisorId;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public String getMinorDepartment() {
        return minorDepartment;
    }

    public void setMinorDepartment(String minorDepartment) {
        this.minorDepartment = minorDepartment;
    }

    public String getSecondDepartment() {
        return secondDepartment;
    }

    public void setSecondDepartment(String secondDepartment) {
        this.secondDepartment = secondDepartment;
    }


    public static void setStudents(ArrayList<Student> students) {
        Student.students = students;
    }

    public static ArrayList<Student> getStudents() {
        return students;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public void addGrade(Grade grade) {
        this.grades.add(grade);
    }

    public static Student getStudent(String studentId) {
        for (Student student : Student.getStudents())
            if (student.getId().equals(studentId))
                return student;
        return null;
    }
}
