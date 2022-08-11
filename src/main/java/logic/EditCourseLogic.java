package logic;

import server.Savers.Saver;
import elements.courses.Course;
import elements.courses.Grade;
import elements.people.Professor;
import elements.people.Student;
import elements.university.Department;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import site.edu.Main;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EditCourseLogic {
    private static EditCourseLogic editCourseLogic;
    private static Logger log = LogManager.getLogger(EditCourseLogic.class);
    private EditCourseLogic() {

    }

    public static EditCourseLogic getInstance() {
        if (editCourseLogic == null)
            editCourseLogic = new EditCourseLogic();
        return editCourseLogic;
    }

    public boolean createCourse(String name, String professorId, String departmentId, String unit, String length,
                                String hour, ArrayList<String> days,LocalDate localDate,String examHour,String degree) {
        if (!checkCourse(name, professorId, departmentId, unit, length, hour, days,localDate,examHour,degree))
            return false;

        LocalDateTime localDateTime = localDate.atTime(Integer.parseInt(examHour),0);

        Main.mainClient.getServerController().createCourse(name, professorId, departmentId, unit, days,
                hour, length,localDateTime,degree);

        //log.info(Main.mainClient.getUser().getId()+" created: "+course.getId());
        return true;
    }

    public boolean editCourse(String name, String professorId,  String unit, String length, String hour, ArrayList<String> days,
                              String id,LocalDate localDate,String examHour,String degree) {
        //TODO: send a request to server to edit a course
        if (!doesCourseExist(id))
            return false;
        Course course = Main.mainClient.getServerController().getCourseById(id);
        if (!Main.mainClient.getUser().getDepartmentId().equals(course.getDepartmentId()))
            return false;
        if (!name.equals(""))
            course.setName(name);
        if (!professorId.equals(""))
            course.setProfessorIdInClient(professorId);
        if (!unit.equals(""))
            course.setUnit(Integer.parseInt(unit));
        if (!length.equals(""))
            course.setLength(Integer.parseInt(length));
        if (!hour.equals(""))
            course.setHour(Integer.parseInt(hour));
        if (days.size() != 0)
            course.setDays(days);
        if (localDate!=null && !examHour.equals(""))
            course.setExamDate(localDate,Integer.parseInt(examHour));
        if (!degree.equals(""))
            course.setDegree(degree);
        Main.mainClient.getServerController().editCourse(course);
        log.info(Main.mainClient.getUser().getId()+" edited: "+course.getId());
        return true;
    }

    public boolean checkCourse(String name, String professorId, String departmentId, String unit, String length, String hour,
                               ArrayList<String> days, LocalDate localdate,String examHour,String degree) {
        if (name.equals("")) return false;
        if (professorId.equals("")) return false;
        if (departmentId.equals("")) return false;
        if (unit.equals("")) return false;
        if (length.equals("")) return false;
        if (hour.equals("")) return false;
        if (days.size() == 0) return false;
        if (!Main.mainClient.getUser().getDepartmentId().equals(departmentId)) return false;
        if (localdate==null) return false;
        if (examHour.equals("")) return false;
        if (degree==null) return false;
        return true;
    }

    public boolean deleteCourse(String id){
        //TODO: send the request to Server to delete a course
        if (!doesCourseExist(id))
            return false;
        Course course = Main.mainClient.getServerController().getCourseById(id);
        if (!Main.mainClient.getUser().getDepartmentId().equals(course.getDepartmentId()))
            return false;
        Main.mainClient.getServerController().deleteCourse(id);

        return true;
    }
    public boolean doesCourseExist(String id) {
        Course course = Main.mainClient.getServerController().getCourseById(id);
        if (course == null) return false;
        return true;
    }
}
