package elements.request;

import elements.people.Student;

import java.util.ArrayList;

public class MinorRequest extends Request{
    private String secondDepartmentId;
    private boolean secondAccepted;
    private static ArrayList<MinorRequest> minorRequests=new ArrayList<>();

    public MinorRequest(String studentId, String departmentId, String acceptedText, String deniedText, String requestText, String secondDepartmentId) {
        super(studentId, departmentId);
        this.acceptedText=makeAcceptedText();
        this.requestText="please Accept me I'm a nerd and want to study more";
        this.deniedText="You are lucky to be REJECTED!";
        this.secondDepartmentId = secondDepartmentId;
        this.secondAccepted=false;
        minorRequests.add(this);
    }

    public String getSecondDepartmentId() {
        return secondDepartmentId;
    }

    public void setSecondDepartmentId(String secondDepartmentId) {
        this.secondDepartmentId = secondDepartmentId;
    }

    public boolean isSecondAccepted() {
        return secondAccepted;
    }

    public void setSecondAccepted(boolean secondAccepted) {
        this.secondAccepted = secondAccepted;
        if (!secondAccepted)
            pending=false;
    }

    public static ArrayList<MinorRequest> getMinorRequests() {
        return minorRequests;
    }

    public static void setMinorRequests(ArrayList<MinorRequest> minorRequests) {
        MinorRequest.minorRequests = minorRequests;
    }

    @Override
    public void setAccepted(boolean accepted) {
        this.accepted=accepted;
        if (!accepted)
            pending=false;
    }

    @Override
    public boolean getTotalAccepted() {
        if (!this.pending)
            if (this.accepted && this.secondAccepted) {
                Student.getStudent(studentId).setMinorDepartment(departmentId);
                return true;
            }
        return false;
    }

    @Override
    protected String makeAcceptedText() {
        return "God bless your soul! you have to work hard more, is that what you wanted?";
    }

}
