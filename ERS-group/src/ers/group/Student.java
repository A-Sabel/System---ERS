package ers.group;

import java.util.Collection;

public class Student {
    private final String studentID;

    public Student(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentID() { return studentID; }

    // Minimal hasPassed contract: returns true if student is assumed to have passed all prerequisites.
    // Replace with real transcript checking when available.
    public boolean hasPassed(Collection<CourseSubject> prerequisites) {
        return prerequisites == null || prerequisites.isEmpty();
    }
}
