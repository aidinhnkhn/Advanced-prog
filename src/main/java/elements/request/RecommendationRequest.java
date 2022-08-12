package elements.request;

import elements.people.Professor;
import elements.people.Student;
import server.university.University;

import java.util.ArrayList;

public class RecommendationRequest extends Request{
    private static ArrayList<RecommendationRequest> recommendationRequests=new ArrayList<>();
    private String professorName;
    public RecommendationRequest(String studentId, String departmentId) {
        super(studentId, departmentId);
        this.requestText="plase let me out of this country!";
        this.acceptedText=makeAcceptedText();
        this.deniedText="oh man you're stuck here with me!";
        this.professorName= University.getInstance().getProfessorById(departmentId).getUsername();

        University.getInstance().getRecommendationRequests().add(this);
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
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
        res.append(University.getInstance().getProfessorById(departmentId).getUsername());
        res.append("recommend student");
        res.append(University.getInstance().getStudentById(studentId).getUsername()).append("with student Id:");
        res.append(University.getInstance().getStudentById(studentId).getId()+"\nhas been a hard worker,punctual and determined student.\n");
        res.append("this student has also had this courses with me:\n");
        res.append(University.getInstance().getStudentById(studentId).getStudentCoursesWithProfessor(departmentId));
        return res.toString();
    }
}
