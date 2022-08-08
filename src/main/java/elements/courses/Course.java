package elements.courses;

import elements.people.Professor;
import elements.university.Department;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Course {
    private String id, name;
    private String professorId,departmentId;
    private ArrayList<String> studentId=new ArrayList<>();
    private int unit;
    private ArrayList<String> days= new ArrayList<>();
    private int hour;
    private int length;
    private static ArrayList<Course> Courses=new ArrayList<>();
    private LocalDateTime examDate;
    private String professorName;
    private String degree;
    public Course(String name, String professorId, String departmentId, int unit, ArrayList<String> days,
                  int hour, int length, LocalDate examDate, int examHour,String degree) {
        this.name = name;
        this.professorId = professorId;
        this.departmentId=departmentId;
        this.unit = unit;
        this.id = createId();
        this.days=days;
        this.hour=hour;
        this.length=length;
        this.examDate=examDate.atTime(examHour,0);
        this.degree=degree;
        // TODO: fix these lines
        Courses.add(this);
        Department.getDepartment(departmentId).getCourses().add(this.id);
        Professor.getProfessor(professorId).getCoursesId().add(this.id);
        professorName=Professor.getProfessor(professorId).getUsername();
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
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

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName() {
        this.professorName = Professor.getProfessor(professorId).getUsername();
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate,int examHour) {
        this.examDate = examDate.atTime(examHour,0);
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setLength(int length) {
        this.length = length;
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
        Professor.getProfessor(this.professorId).getCoursesId().remove(this.id);
        this.professorId = professorId;
        Professor.getProfessor(this.professorId).getCoursesId().add(this.id);
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public int getHour() {
        return hour;
    }

    public int getLength() {
        return length;
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
    public static void deleteCourse(String id){
        Course.getCourses().remove(Course.getCourse(id));
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\course\\"+id+".txt");
        file.delete();
    }
}
