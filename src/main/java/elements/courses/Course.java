package elements.courses;

import elements.university.Department;
import javafx.scene.control.ComboBox;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Course {
    private String id, name;
    private String professorId,departmentId;
    private ArrayList<String> studentId=new ArrayList<>();
    private int unit;
    private static ArrayList<Course> Courses=new ArrayList<>();
    public Course(String name, String professorId, String departmentId, int unit) {
        this.name = name;
        this.professorId = professorId;
        this.departmentId=departmentId;
        this.unit = unit;
        this.id = createId();
        Courses.add(this);
        Department.getDepartment(departmentId).getCourses().add(this.id);
    }

    private String createId() {
        StringBuilder idBuilder = new StringBuilder();
        LocalDateTime localDate = LocalDateTime.now();
        idBuilder.append("C");
        idBuilder.append(localDate.getYear());
        idBuilder.append(localDate.getMonthValue());
        idBuilder.append(localDate.getDayOfMonth());
        idBuilder.append(localDate.getHour());
        idBuilder.append(localDate.getMinute());
        idBuilder.append(localDate.getSecond());
        return idBuilder.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public ArrayList<String> getStudentId() {
        return studentId;
    }

    public void setStudentId(ArrayList<String> studentId) {
        this.studentId = studentId;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public static ArrayList<Course> getCourses() {
        return Courses;
    }

    public static void setCourses(ArrayList<Course> courses) {
        Courses = courses;
    }
    public static Course getCourse(String id){
        for (Course course:Courses)
            if (course.id.equals(id))
                return course;
        return null;
    }
}
