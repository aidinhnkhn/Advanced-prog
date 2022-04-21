package elements.people;

public class User {
    protected String username;
    protected String password;
    protected int id;
    protected Role role;
    private static int id_cnt=0;

    public User(String username, String password,Role role) {
        this.username = username;
        this.password = password;
        this.role=role;
        this.id=id_cnt++;
    }
}
