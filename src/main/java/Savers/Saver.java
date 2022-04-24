package Savers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import elements.courses.Course;
import elements.people.Professor;
import elements.people.Student;
import elements.university.Department;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Saver {
    private static Saver saveStudent;

    private Saver() {

    }

    public static Saver getInstance() {
        if (saveStudent == null)
            saveStudent = new Saver();
        return saveStudent;
    }

    public void saveStudent(Student student) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\users\\students\\" + student.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String userJson = gson.toJson(student);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(userJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProfessor(Professor professor) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\users\\professors\\" + professor.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String userJson = gson.toJson(professor);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(userJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDepartment(Department department){
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\departments\\"+department.getName()+".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String departmentJson=gson.toJson(department);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(departmentJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveCourse(Course course){
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\course\\"+course.getId()+".txt");
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson=gsonBuilder.create();
        String courseJson=gson.toJson(course);
        try{
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void saveChanges(){
        for (Department department:Department.getDepartments())
            Saver.getInstance().saveDepartment(department);
        for (Student student:Student.getStudents())
            Saver.getInstance().saveStudent(student);
        for (Professor professor:Professor.getProfessors())
            Saver.getInstance().saveProfessor(professor);
        for (Course course:Course.getCourses())
            Saver.getInstance().saveCourse(course);
    }

}
