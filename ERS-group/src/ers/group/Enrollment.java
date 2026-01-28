<<<<<<< HEAD
package ers.group;

import java.util.ArrayList;

public class Enrollment {
    // Identity
    private final String courseID;
    private final String courseName;

    // Academic constraints
    private final int units;
    private int maxStudents;
    private boolean isLabCourse;
    private boolean isScheduled = false;
    private int yearLevel;
    private int semester;

    private ArrayList<CourseSubject> prerequisites;

    // List of students enrolled in this course
    private ArrayList<String> enrolledStudents;

    // Constructor
    public Enrollment(String courseID, String courseName, int units, int maxStudents,
                        boolean isLab, int year, int sem) {
=======
package ers.group;  

public class Enrollment {

    // Link info
    private String studentID;
    private String sectionID;
    private String courseID;

    // Constructor
    public Enrollment(String studentID, String sectionID, String courseID) {
        this.studentID = studentID;
        this.sectionID = sectionID;
>>>>>>> 2cf12c3 (Add enrollment)
        this.courseID = courseID;
    }

    // Validate prerequisites (Admin checks before creating Enrollment)
    public static boolean validatePrerequisites(Student student, CourseSubject course) {
        return student.hasPassed(course.getPrerequisites());
    }

    // Check schedule conflicts (Admin checks before creating Enrollment)
    public static boolean conflictsWith(Schedule s1, Schedule s2) {
        if (!s1.getDay().equals(s2.getDay())) return false;
        return !(s1.getEndHour() <= s2.getStartHour() || s1.getStartHour() >= s2.getEndHour());
    }

    // Check if section is full 
    public static boolean isFull(Section section) {
        return section.getStudentIDs().size() >= section.getMaxStudents();
    }

    // Getters
    public String getStudentID() { return studentID; }
    public String getSectionID() { return sectionID; }
    public String getCourseID() { return courseID; }

    @Override
    public String toString() {
        return "Student " + studentID + " enrolled in Course " + courseID + " (Section " + sectionID + ")";
    }
}
