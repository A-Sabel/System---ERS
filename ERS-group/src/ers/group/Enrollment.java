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
        this.courseID = courseID;
        this.courseName = courseName;
        this.units = units;
        this.maxStudents = maxStudents;
        this.isLabCourse = isLab;
        this.yearLevel = year;
        this.semester = sem;
        this.prerequisites = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    // Methods
    public boolean canFitIn(int roomCapacity, boolean roomIsLab) {
        if (this.isLabCourse != roomIsLab) return false;
        return roomCapacity >= this.maxStudents;
    }

    public void addPrerequisite(CourseSubject prerequisite) {
        if (prerequisite != null && !prerequisites.contains(prerequisite)) {
            prerequisites.add(prerequisite);
        }
    }

    // --- ENROLLMENT METHODS ---
    public boolean enrollStudent(String studentName) {
        if (enrolledStudents.size() >= maxStudents) {
            System.out.println("Cannot enroll " + studentName + ": course full.");
            return false;
        }
        if (enrolledStudents.contains(studentName)) {
            System.out.println(studentName + " is already enrolled in " + courseName);
            return false;
        }
        enrolledStudents.add(studentName);
        System.out.println(studentName + " enrolled in " + courseName);
        return true;
    }

    public boolean dropStudent(String studentName) {
        if (!enrolledStudents.contains(studentName)) {
            System.out.println(studentName + " is not enrolled in " + courseName);
            return false;
        }
        enrolledStudents.remove(studentName);
        System.out.println(studentName + " dropped from " + courseName);
        return true;
    }

    public void displayEnrollment() {
        System.out.println("\nCourse: " + courseName + " (" + courseID + ")");
        System.out.println("Enrolled Students:");
        if (enrolledStudents.isEmpty()) {
            System.out.println(" - None");
        } else {
            for (String s : enrolledStudents) {
                System.out.println(" - " + s);
            }
        }
    }

    // Getters
    public String getCourseID() { return courseID; }
    public String getCourseName() { return courseName; }
    public int getUnits() { return units; }
    public int getMaxStudents() { return maxStudents; }
    public boolean isLabCourse() { return isLabCourse; }
    public boolean isScheduled() { return isScheduled; }
    public int getYearLevel() { return yearLevel; }
    public int getSemester() { return semester; }
    public ArrayList<CourseSubject> getPrerequisites() { return prerequisites; }

    // Setters
    public void setScheduled(boolean scheduled) { this.isScheduled = scheduled; }

    // toString
    @Override
    public String toString() {
        String status = isScheduled ? "[Scheduled]" : "[Pending]";
        return String.format("%s %s (%s) - Units: %d, Capacity: %d", 
                status, courseID, courseName, units, maxStudents);
    }
}
