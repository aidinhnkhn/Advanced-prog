package server.network;

import shared.messages.message.Message;
import shared.messages.message.MessageStatus;

public class Analyser {
    private static Analyser analyser;

    private Analyser(){

    }

    public static Analyser getInstance(){
        if (analyser==null)
            analyser = new Analyser();
        return analyser;
    }

    public void analyse(Message message){
        if (message.getStatus()== MessageStatus.Captcha)
            Handler.getInstance().sendCaptcha(message.getAuthToken());
        else if (message.getStatus() == MessageStatus.Login)
            Handler.getInstance().checkLogin(message.getAuthToken(),message);
        else if (message.getStatus() == MessageStatus.NewPassword)
            Handler.getInstance().setNewPassword(message);
        else if (message.getStatus() == MessageStatus.LastEnter)
            Handler.getInstance().setLastEnter(message.getAuthToken());
        else if (message.getStatus() == MessageStatus.UserImage)
            Handler.getInstance().sendUserImage(message);
        else if (message.getStatus() == MessageStatus.getProfessor)
            Handler.getInstance().sendProfessor(message);
        else if (message.getStatus() == MessageStatus.NewUserInfo)
            Handler.getInstance().setUserInfo(message);
        else if (message.getStatus() == MessageStatus.Objection)
            Handler.getInstance().setGradeObjection(message);
        else if (message.getStatus() == MessageStatus.StudentList)
            Handler.getInstance().sendStudentList(message);
        else if (message.getStatus() == MessageStatus.Course)
            Handler.getInstance().sendCourse(message);
        else if (message.getStatus() == MessageStatus.Student)
            Handler.getInstance().sendStudent(message);
        else if (message.getStatus() == MessageStatus.setGrade)
            Handler.getInstance().setGrade(message);
        else if (message.getStatus() == MessageStatus.FinalizeGrades)
            Handler.getInstance().finalizeGrades(message);
        else if (message.getStatus() == MessageStatus.AnswerObjection)
            Handler.getInstance().AnswerObjection(message);
        else if (message.getStatus() == MessageStatus.CourseList)
            Handler.getInstance().sendCourseList(message);
        else if (message.getStatus() == MessageStatus.ProfessorList)
            Handler.getInstance().sendProfessorList(message);
        else if (message.getStatus() == MessageStatus.PickCourse)
            Handler.getInstance().pickCourse(message);
        else if (message.getStatus() == MessageStatus.DepartmentList)
            Handler.getInstance().sendDepartmentList(message);
        else if (message.getStatus() == MessageStatus.Department)
            Handler.getInstance().sendDepartment(message);
        else if (message.getStatus() == MessageStatus.SendUserImage)
            Handler.getInstance().saveUserImage(message);
        else if (message.getStatus() == MessageStatus.CreateStudent)
            Handler.getInstance().createStudent(message);
        else if (message.getStatus() == MessageStatus.CreateProfessor)
            Handler.getInstance().createProfessor(message);
    }
}
