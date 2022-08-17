package elements.courses;

import elements.people.Professor;
import server.university.University;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Course {
    private String id, name;
    private String professorId,departmentId;

    private ArrayList<String> taIds=new ArrayList<>();
    private ArrayList<String> studentId=new ArrayList<>();
    private int unit;
    private ArrayList<String> days= new ArrayList<>();
    private int hour;
    private int length;
    private int studentNumber;
    private LocalDateTime examDate;
    private String professorName;
    private String degree;

    private String nowCourse;
    private String previousCourse;
    private boolean finished;
    public Course(String name, String professorId, String departmentId, int unit, ArrayList<String> days,
                  int hour, int length, LocalDateTime examDate, String degree, ArrayList<String> taIds,
                  String courseId, String previousCourse, String nowCourse, int studentNumber) {
        this.name = name;
        this.professorId = professorId;
        this.departmentId=departmentId;
        this.unit = unit;
        if (courseId.equals(""))
            this.id = createId();
        else
            this.id = 'C'+courseId;
        this.days=days;
        this.hour=hour;
        this.length=length;
        this.examDate=examDate;
        this.degree=degree;
        this.finished=false;
        this.taIds = taIds;
        this.previousCourse = previousCourse;
        this.nowCourse = nowCourse;
        this.studentNumber = studentNumber;
        University.getInstance().getCourses().add(this);
        University.getInstance().getDepartmentById(departmentId).getCourses().add(this.id);
        University.getInstance().getProfessorById(professorId).getCoursesId().add(this.id);
        professorName= University.getInstance().getProfessorById(professorId).getUsername();
    }

    public ArrayList<String> getTaIds() {
        if (this.taIds == null) taIds = new ArrayList<>();
        return taIds;
    }

    public void setTaIds(ArrayList<String> taIds) {
        this.taIds = taIds;
    }

    public String getNowCourse() {
        return nowCourse;
    }

    public void setNowCourse(String nowCourse) {
        this.nowCourse = nowCourse;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPreviousCourse() {
        return previousCourse;
    }

    public void setPreviousCourse(String previousCourse) {
        this.previousCourse = previousCourse;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
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
        University.getInstance().getProfessorById(this.professorId).getCoursesId().remove(this.id);
        this.professorId = professorId;
        University.getInstance().getProfessorById(this.professorId).getCoursesId().add(this.id);
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


    public void setProfessorIdInClient(String professorId) {
        this.professorId = professorId;
    }
}
