package ers.group;

public class Enrollment {

    private String studentID;
    private String sectionID;
    private String courseID;

    public Enrollment(String studentID, String sectionID, String courseID) {
        this.studentID = studentID;
        this.sectionID = sectionID;
        this.courseID = courseID;
    }

    // Validate prerequisites
    public static boolean validatePrerequisites(Student student, CourseSubject course) {
        return student.hasPassed(course.getPrerequisites());
    }

    // Check schedule conflicts
    public static boolean conflictsWith(Schedule s1, Schedule s2) {
        if (!s1.getDay().equals(s2.getDay())) return false;
        return !(s1.getEndTime().compareTo(s2.getStartTime()) <= 0 || s1.getStartTime().compareTo(s2.getEndTime()) >= 0);
    }

    // Check if section is full
    public static boolean isFull(Section section) {
        return section.getEnrolledStudentIDs().size() >= section.getCapacityLimit();
    }

    public String getStudentID() { return studentID; }
    public String getSectionID() { return sectionID; }
    public String getCourseID() { return courseID; }

    @Override
    public String toString() {
        return "Student " + studentID + " enrolled in Course " + courseID + " (Section " + sectionID + ")";
    }
}