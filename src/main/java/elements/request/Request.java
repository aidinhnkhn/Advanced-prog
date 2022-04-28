package elements.request;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Request {
    protected String studentId;
    protected String departmentId;
    protected String acceptedText,deniedText;
    protected String requestText,statusText;
    protected boolean pending,accepted;
    protected String id;

    public Request(String studentId, String departmentId) {
        this.studentId = studentId;
        this.departmentId = departmentId;
        this.pending = true;
        this.accepted = false;
        this.id=createId();
    }
    private String createId(){
        StringBuilder idBuilder=new StringBuilder();
        LocalDateTime localDate = LocalDateTime.now();
        idBuilder.append("R");
        idBuilder.append(localDate.getYear());
        idBuilder.append(localDate.getMonthValue());
        idBuilder.append(localDate.getDayOfMonth());
        idBuilder.append(localDate.getHour());
        idBuilder.append(localDate.getMinute());
        idBuilder.append(localDate.getSecond());
        return idBuilder.toString();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getAcceptedText() {
        return acceptedText;
    }

    public void setAcceptedText(String acceptedText) {
        this.acceptedText = acceptedText;
    }

    public String getDeniedText() {
        return deniedText;
    }

    public void setDeniedText(String deniedText) {
        this.deniedText = deniedText;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public abstract boolean getTotalAccepted();
    protected abstract String makeAcceptedText();
    public  void setStatusText(){
        if (this.pending)
            this.statusText="pending";
        else if (getTotalAccepted())
            this.statusText="accepted";
        else
            this.statusText="rejected";
    }

    public String getStatusText() {
        return statusText;
    }

    public void setAccepted(boolean accepted) {
        this.pending=false;
        this.accepted = accepted;
    }

    public String getId() {
        return id;
    }
}
