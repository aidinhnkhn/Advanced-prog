package server.Savers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import elements.chat.Chat;
import elements.courses.Course;
import elements.people.Manager;
import elements.people.Professor;
import elements.people.Student;
import elements.request.*;
import elements.university.Department;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import server.university.University;
import shared.gsonSerializers.LocalDateTimeDeserializer;
import shared.util.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Loader {
    private static Loader load;
    private static Logger log = LogManager.getLogger(Loader.class);
    private Loader() {

    }

    public static Loader getInstance() {
        if (load == null)
            load = new Loader();
        return load;
    }

    public void initializeEdu() {

        University.getInstance().setDepartments(loadDepartments());
        University.getInstance().setStudents(loadStudents());
        University.getInstance().setProfessors(loadProfessors());
        University.getInstance().setCourses(loadCourses());
        University.getInstance().setMinorRequests(loadMinorRequests());
        University.getInstance().setDormRequests(loadDormRequests());
        University.getInstance().setRecommendationRequests(loadRecommendationRequests());
        University.getInstance().setFreedomRequests(loadFreedomRequests());
        University.getInstance().setCertificateStudentRequests(loadCertificates());
        University.getInstance().setThesisDefenseRequests(loadThesisDefenseRequests());
        University.getInstance().setChats(loadChats());
        University.getInstance().setManagers(loadManagers());
        //loadStartDate();
        //loadEndDate();
        log.info("edu Initialized");
    }

    private void loadEndDate() {
        try {
            File file = new File(System.getProperty("user.dir") +
                    Config.getConfig().getProperty(String.class, "endPickingPath"));
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            University.getInstance().setStartPicking(gson.fromJson(userJson, LocalDateTime.class));
        }catch (FileNotFoundException e){
            log.error("couldn't load the file");
        }
    }

    private void loadStartDate() {
        try {
            File file = new File(System.getProperty("user.dir") +
                    Config.getConfig().getProperty(String.class, "startPickingPath"));
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            University.getInstance().setStartPicking(gson.fromJson(userJson, LocalDateTime.class));
        }catch (FileNotFoundException e){
            log.error("couldn't load the file");
        }
    }

    private ArrayList<Chat> loadChats() {
        File chatDirectory = new File(System.getProperty("user.dir") +
                Config.getConfig().getProperty(String.class,"chatPath"));
        ArrayList<Chat> chats = new ArrayList<>();
        for (File file:chatDirectory.listFiles())
            chats.add(loadChat(file));
        log.info("chats loaded");
        return chats;
    }

    private Chat loadChat(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            return gson.fromJson(userJson, Chat.class);
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }

    private ArrayList<Manager> loadManagers() {
        File chatDirectory = new File(System.getProperty("user.dir") +
                Config.getConfig().getProperty(String.class,"managerPath"));
        ArrayList<Manager> managers = new ArrayList<>();
        for (File file:chatDirectory.listFiles())
            managers.add(loadManager(file));
        log.info("managers loaded");
        return managers;
    }
    private Manager loadManager(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            return gson.fromJson(userJson, Manager.class);
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }
    public Student loadStudent(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            Student student = gson.fromJson(userJson, Student.class);
            return student;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }

    public ArrayList<Student> loadStudents() {
        File studentDirectory = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\eData\\users\\students");
        ArrayList<Student> students = new ArrayList<>();
        for (File file : studentDirectory.listFiles())
            students.add(loadStudent(file));
        log.info("students loaded.");
        return students;
    }

    public Professor loadProfessor(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            Professor professor = gson.fromJson(userJson, Professor.class);
            return professor;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }
    public ArrayList<Professor> loadProfessors() {
        File studentDirectory = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\eData\\users\\professors");
        ArrayList<Professor> professors = new ArrayList<>();
        for (File file : studentDirectory.listFiles())
            professors.add(loadProfessor(file));
        log.info("professors loaded");
        return professors;
    }

    public Department loadDepartment(File file){
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            Department department = gson.fromJson(userJson, Department.class);
            return department;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }
    public ArrayList<Department> loadDepartments(){
        File studentDirectory = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\eData\\departments");
        ArrayList<Department> departments=new ArrayList<>();
        for (File file : studentDirectory.listFiles())
            departments.add(loadDepartment(file));
        log.info("professor loaded");
        return departments;
    }

    public Course loadCourse(File file){
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            Course course = gson.fromJson(userJson, Course.class);
            return course;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }
    public ArrayList<Course> loadCourses(){
        File studentDirectory = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\eData\\course");
        ArrayList<Course> courses=new ArrayList<>();
        for (File file:studentDirectory.listFiles())
            courses.add(loadCourse(file));
        log.info("courses loaded");
        return courses;
    }

    public MinorRequest loadMinorRequest(File file){
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            MinorRequest minorRequest = gson.fromJson(userJson, MinorRequest.class);
            return minorRequest;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }

    public ArrayList<MinorRequest> loadMinorRequests(){
        File minorDirectory = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\minorRequest");
        ArrayList<MinorRequest> minorRequests=new ArrayList<>();
        for (File file: Objects.requireNonNull(minorDirectory.listFiles()))
            minorRequests.add(loadMinorRequest(file));
        log.info("minor requests loaded");
        return  minorRequests;
    }

    public DormRequest loadDormRequest(File file){
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            DormRequest dormRequest = gson.fromJson(userJson, DormRequest.class);
            return dormRequest;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }

    public ArrayList<DormRequest> loadDormRequests(){
        File dormRequestDirectory = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\dormRequest");
        ArrayList<DormRequest> dormRequests=new ArrayList<>();
        for (File file: Objects.requireNonNull(dormRequestDirectory.listFiles()))
            dormRequests.add(loadDormRequest(file));
        log.info("dorm requests loaded");
        return dormRequests;
    }

    public RecommendationRequest loadRecommendationRequest(File file){
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            RecommendationRequest recommendationRequest = gson.fromJson(userJson, RecommendationRequest.class);
            return recommendationRequest;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }

    public ArrayList<RecommendationRequest> loadRecommendationRequests(){
        File recommendationRequestDirectory = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\recommendationRequest");
        ArrayList<RecommendationRequest> recommendationRequests=new ArrayList<>();
        for (File file: Objects.requireNonNull(recommendationRequestDirectory.listFiles()))
            recommendationRequests.add(loadRecommendationRequest(file));
        log.info("Recommendation requests loaded");
        return recommendationRequests;
    }

    public FreedomRequest loadFreedomRequest(File file){
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            FreedomRequest freedomRequest = gson.fromJson(userJson, FreedomRequest.class);
            return freedomRequest;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }

    public ArrayList<FreedomRequest> loadFreedomRequests(){
        File freeDomeRequestDirectory = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\freedomRequest");
        ArrayList<FreedomRequest> freedomRequests=new ArrayList<>();
        for (File file: Objects.requireNonNull(freeDomeRequestDirectory.listFiles()))
            freedomRequests.add(loadFreedomRequest(file));
        log.info("freedom requests loaded");
        return freedomRequests;
    }

    public CertificateStudentRequest loadCertificate(File file){
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            CertificateStudentRequest certificateStudentRequest = gson.fromJson(userJson, CertificateStudentRequest.class);
            return certificateStudentRequest;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }
    public ArrayList<CertificateStudentRequest> loadCertificates(){
        File certificateDirectory = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\certificateStudentRequest");
        ArrayList <CertificateStudentRequest> certificates=new ArrayList<>();
        for (File file: Objects.requireNonNull(certificateDirectory.listFiles()))
            certificates.add(loadCertificate(file));
        log.info("certificate requests loaded");
        return certificates;
    }

    public ThesisDefenseRequest loadThesisDefenseRequest(File file){
        try {
            Scanner scanner = new Scanner(file);
            String userJson = "";
            while (scanner.hasNext())
                userJson += scanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            Gson gson = gsonBuilder.create();
            ThesisDefenseRequest thesisDefenseRequest = gson.fromJson(userJson, ThesisDefenseRequest.class);
            return thesisDefenseRequest;
        } catch (FileNotFoundException e) {
            log.error("file not Found!");
        }
        return null;
    }

    public ArrayList<ThesisDefenseRequest> loadThesisDefenseRequests(){
        File thesisDirectory = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\thesisDefenseRequest");
        ArrayList<ThesisDefenseRequest> requests=new ArrayList<>();
        for (File file: Objects.requireNonNull(thesisDirectory.listFiles()))
            requests.add(loadThesisDefenseRequest(file));
        log.info("thesis requests loaded");
        return requests;
    }
}
