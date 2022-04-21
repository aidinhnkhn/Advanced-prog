package elements.people;

import javafx.scene.image.Image;

import java.time.Year;

public class User {
    protected String username;
    protected String password;
    protected String id,melicode,phoneNumber,email,degree,departmentId;
    protected Role role;
    private static int id_cnt=0;
    public User(String username, String password,Role role,String melicode,String phoneNumber,String email,String degree,String departmentId) {
        this.username = username;
        this.password = password;
        this.role=role;
        this.melicode=melicode;
        this.phoneNumber=phoneNumber;
        this.email=email;
        this.degree=degree;
        this.departmentId=departmentId;
        this.id=this.createId();
    }
    private String createId(){
        StringBuilder idBuilder=new StringBuilder();
        Year year=Year.now();
        idBuilder.append(getRoleid());
        idBuilder.append(year.toString());
        idBuilder.append(id_cnt++);
        return idBuilder.toString();
    }
    private String getRoleid(){
        if (this.role==Role.Student) return "1";
        else return "2";
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
}
