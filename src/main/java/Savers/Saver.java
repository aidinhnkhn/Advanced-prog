package Savers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import elements.courses.Course;
import elements.people.Professor;
import elements.people.Student;
import elements.request.*;
import elements.university.Department;

import javax.xml.parsers.SAXParser;
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

    public void saveDepartment(Department department) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\departments\\" + department.getName() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String departmentJson = gson.toJson(department);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(departmentJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCourse(Course course) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\course\\" + course.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(course);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMinorRequest(MinorRequest minorRequest) {
        File file = new File(System.getProperty("user.dir") +
                "src\\main\\resources\\eData\\request\\minorRequest" + minorRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(minorRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDormRequest(DormRequest dormRequest) {
        File file = new File(System.getProperty("user.dir") +
                "src\\main\\resources\\eData\\request\\dormRequest" + dormRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(dormRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRecommendationRequest(RecommendationRequest recommendationRequest){
        File file = new File(System.getProperty("user.dir") +
                "src\\main\\resources\\eData\\request\\recommendationRequest" + recommendationRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(recommendationRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFreedomRequest(FreedomRequest freedomRequest){
        File file = new File(System.getProperty("user.dir") +
                "src\\main\\resources\\eData\\request\\freedomRequest" + freedomRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(freedomRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCertificateStudentRequest(CertificateStudentRequest certificateStudentRequest){
        File file = new File(System.getProperty("user.dir") +
                "src\\main\\resources\\eData\\request\\certificateStudentRequest" + certificateStudentRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(certificateStudentRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveThesisDefenseRequest(ThesisDefenseRequest thesisDefenseRequest){
        File file = new File(System.getProperty("user.dir") +
                "src\\main\\resources\\eData\\request\\thesisDefenseRequest" + thesisDefenseRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(thesisDefenseRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveChanges() {
        //TODO: add changes to this
        for (Department department : Department.getDepartments())
            Saver.getInstance().saveDepartment(department);

        for (Student student : Student.getStudents())
            Saver.getInstance().saveStudent(student);

        for (Professor professor : Professor.getProfessors())
            Saver.getInstance().saveProfessor(professor);

        for (Course course : Course.getCourses())
            Saver.getInstance().saveCourse(course);

        for (MinorRequest minorRequest : MinorRequest.getMinorRequests())
            Saver.getInstance().saveMinorRequest(minorRequest);

        for (DormRequest dormRequest : DormRequest.getDormRequests())
            Saver.getInstance().saveDormRequest(dormRequest);

        for (RecommendationRequest recommendationRequest:RecommendationRequest.getRecommendationRequests())
            Saver.getInstance().saveRecommendationRequest(recommendationRequest);

        for (FreedomRequest freedomRequest:FreedomRequest.getFreedomRequests())
            Saver.getInstance().saveFreedomRequest(freedomRequest);

        for (CertificateStudentRequest certificateStudentRequest:CertificateStudentRequest.getCertificateStudentRequests())
            Saver.getInstance().saveCertificateStudentRequest(certificateStudentRequest);

        for (ThesisDefenseRequest thesisDefenseRequest:ThesisDefenseRequest.getThesisDefenseRequests())
            Saver.getInstance().saveThesisDefenseRequest(thesisDefenseRequest);

    }

}
