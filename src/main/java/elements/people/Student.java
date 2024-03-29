package elements.people;

import elements.courses.Course;
import elements.courses.Grade;
import elements.university.Department;
import server.university.University;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Student extends User {
    public String supervisorId;
    private String minorDepartment;
    private String secondDepartment;
    private ArrayList<Grade> grades;
    private ArrayList<String> favoriteCourse;
    private LocalDateTime enrollDate;
    private boolean enrollPermission;
    private static ArrayList<Student> students = new ArrayList<>();
    private boolean educating;
    public Student(String username, String password, Role role, String melicode, String phoneNumber, String email, String degree, String departmentId, String id,String supervisorId) {
        super(username, password, role, melicode, phoneNumber, email, degree, departmentId, id);
        students.add(this);
        this.grades = new ArrayList<>();
        this.educating=true;
        this.supervisorId=supervisorId;
        this.enrollPermission=true;
        favoriteCourse = new ArrayList<>();
        University.getInstance().getDepartmentById(this.departmentId).getStudents().add(this.id);
        University.getInstance().getStudents().add(this);
    }

    public ArrayList<String> getFavoriteCourse() {
        if (favoriteCourse == null) this.favoriteCourse = new ArrayList<>();
        return favoriteCourse;
    }

    public void setFavoriteCourse(ArrayList<String> favoriteCourse) {
        this.favoriteCourse = favoriteCourse;
    }

    public LocalDateTime getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(LocalDateTime enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getEnterYear(){
        return this.id.substring(1,3);
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

    public Grade getGrade(String id) {
        for (Grade grade : this.grades)
            if (grade.getCourseId().equals(id) && !grade.isFinalGrade())
                return grade;
        return null;
    }

    public Grade getAnyGrade(String id) {
        for (Grade grade : this.grades)
            if (grade.getCourseId().equals(id))
                return grade;
        return null;
    }
    public static Student getStudent(String studentId) {
        for (Student student : Student.getStudents())
            if (student.getId().equals(studentId))
                return student;
        return null;
    }
    public ArrayList <String> getStudentCoursesWithProfessor(String professorId){
        ArrayList<String> coursesWithProfessor=new ArrayList<>();
        for (Grade grade:this.grades){
            if (!grade.getProfessorId().equals(professorId)) continue;
            StringBuilder courseResult=new StringBuilder();
            courseResult.append(grade.getName()).append(": ");
            courseResult.append(grade.getGrade());
            coursesWithProfessor.add(courseResult.toString());
        }
        return coursesWithProfessor;
    }
    public double getَAverage(){
        double units=0;
        double grades=0;
        for (Grade grade:this.grades){
            if (!grade.isFinalGrade())
                continue;
            units+=grade.getUnit();
            grades+=grade.getGrade()*grade.getUnit();
        }
        if (units!=0)
            return grades/units;
        else
            return 0;
    }
    public Grade getFinalGrades(String id){
        for (Grade grade : this.grades)
            if (grade.getCourseId().equals(id) && grade.isFinalGrade())
                return grade;
        return null;
    }
    public boolean hasFavorite(String courseId){
        for (String course:getFavoriteCourse())
            if (course.equals(courseId))
                return true;
        return false;
    }
    @Override
    public String toString() {
        return this.getUsername()+" "+this.getId();
    }
}
