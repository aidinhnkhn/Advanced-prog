package elements.courses;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Course {
    private String id,name;
    private String professorId;
    private ArrayList<String> studentId;
    private int unit;

    public Course(String name, String professorId, ArrayList<String> studentId, int unit) {
        this.name = name;
        this.professorId = professorId;
        this.studentId = studentId;
        this.unit = unit;
        this.id=createId();
    }
    private String createId(){
        StringBuilder idBuilder=new StringBuilder();
        LocalDateTime localDate = LocalDateTime.now();
        idBuilder.append(localDate.getYear());
        idBuilder.append(localDate.getMonthValue());
        idBuilder.append(localDate.getDayOfMonth());
        idBuilder.append(localDate.getHour());
        idBuilder.append(localDate.getMinute());
        idBuilder.append(localDate.getSecond());
        return idBuilder.toString();
    }
}
