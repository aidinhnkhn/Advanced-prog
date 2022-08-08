package server.university;

import elements.courses.Course;
import elements.people.Professor;
import elements.people.Student;
import elements.request.*;
import elements.university.Department;

import java.util.ArrayList;

public class University {
    private static University university;

    private ArrayList<Department> departments=new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Professor> professors = new ArrayList<>();
    private ArrayList<Course> Courses=new ArrayList<>();
    private ArrayList<MinorRequest> minorRequests=new ArrayList<>();
    private ArrayList<DormRequest> dormRequests=new ArrayList<>();
    private ArrayList<RecommendationRequest> recommendationRequests=new ArrayList<>();
    private ArrayList<FreedomRequest> freedomRequests=new ArrayList<>();
    private ArrayList<CertificateStudentRequest> certificateStudentRequests=new ArrayList<>();
    private ArrayList<ThesisDefenseRequest> thesisDefenseRequests=new ArrayList<>();
    private University(){

    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(ArrayList<Professor> professors) {
        this.professors = professors;
    }

    public ArrayList<Course> getCourses() {
        return Courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        Courses = courses;
    }

    public ArrayList<MinorRequest> getMinorRequests() {
        return minorRequests;
    }

    public void setMinorRequests(ArrayList<MinorRequest> minorRequests) {
        this.minorRequests = minorRequests;
    }

    public ArrayList<DormRequest> getDormRequests() {
        return dormRequests;
    }

    public void setDormRequests(ArrayList<DormRequest> dormRequests) {
        this.dormRequests = dormRequests;
    }

    public ArrayList<RecommendationRequest> getRecommendationRequests() {
        return recommendationRequests;
    }

    public void setRecommendationRequests(ArrayList<RecommendationRequest> recommendationRequests) {
        this.recommendationRequests = recommendationRequests;
    }

    public ArrayList<FreedomRequest> getFreedomRequests() {
        return freedomRequests;
    }

    public void setFreedomRequests(ArrayList<FreedomRequest> freedomRequests) {
        this.freedomRequests = freedomRequests;
    }

    public ArrayList<CertificateStudentRequest> getCertificateStudentRequests() {
        return certificateStudentRequests;
    }

    public void setCertificateStudentRequests(ArrayList<CertificateStudentRequest> certificateStudentRequests) {
        this.certificateStudentRequests = certificateStudentRequests;
    }

    public ArrayList<ThesisDefenseRequest> getThesisDefenseRequests() {
        return thesisDefenseRequests;
    }

    public void setThesisDefenseRequests(ArrayList<ThesisDefenseRequest> thesisDefenseRequests) {
        this.thesisDefenseRequests = thesisDefenseRequests;
    }

    public static University getInstance(){
        if (university==null)
            university=new University();
        return university;
    }

    public Student getStudentById(String id){
        for (Student student:students)
            if (student.getId().equals(id))
                return student;
        return null;
    }

    public Professor getProfessorById(String id){
        for (Professor professor:professors)
            if (professor.getId().equals(id))
                return professor;
        return null;
    }

}
