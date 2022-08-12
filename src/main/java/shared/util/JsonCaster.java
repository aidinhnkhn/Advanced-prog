package shared.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import elements.courses.Course;
import elements.people.Professor;
import elements.people.Student;
import elements.request.*;
import elements.university.Department;
import shared.gsonSerializers.LocalDateTimeDeserializer;
import shared.gsonSerializers.LocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class JsonCaster {

    public static Professor professorCaster(String professorString){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(professorString, Professor.class);
    }
    public static Student studentCaster(String studentString){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(studentString, Student.class);
    }

    public static LocalDateTime dateCaster(String date){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(date,LocalDateTime.class);
    }

    public static ArrayList<Student> studentArrayListCaster(String data){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<Student>>(){}.getType());
    }

    public static ArrayList<Course> courseArrayListCaster(String data){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<Course>>(){}.getType());
    }

    public static ArrayList<Professor> professorArrayListCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<Professor>>(){}.getType());
    }
    public static String objectToJson(Object object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static Course courseCaster(String course) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(course,Course.class);
    }

    public static ArrayList<Department> departmentArrayListCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<Department>>(){}.getType());
    }
    public static ArrayList<String> StringArrayListCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<String>>(){}.getType());
    }
    public static Department departmentCaster(String department) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(department, Department.class);
    }

    public static ArrayList<RecommendationRequest> RecommendationArrayListCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<RecommendationRequest>>(){}.getType());
    }

    public static CertificateStudentRequest certificateCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        System.out.println("i am here");
        return gson.fromJson(data, CertificateStudentRequest.class);
    }

    public static ArrayList<FreedomRequest> freedomArrayListCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<FreedomRequest>>(){}.getType());
    }

    public static FreedomRequest freedomCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data, FreedomRequest.class);
    }

    public static ArrayList<MinorRequest> minorRequestArrayListCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<MinorRequest>>(){}.getType());
    }

    public static MinorRequest minorCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data, MinorRequest.class);
    }

    public static ArrayList<DormRequest> dormArrayListCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<DormRequest>>(){}.getType());
    }

    public static DormRequest DormCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data, DormRequest.class);
    }


    public static ArrayList<ThesisDefenseRequest> thesisArrayListCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data,new TypeToken<ArrayList<ThesisDefenseRequest>>(){}.getType());
    }

    public static ThesisDefenseRequest thesisCaster(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(data, ThesisDefenseRequest.class);
    }
}
