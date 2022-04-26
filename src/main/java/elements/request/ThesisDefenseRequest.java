package elements.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ThesisDefenseRequest extends Request{
    private static ArrayList<ThesisDefenseRequest> thesisDefenseRequests=new ArrayList<>();
    private LocalDateTime defenseDate;
    public ThesisDefenseRequest(String studentId, String departmentId, LocalDateTime defenseDate) {
        super(studentId, departmentId);
        this.defenseDate=defenseDate;
        this.pending=false;
        this.accepted=true;
        this.acceptedText=makeAcceptedText();
        thesisDefenseRequests.add(this);
    }

    @Override
    public boolean getTotalAccepted() {
        return true;
    }

    @Override
    protected String makeAcceptedText() {
        return "you can defend your thesis in: "
                +defenseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static ArrayList<ThesisDefenseRequest> getThesisDefenseRequests() {
        return thesisDefenseRequests;
    }

    public static void setThesisDefenseRequests(ArrayList<ThesisDefenseRequest> thesisDefenseRequests) {
        ThesisDefenseRequest.thesisDefenseRequests = thesisDefenseRequests;
    }

    public LocalDateTime getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(LocalDateTime defenseDate) {
        this.defenseDate = defenseDate;
    }
}
