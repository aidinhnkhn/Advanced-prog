package elements.request;

import elements.people.Student;
import server.university.University;

import java.util.ArrayList;

public class FreedomRequest extends Request{
    private static ArrayList<FreedomRequest> freedomRequests=new ArrayList<>();
    public FreedomRequest(String studentId, String departmentId) {
        super(studentId, departmentId);
        this.acceptedText="Congrats you are free now!";
        this.deniedText="you are gonna be here for a while!";
        this.requestText="let me out please!";
        University.getInstance().getFreedomRequests().add(this);
    }

    public static ArrayList<FreedomRequest> getFreedomRequests() {
        return freedomRequests;
    }

    public static void setFreedomRequests(ArrayList<FreedomRequest> freedomRequests) {
        FreedomRequest.freedomRequests = freedomRequests;
    }

    @Override
    public void setAccepted(boolean accepted) {
        this.pending=false;
        this.accepted=accepted;
        if (accepted){
            University.getInstance().getStudentById(studentId).setEducating(false);
        }
    }

    @Override
    public boolean getTotalAccepted() {
        return !pending && this.accepted;
    }

    @Override
    protected String makeAcceptedText() {
        return null;
    }
}
