package elements.request;

import elements.people.Student;
import server.university.University;

import java.util.ArrayList;

public class MinorRequest extends Request{
    private String secondDepartmentId;
    private boolean secondAccepted;
    private static ArrayList<MinorRequest> minorRequests=new ArrayList<>();

    public MinorRequest(String studentId, String departmentId, String secondDepartmentId) {
        super(studentId, departmentId);
        this.acceptedText=makeAcceptedText();
        this.requestText="please Accept me I'm a nerd and want to study more";
        this.deniedText="Rejected";
        this.secondDepartmentId = secondDepartmentId;
        this.secondAccepted=false;

        University.getInstance().getMinorRequests().add(this);
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
        if (this.secondAccepted && this.accepted)
            pending=false;
        if (getTotalAccepted())
            University.getInstance().getStudentById(studentId).setMinorDepartment(secondDepartmentId);
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
        if (this.secondAccepted && this.accepted)
            pending=false;
        if (getTotalAccepted())
            University.getInstance().getStudentById(studentId).setMinorDepartment(secondDepartmentId);
    }

    @Override
    public boolean getTotalAccepted() {
        if (!this.pending)
            if (this.accepted && this.secondAccepted) {
                return true;
            }
        return false;
    }

    @Override
    protected String makeAcceptedText() {
        return "Approved";
    }

}
