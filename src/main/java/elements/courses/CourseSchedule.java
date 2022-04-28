package elements.courses;

public class CourseSchedule {
    private String name;
    private String monday, tuesday, wednesday, thursday, friday;

    public CourseSchedule(Course course) {
        this.monday = "-";
        this.tuesday = "-";
        this.wednesday = "-";
        this.thursday = "-";
        this.friday = "-";
        this.name= course.getName();
        StringBuilder courseDuration = new StringBuilder();
        courseDuration.append(course.getHour() + " : ");
        courseDuration.append((course.getHour() + course.getLength()));
        for (String day : course.getDays()) {
            if (day.equals("Monday"))
                this.monday = courseDuration.toString();
            if (day.equals("Tuesday"))
                this.tuesday = courseDuration.toString();
            if (day.equals("Wednesday"))
                this.wednesday = courseDuration.toString();
            if (day.equals("Thursday"))
                this.thursday = courseDuration.toString();
            if (day.equals("Friday"))
                this.friday = courseDuration.toString();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }
}
