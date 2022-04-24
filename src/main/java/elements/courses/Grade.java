package elements.courses;

public class Grade {
    private String courseId;
    private double grade;
    private boolean W;
    private boolean finished;
    private int unit;
    private String name;
    public Grade(String courseId, double grade) {
        this.courseId = courseId;
        this.grade = grade;
        this.W = false;
        this.finished = false;
        this.unit=Course.getCourse(courseId).getUnit();
        this.name=Course.getCourse(courseId).getName();
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getCourseId() {
        return courseId;
    }

    public boolean isW() {
        return W;
    }

    public void setW(boolean w) {
        W = w;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String showGrade() {
        if (this.W) return "W";
        if (!finished) return "N/A";
        else return Double.toString(this.grade);
    }
}