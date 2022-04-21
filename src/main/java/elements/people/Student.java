package elements.people;

import java.util.ArrayList;

public class Student extends User{
    private int score;
    private String supervisorId;
    private static ArrayList<Student> students=new ArrayList<>();
    public Student(String username, String password, Role role, String melicode, String phoneNumber, String email, String degree,String departmentId) {
        super(username, password, role, melicode, phoneNumber, email, degree,departmentId);
        this.score=0;
        students.add(this);
    }

    public int getScore() {
        return score;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public static ArrayList<Student> getStudents() {
        return students;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }
    public static Student getStudent(String studentId) {
        for (Student student : Student.getStudents())
            if (student.getId().equals(studentId))
                return student;
        return null;
    }
}
