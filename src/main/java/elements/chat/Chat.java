package elements.chat;


import elements.chat.pm.Pm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;


public class Chat {
    private ArrayList<Pm> messages;
    private String id;
    private String studentId1,studentName1;
    private String studentId2,studentName2;
    private boolean accepted;
    private LocalDateTime date;

    public Chat(String studentId1,String studentName1){
        this.studentId1 = studentId1;
        this.studentName1 = studentName1;
        accepted = false;
        messages = new ArrayList<>();
        this.id = createId();
    }
    private String createId(){
        StringBuilder idBuilder=new StringBuilder();
        LocalDateTime localDate = LocalDateTime.now();
        idBuilder.append("Chat");
        idBuilder.append(localDate.getYear()%100);
        idBuilder.append(localDate.getMonthValue());
        idBuilder.append(localDate.getDayOfMonth());
        idBuilder.append(localDate.getHour());
        idBuilder.append(localDate.getMinute());
        idBuilder.append(localDate.getSecond());
        return idBuilder.toString();
    }

    public ArrayList<Pm> getMessages() {
        return messages;
    }

    public String getId() {
        return id;
    }

    public String getStudentId1() {
        return studentId1;
    }

    public String getStudentName1() {
        return studentName1;
    }

    public String getStudentId2() {
        return studentId2;
    }

    public String getStudentName2() {
        return studentName2;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setStudentId2(String studentId2) {
        this.studentId2 = studentId2;
    }

    public void setStudentName2(String studentName2) {
        this.studentName2 = studentName2;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void addPm(Pm pm){
        this.messages.add(pm);
        this.date = pm.getDate();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void sortArrayList(){
        messages.sort(Comparator.comparing(Pm::getDate));
    }

}
