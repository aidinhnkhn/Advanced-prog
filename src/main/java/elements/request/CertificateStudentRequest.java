package elements.request;

import elements.people.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CertificateStudentRequest extends Request{
    private static ArrayList<CertificateStudentRequest> certificateStudentRequests=new ArrayList<>();

    public CertificateStudentRequest(String studentId, String departmentId) {
        super(studentId, departmentId);
        this.acceptedText=makeAcceptedText();
        this.deniedText="it seems this university doesn't want you to get a certificate.";
        this.requestText="Give this Student whatever he/she likes!(Certificate)";
        certificateStudentRequests.add(this);
        this.pending=false;
        this.accepted=true;
    }

    public static ArrayList<CertificateStudentRequest> getCertificateStudentRequests() {
        return certificateStudentRequests;
    }

    public static void setCertificateStudentRequests(ArrayList<CertificateStudentRequest> certificateStudentRequests) {
        CertificateStudentRequest.certificateStudentRequests = certificateStudentRequests;
    }

    @Override
    public boolean getTotalAccepted() {
        return !this.pending && this.accepted;
    }

    @Override
    protected String makeAcceptedText() {
        StringBuilder res=new StringBuilder("this student");
        res.append(Student.getStudent(studentId).getUsername());
        res.append("is educating in Sharif University of nothing in department of ");
        res.append(Student.getStudent(studentId).getDepartmentId());
        res.append("\n Expiration date: ");
        res.append((LocalDate.now().plusMonths(6)));
        return res.toString();
    }

    @Override
    public void setStatusText() {
        this.acceptedText=acceptedText+" :)";
    }
}
