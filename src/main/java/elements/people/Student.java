package elements.people;

import elements.courses.Grade;

import java.util.ArrayList;

public class Student extends User {
    public String supervisorId;
    private String minorDepartment;
    private String secondDepartment;
    private ArrayList<Grade> grades;
    private String enrollHour;
    private boolean enrollPermission;
    private static ArrayList<Student> students = new ArrayList<>();
    private boolean educating;
    public Student(String username, String password, Role role, String melicode, String phoneNumber, String email, String degree, String departmentId, String id,String supervisorId) {
        super(username, password, role, melicode, phoneNumber, email, degree, departmentId, id);
        students.add(this);
        this.grades = new ArrayList<>();
        this.educating=true;
        this.supervisorId=supervisorId;
        this.enrollHour="8:00";
        this.enrollPermission=true;
    }

    public String getEnrollHour() {
        return enrollHour;
    }

    public void setEnrollHour(String enrollHour) {
        this.enrollHour = enrollHour;
    }

    public boolean isEnrollPermission() {
        return enrollPermission;
    }

    public void setEnrollPermission(boolean enrollPermission) {
        this.enrollPermission = enrollPermission;
    }

    public boolean isEducating() {
        return educating;
    }

    public void setEducating(boolean educating) {
        this.educating = educating;
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
