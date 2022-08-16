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
import shared.gsonSerializers.LocalDateTimeSerializer;
import shared.util.Config;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Saver implements Runnable {
    private static Saver saveStudent;
    private static Logger log = LogManager.getLogger(Saver.class);

    private boolean running;

    private Saver() {
        running = true;
    }

    public static Saver getInstance() {
        if (saveStudent == null)
            saveStudent = new Saver();
        return saveStudent;
    }

    public void stop() {
        running = false;
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
            log.error("couldn't save the file!");
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
            log.error("couldn't save the file!");
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
            log.error("couldn't save the file!");
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
            log.error("couldn't save the file!");
        }
    }

    public void saveMinorRequest(MinorRequest minorRequest) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\minorRequest\\" + minorRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(minorRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            log.error("couldn't save the file!");
        }
    }

    public void saveDormRequest(DormRequest dormRequest) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\dormRequest\\" + dormRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(dormRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            log.error("couldn't save the file!");
        }
    }

    public void saveRecommendationRequest(RecommendationRequest recommendationRequest) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\recommendationRequest\\" + recommendationRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(recommendationRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            log.error("couldn't save the file!");
        }
    }

    public void saveFreedomRequest(FreedomRequest freedomRequest) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\freedomRequest\\" + freedomRequest.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(freedomRequest);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            log.error("couldn't save the file!");
        }
    }

    public void saveCertificateStudentRequest(CertificateStudentRequest certificateStudentRequest) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\certificateStudentRequest\\" + certificateStudentRequest.getId() + ".txt");
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
            log.error("couldn't save the file!");
        }
    }

    public void saveThesisDefenseRequest(ThesisDefenseRequest thesisDefenseRequest) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\request\\thesisDefenseRequest\\" + thesisDefenseRequest.getId() + ".txt");
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
            log.error("couldn't save the file!");
        }
    }
    private void saveChat(Chat chat) {
        File file = new File(System.getProperty("user.dir") +
                Config.getConfig().getProperty(String.class,"chatPath")+ "\\" + chat.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(chat);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            log.error("couldn't save the file!");
        }
    }

    private void saveDates(){
        File file = new File(System.getProperty("user.dir") +
                Config.getConfig().getProperty(String.class,"startPickingPath"));
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(University.getInstance().getStartPicking());
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            log.error("couldn't save the file!");
        }
        file = new File(System.getProperty("user.dir") +
                Config.getConfig().getProperty(String.class,"endPickingPath"));
        courseJson = gson.toJson(University.getInstance().getEndPicking());
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            log.error("couldn't save the file!");
        }
    }
    private void saveManager(Manager manager) {
        File file = new File(System.getProperty("user.dir") +
                Config.getConfig().getProperty(String.class,"managerPath")+ "\\" + manager.getId() + ".txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        String courseJson = gson.toJson(manager);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(courseJson);
            writer.close();
        } catch (IOException e) {
            log.error("couldn't save the file!");
        }
    }
    public void saveChanges() {
        for (Department department : University.getInstance().getDepartments())
            Saver.getInstance().saveDepartment(department);

        for (Student student : University.getInstance().getStudents())
            Saver.getInstance().saveStudent(student);

        for (Professor professor : University.getInstance().getProfessors())
            Saver.getInstance().saveProfessor(professor);

        for (Course course : University.getInstance().getCourses())
            Saver.getInstance().saveCourse(course);

        for (MinorRequest minorRequest : University.getInstance().getMinorRequests())
            Saver.getInstance().saveMinorRequest(minorRequest);

        for (DormRequest dormRequest : University.getInstance().getDormRequests())
            Saver.getInstance().saveDormRequest(dormRequest);

        for (RecommendationRequest recommendationRequest : University.getInstance().getRecommendationRequests())
            Saver.getInstance().saveRecommendationRequest(recommendationRequest);

        for (FreedomRequest freedomRequest : University.getInstance().getFreedomRequests())
            Saver.getInstance().saveFreedomRequest(freedomRequest);

        for (CertificateStudentRequest certificateStudentRequest : University.getInstance().getCertificateStudentRequests())
            Saver.getInstance().saveCertificateStudentRequest(certificateStudentRequest);

        for (ThesisDefenseRequest thesisDefenseRequest : University.getInstance().getThesisDefenseRequests())
            Saver.getInstance().saveThesisDefenseRequest(thesisDefenseRequest);

        for (Chat chat : University.getInstance().getChats())
            Saver.getInstance().saveChat(chat);

        for (Manager manager:University.getInstance().getManagers())
            Saver.getInstance().saveManager(manager);

        saveDates();
    }



    @Override
    public void run() {
        int cnt = 0;
        while (running) {
            saveChanges();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.fatal("Saver class can't save the changes");
                e.printStackTrace();
            }
        }
    }
    //TODO: config the addresses
}
