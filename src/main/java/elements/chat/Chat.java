package elements.chat;


import elements.chat.pm.Pm;
import server.university.University;
import shared.util.Config;
import shared.util.ImageSender;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;


public class Chat {
    private ArrayList<Pm> messages;
    private String id;
    private String studentId1,studentName1;
    private String studentId2,studentName2;

    private String image1,image2;
    private boolean accepted;
    private LocalDateTime date;
    private String lastPm;

    private String name;
    public Chat(String studentId1,String studentName1,String studentId2,String studentName2){
        this.studentId1 = studentId1;
        this.studentName1 = studentName1;
        this.studentId2=studentId2;
        this.studentName2 = studentName2;
        setUser1Image();
        setUser2Image();
        accepted = true;
        messages = new ArrayList<>();
        this.id = createId();
        University.getInstance().getChats().add(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getLastPm() {
        return lastPm;
    }

    private void setUser2Image() {
        String filename = studentId2 + ".png";
        File file = new File(System.getProperty("user.dir") +
                Config.getConfig().getProperty(String.class,"userPicturePath")+"\\" + filename);
        String path = "";
        if (!file.exists())
            path = System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\every.png";
        else
            path = System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename;
        image2 = ImageSender.encode(path);
    }

    private void setUser1Image() {
        String filename = studentId1 + ".png";
        File file = new File(System.getProperty("user.dir") +
               Config.getConfig().getProperty(String.class,"userPicturePath")+"\\" + filename);
        String path = "";
        if (!file.exists())
            path = System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\every.png";
        else
            path = System.getProperty("user.dir") +
                    "\\src\\main\\resources\\eData\\users\\pictures\\" + filename;
        image1 = ImageSender.encode(path);
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
        this.lastPm = pm.getContent();
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

    public Pm getPmBYId(String id){
        for (Pm pm:messages)
            if (pm.getId().equals(id))
                return pm;
        return null;
    }

}
