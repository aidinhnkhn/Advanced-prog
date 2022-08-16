package server.university;

import elements.chat.Chat;
import elements.courses.Course;
import elements.people.Manager;
import elements.people.Professor;
import elements.people.Student;
import elements.request.*;
import elements.university.Department;
import shared.util.JsonCaster;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class University {
    private static University university;

    private ArrayList<Manager> managers=new ArrayList<>();
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

    private LocalDateTime startPicking;
    private LocalDateTime endPicking;
    private ArrayList<Chat> chats = new ArrayList<>();
    private University(){

    }

    public LocalDateTime getStartPicking() {
        return startPicking;
    }

    public void setStartPicking(LocalDateTime startPicking) {
        this.startPicking = startPicking;
    }

    public LocalDateTime getEndPicking() {
        return endPicking;
    }

    public void setEndPicking(LocalDateTime endPicking) {
        this.endPicking = endPicking;
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }

    public void setManagers(ArrayList<Manager> managers) {
        this.managers = managers;
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

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
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

    public Course getCourseById(String id){
        for (Course course:Courses)
            if (course.getId().equals(id))
                return course;
        return null;
    }

    public Department getDepartmentById(String id) {
        for (Department department:departments)
            if (department.getName().equals(id))
                return department;
        return null;
    }

    public MinorRequest getMinorById(String id){
        for (MinorRequest minorRequest : minorRequests)
            if (minorRequest.getId().equals(id))
                return minorRequest;
        return null;
    }

    public RecommendationRequest getRecommendationById(String id){
        for (RecommendationRequest recommendationRequest : recommendationRequests)
            if (recommendationRequest.getId().equals(id))
                return recommendationRequest;
        return null;
    }

    public FreedomRequest getFreedomById(String id){
        for (FreedomRequest freedomRequest : freedomRequests)
            if (freedomRequest.getId().equals(id))
                return freedomRequest;
        return null;
    }

    public Chat getChatById(String id){
        for (Chat chat : chats)
            if (chat.getId().equals(id))
                return chat;
        return null;
    }

    public String getUsername(String id){
        String username;
        Student student = getStudentById(id);
        if (student != null) return student.getUsername();
        Professor professor = getProfessorById(id);
        if (professor != null) return professor.getUsername();
        return getAdmin().getUsername();
    }
    public String getUserChats(String id) {
        ArrayList<Chat> sendChat = new ArrayList<>();
        for (Chat chat:this.chats)
            if (chat.getStudentId1().equals(id) || chat.getStudentId2().equals(id))
                sendChat.add(chat);
        return JsonCaster.objectToJson(sendChat);
    }

    public Manager getManagerById(String id){
        for (Manager manager:managers)
            if (manager.getId().equals(id))
                return manager;
        return null;
    }
    public Manager getAdmin(){
        for (Manager manager:managers)
            if (manager.getId().equals("admin"))
                return manager;
        return null;
    }

    public Manager getMohseni(){
        for (Manager manager:managers)
            if (manager.getId().equals("mohseni"))
                return manager;
        return null;
    }
}
