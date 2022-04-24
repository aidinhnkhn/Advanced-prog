package logic;

import Savers.Saver;
import elements.courses.Course;
import elements.courses.Grade;
import elements.people.Professor;
import elements.people.Student;
import elements.university.Department;

import java.io.File;
import java.util.ArrayList;

public class EditCourseLogic {
    private static EditCourseLogic editCourseLogic;

    private EditCourseLogic() {

    }

    public static EditCourseLogic getInstance() {
        if (editCourseLogic == null)
            editCourseLogic = new EditCourseLogic();
        return editCourseLogic;
    }

    public boolean createCourse(String name, String professorId, String departmentId, String unit, String length, String hour, ArrayList<String> days) {
        if (!checkCourse(name, professorId, departmentId, unit, length, hour, days)) return false;
        Course course = new Course(name, professorId, departmentId, Integer.parseInt(unit), days, Integer.parseInt(hour), Integer.parseInt(length));
        Saver.getInstance().saveCourse(course);
        return true;
    }

    public boolean editCourse(String name, String professorId,  String unit, String length, String hour, ArrayList<String> days, String id) {
        if (!doesCourseExist(id))
            return false;
        Course course = Course.getCourse(id);
        if (!LogicalAgent.getInstance().getUser().getDepartmentId().equals(course.getDepartmentId()))
            return false;
        if (!name.equals(""))
            course.setName(name);
        if (!professorId.equals(""))
            course.setProfessorId(professorId);
        if (!unit.equals(""))
            course.setUnit(Integer.parseInt(unit));
        if (!length.equals(""))
            course.setLength(Integer.parseInt(length));
        if (!hour.equals(""))
            course.setHour(Integer.parseInt(hour));
        if (days.size() != 0)
            course.setDays(days);
        return true;
    }

    public boolean checkCourse(String name, String professorId, String departmentId, String unit, String length, String hour, ArrayList<String> days) {
        if (name.equals("")) return false;
        if (professorId.equals("")) return false;
        if (departmentId.equals("")) return false;
        if (unit.equals("")) return false;
        if (length.equals("")) return false;
        if (hour.equals("")) return false;
        if (days.size() == 0) return false;
        if (!LogicalAgent.getInstance().getUser().getDepartmentId().equals(departmentId)) return false;
        return true;
    }

    public boolean deleteCourse(String id){
        if (!doesCourseExist(id))
            return false;
        Course course = Course.getCourse(id);
        if (!LogicalAgent.getInstance().getUser().getDepartmentId().equals(course.getDepartmentId()))
            return false;
        Professor professor=Professor.getProfessor(course.getProfessorId());
        professor.getCoursesId().remove(id);
        Department department=Department.getDepartment(course.getDepartmentId());
        department.getCourses().remove(id);
        Course.getCourses().remove(course);
        for (String studentId:course.getStudentId()){
            Student student=Student.getStudent(studentId);
            Grade grade=student.getGrade(id);
            if (grade==null) continue;
            if (grade.showGrade().equals("N/A"))
                grade.setGrade(20);
        }
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\course\\"+course.getId()+".txt");
        System.out.println(file.delete());
        return true;
    }
    public boolean doesCourseExist(String id) {
        Course course = Course.getCourse(id);
        if (course == null) return false;
        return true;
    }
}
