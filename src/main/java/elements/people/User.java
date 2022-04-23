package elements.people;

import javafx.scene.image.Image;
import java.time.*;
import java.time.DayOfWeek;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class User {
    protected String username;
    protected String password;
    protected String id,melicode,phoneNumber,email,degree,departmentId;
    protected Role role;
    protected LocalDateTime lastEnter;
    public User(String username, String password,Role role,String melicode,String phoneNumber,String email,String degree,String departmentId,String id) {
        this.username = username;
        this.password = password;
        this.role=role;
        this.melicode=melicode;
        this.phoneNumber=phoneNumber;
        this.email=email;
        this.degree=degree;
        this.departmentId=departmentId;
        if (id.equals("nothing"))
            this.id=this.createId();
        else
            this.id=id;
        lastEnter=LocalDateTime.now();
    }
    private String createId(){
        StringBuilder idBuilder=new StringBuilder();
        LocalDateTime localDate = LocalDateTime.now();
        idBuilder.append(getRoleid());
        idBuilder.append(localDate.getYear()%100);
        idBuilder.append(localDate.getMonthValue());
        idBuilder.append(localDate.getDayOfMonth());
        idBuilder.append(localDate.getHour());
        idBuilder.append(localDate.getMinute());
        idBuilder.append(localDate.getSecond());
        return idBuilder.toString();
    }
    private String getRoleid(){
        if (this.role==Role.Student) return "s";
        else return "p";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getMelicode() {
        return melicode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLastEnter() {
        return lastEnter;
    }

    public void setLastEnter(LocalDateTime lastEnter) {
        this.lastEnter = lastEnter;
    }

    public String getDegree() {
        return degree;
    }

    public Role getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(lastEnter);
    }
}
