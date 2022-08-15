package elements.people;

import server.university.University;

public class Manager extends User{
    public Manager(String username, String password, Role role, String email, String id) {
        super(username, password, role, null, null, email, null, null, id);
        University.getInstance().getManagers().add(this);
    }
}
