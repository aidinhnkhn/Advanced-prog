package elements.courses;

public class Grade {
    private String courseId;
    private double grade;
    private boolean W;
    private boolean finished,finalGrade;
    private boolean objection,answered;
    private String objectionText;
    private String answerText;
    private int unit;
    private String name,professorId;
    private String gradeStatus;
    public Grade(String courseId, double grade) {
        this.courseId = courseId;
        this.grade = grade;
        this.W = false;
        this.finished = false;
        this.finalGrade=false;
        this.objection=false;
        this.answered=false;
        this.unit=Course.getCourse(courseId).getUnit();
        this.name=Course.getCourse(courseId).getName();
        this.professorId=Course.getCourse(courseId).getProfessorId();
    }

    public String getProfessorId() {
        if (professorId==null)
            this.professorId=Course.getCourse(courseId).getProfessorId();
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getUnit() {
        return unit;
    }

    public boolean isFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(boolean finalGrade) {
        this.finalGrade = finalGrade;
    }
    public boolean isObjection() {
        return objection;
    }

    public void setObjection(boolean objection) {
        this.objection = objection;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public String getObjectionText() {
        if (objectionText==null)
            objectionText="";
        return objectionText;
    }

    public void setObjectionText(String objectionText) {
        this.objectionText = objectionText;
    }

    public String getAnswerText() {
        if (answerText==null)
            answerText="";
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getName() {
        if (name==null){
            name=Course.getCourse(this.courseId).getName();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (this.W){
            this.finished=true;
            this.finalGrade=true;
        }
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getGradeStatus() {
        setGradeStatus();
        return gradeStatus;
    }

    public void setGradeStatus() {
        this.gradeStatus = showGrade();
    }

    public String showGrade() {
        if (this.W) return "W";
        if (!finished) return "N/A";
        else return Double.toString(this.grade);
    }
}