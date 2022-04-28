package elements.request;

import java.util.ArrayList;
import java.util.Random;

public class DormRequest extends Request{
    private static ArrayList<DormRequest> dormRequests=new ArrayList<>();

    public DormRequest(String studentId, String departmentId) {
        super(studentId, departmentId);
        this.pending=false;
        Random random=new Random();
        int randomNumber= random.nextInt(100);
        this.accepted=randomNumber<=24;
        this.acceptedText="You have a place to sleep.";
        this.requestText="please let this sleep under a roof.";
        this.deniedText="Oh ****, try again this **** is totally random.";
        dormRequests.add(this);
    }

    public static ArrayList<DormRequest> getDormRequests() {
        return dormRequests;
    }

    public static void setDormRequests(ArrayList<DormRequest> dormRequests) {
        DormRequest.dormRequests = dormRequests;
    }

    @Override
    public boolean getTotalAccepted() {
        return !pending && this.accepted;
    }

    @Override
    protected String makeAcceptedText() {
        return null;
    }

    @Override
    public void setStatusText() {
        if (this.pending)
            this.statusText="pending";
        else if (getTotalAccepted())
            this.statusText="accepted";
        else
            this.statusText=this.requestText;
    }
}
