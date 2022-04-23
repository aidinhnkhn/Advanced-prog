package Savers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import elements.people.Professor;
import elements.people.Student;

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
                "\\src\\main\\resources\\eData\\users\\students\\" + professor.getId() + ".txt");
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
}
