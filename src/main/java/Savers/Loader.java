package Savers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import elements.courses.Course;
import elements.people.Professor;
import elements.people.Student;
import elements.university.Department;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Loader {
    private static Loader load;

    private Loader() {

    }

    public static Loader getInstance() {
        if (load == null)
            load = new Loader();
        return load;
    }

    public void initializeEdu() {
        Department.setDepartments(Loader.getInstance().loadDepartments());
        Student.setStudents(Loader.getInstance().loadStudents());
        Professor.setProfessors(Loader.getInstance().loadProfessors());
//        File file = new File(System.getProperty("user.dir") +
//                "\\src\\main\\resources\\eData\\course\\C202242412954"+".txt");
//        file.delete();
        Course.setCourses(Loader.getInstance().loadCourses());
    }

    public void deleteFile(File file){
        file.delete();
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
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Student> loadStudents() {
        File studentDirectory = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\eData\\users\\students");
        ArrayList<Student> students = new ArrayList<>();
        for (File file : studentDirectory.listFiles())
            students.add(loadStudent(file));
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
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Professor> loadProfessors() {
        File studentDirectory = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\eData\\users\\professors");
        ArrayList<Professor> professors = new ArrayList<>();
        for (File file : studentDirectory.listFiles())
            professors.add(loadProfessor(file));

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
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Department> loadDepartments(){
        File studentDirectory = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\eData\\departments");
        ArrayList<Department> departments=new ArrayList<>();
        for (File file : studentDirectory.listFiles())
            departments.add(loadDepartment(file));
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
            Gson gson = gsonBuilder.create();
            Course course = gson.fromJson(userJson, Course.class);
            return course;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Course> loadCourses(){
        File studentDirectory = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\eData\\course");
        ArrayList<Course> courses=new ArrayList<>();
        for (File file:studentDirectory.listFiles())
            courses.add(loadCourse(file));
        return courses;
    }
}
