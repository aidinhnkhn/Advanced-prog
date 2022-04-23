package logic;

import elements.people.User;

public class LogicalAgent {
    private static LogicalAgent logicalAgent;
    private User user;
    private LogicalAgent(){

    }
    public static LogicalAgent getInstance(){
        if (logicalAgent==null)
            logicalAgent=new LogicalAgent();
        return logicalAgent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
