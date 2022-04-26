package elements.request;

import elements.people.Professor;
import elements.people.Student;

import java.util.ArrayList;

public class RecommendationRequest extends Request{
    private static ArrayList<RecommendationRequest> recommendationRequests=new ArrayList<>();
    public RecommendationRequest(String studentId, String departmentId) {
        super(studentId, departmentId);
        this.requestText="plase let me out of this country!";
        this.acceptedText=makeAcceptedText();
        this.deniedText="oh man you're stuck here with me!";
        recommendationRequests.add(this);
    }

    public static ArrayList<RecommendationRequest> getRecommendationRequests() {
        return recommendationRequests;
    }

    public static void setRecommendationRequests(ArrayList<RecommendationRequest> recommendationRequests) {
        RecommendationRequest.recommendationRequests = recommendationRequests;
    }

    @Override
    public boolean getTotalAccepted() {
            return !pending && accepted;
    }
    @Override
    protected String makeAcceptedText(){
        StringBuilder res=new StringBuilder("I, Dr.");
        res.append(Professor.getProfessor(departmentId).getUsername());
        res.append("recommend student");
        res.append(Student.getStudent(studentId).getUsername()).append("with student Id:");
        res.append(Student.getStudent(studentId).getId()+"has been a hard worker,punctual and determined student.\n");
        res.append("this student has also had this courses with me:\n");
        res.append(Student.getStudent(studentId).getStudentCoursesWithProfessor(departmentId));
        return res.toString();
    }
}
