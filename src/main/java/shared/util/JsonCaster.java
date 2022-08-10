package shared.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import elements.courses.Course;
import elements.people.Professor;
import elements.people.Student;
import shared.gsonSerializers.LocalDateTimeDeserializer;
import shared.gsonSerializers.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class JsonCaster {

    public static Professor professorCaster(String professorString){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        Professor professor = gson.fromJson(professorString, Professor.class);
        return professor;
    }
    public static Student studentCaster(String studentString){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        Student student = gson.fromJson(studentString, Student.class);
        return student;
    }

    public static LocalDateTime dateCaster(String date){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        LocalDateTime localDate = gson.fromJson(date,LocalDateTime.class);
        return localDate;
    }

    public static ArrayList<Student> studentArrayListCaster(String data){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        ArrayList<Student> arrayList = gson.fromJson(data,new TypeToken<ArrayList<Student>>(){}.getType());
        return  arrayList;
    }

    public static ArrayList<Course> courseArrayListCaster(String data){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        ArrayList<Course> arrayList = gson.fromJson(data,new TypeToken<ArrayList<Course>>(){}.getType());
        return  arrayList;
    }

    public static ArrayList<Professor> professorArrayListCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        ArrayList<Professor> arrayList = gson.fromJson(data,new TypeToken<ArrayList<Professor>>(){}.getType());
        return  arrayList;
    }
    public static String objectToJson(Object object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        String objectJson = gson.toJson(object);
        return objectJson;
    }

    public static Course courseCaster(String course) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        Course course1 = gson.fromJson(course,Course.class);
        return course1;
    }


}
