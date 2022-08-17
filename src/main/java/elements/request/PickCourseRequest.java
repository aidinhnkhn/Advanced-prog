package elements.request;

import server.university.University;

public class PickCourseRequest extends Request{
    public PickCourseRequest(String studentId, String departmentId) {
        super(studentId, departmentId);
        this.acceptedText= departmentId+" was picked!";
        this.deniedText="better! "+departmentId+" wasn't picked";
        this.requestText="pick this course "+departmentId;
        University.getInstance().getPickCourseRequests().add(this);
    }

    @Override
    public boolean getTotalAccepted() {
        return !pending && this.accepted;
    }

    @Override
    protected String makeAcceptedText() {
       return departmentId+" was picked!";
    }

    @Override
    public void setAccepted(boolean accepted) {
        this.pending=false;
        this.accepted=accepted;
    }
}
