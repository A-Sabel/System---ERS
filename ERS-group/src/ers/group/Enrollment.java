package ers.group;

public class Enrollment {

    private static int enrollmentCounter = 0;
    
    private String enrollmentID;
    private String studentID;
    private String sectionID;
    private String courseID;
    private String yearLevel;
    private String semester;
    private String status;

    public Enrollment(String enrollmentID, String studentID, String courseID, String yearLevel, String semester, String status) {
        if (enrollmentID == null || enrollmentID.isEmpty()) {
            throw new IllegalArgumentException("Enrollment ID cannot be empty");
        }
        if (studentID == null || studentID.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty");
        }
        if (courseID == null || courseID.isEmpty()) {
            throw new IllegalArgumentException("Course ID cannot be empty");
        }
        this.enrollmentID = enrollmentID;
        this.studentID = studentID;
        this.sectionID = ""; // Section assigned later
        this.courseID = courseID;
        this.yearLevel = yearLevel;
        this.semester = semester;
        this.status = status;
    }
    
    // Static method to generate new enrollment ID
    public static String generateEnrollmentID() {
        enrollmentCounter++;
        return String.format("ENR-%03d", enrollmentCounter);
    }
    
    // Static method to set the counter based on existing enrollments
    public static void setEnrollmentCounter(int maxId) {
        enrollmentCounter = maxId;
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

    public String getEnrollmentID() { return enrollmentID; }
    public String getStudentID() { return studentID; }
    public String getSectionID() { return sectionID; }
    public String getCourseID() { return courseID; }
    public String getYearLevel() { return yearLevel; }
    public String getSemester() { return semester; }
    public String getStatus() { return status; }
    
    public void setEnrollmentID(String enrollmentID) {
        if (enrollmentID == null || enrollmentID.isEmpty()) {
            throw new IllegalArgumentException("Enrollment ID cannot be empty");
        }
        this.enrollmentID = enrollmentID;
    }
    
    public void setStudentID(String studentID) {
        if (studentID == null || studentID.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty");
        }
        this.studentID = studentID;
    }
    
    public void setSectionID(String sectionID) {
        this.sectionID = sectionID; // Can be empty initially
    }
    
    public void setCourseID(String courseID) {
        if (courseID == null || courseID.isEmpty()) {
            throw new IllegalArgumentException("Course ID cannot be empty");
        }
        this.courseID = courseID;
    }
    
    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Enrollment " + enrollmentID + ": Student " + studentID + " in Course " + courseID + 
               " (Section " + sectionID + ") - Status: " + status;
    }
}