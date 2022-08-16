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
        else if (message.getStatus() == MessageStatus.CreateCourse)
            Handler.getInstance().createCourse(message);
        else if (message.getStatus() == MessageStatus.EditCourse)
            Handler.getInstance().editCourse(message);
        else if (message.getStatus() == MessageStatus.DeleteCourse)
            Handler.getInstance().deleteCourse(message);
        else if (message.getStatus() == MessageStatus.RecommendationList)
            Handler.getInstance().sendRecommendationList(message);
        else if (message.getStatus() == MessageStatus.Recommendation)
            Handler.getInstance().createRecommendation(message);
        else if (message.getStatus() == MessageStatus.Certificate)
            Handler.getInstance().createCertificate(message);
        else if (message.getStatus() == MessageStatus.FreedomList)
            Handler.getInstance().sendFreedomList(message);
        else if (message.getStatus() == MessageStatus.Freedom)
            Handler.getInstance().setHimFree(message);
        else if (message.getStatus() == MessageStatus.DormList)
            Handler.getInstance().sendDormList(message);
        else if (message.getStatus() == MessageStatus.Dorm)
            Handler.getInstance().createDorm(message);
        else if (message.getStatus() == MessageStatus.MinorList)
            Handler.getInstance().sendMinorList(message);
        else if (message.getStatus() == MessageStatus.Minor)
            Handler.getInstance().createMinor(message);
        else if (message.getStatus() == MessageStatus.ThesisList)
            Handler.getInstance().sendThesisList(message);
        else if (message.getStatus() == MessageStatus.Thesis)
            Handler.getInstance().createThesis(message);
        else if (message.getStatus() == MessageStatus.Minor1stAccept)
            Handler.getInstance().acceptMinor1st(message);
        else if (message.getStatus() == MessageStatus.Minor2ndAccept)
            Handler.getInstance().acceptMinor2nd(message);
        else if (message.getStatus() == MessageStatus.RecommendationAccept)
            Handler.getInstance().acceptRecommendation(message);
        else if (message.getStatus() == MessageStatus.FreedomAccept)
            Handler.getInstance().acceptFreedom(message);
        else if (message.getStatus() == MessageStatus.Update)
            Handler.getInstance().sendUpdate(message);
        else if (message.getStatus() == MessageStatus.checkUser)
            Handler.getInstance().checkUser(message);
        else if (message.getStatus() == MessageStatus.CreateChat)
            Handler.getInstance().createChat(message);
        else if (message.getStatus() == MessageStatus.AddPm)
            Handler.getInstance().addPm(message);
        else if (message.getStatus() == MessageStatus.GetAdmin)
            Handler.getInstance().sendAdmin(message);
        else if (message.getStatus() == MessageStatus.OnlineAgain)
            Handler.getInstance().setClientHandlerUser(message);
    }
}
