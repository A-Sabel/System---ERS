package ers.group;

import java.util.ArrayList;

/**
 *
 * @author Andrea Ysabela
 */
public class Teachers {
    private final String teacherID;
    private final String teacherName;

    private final ArrayList<String> qualifiedSubjectIDs;
    private final ArrayList<String> assignedSchedules; 

    // Constructor
    public Teachers(String teacherID, String teacherName) {
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.qualifiedSubjectIDs = new ArrayList<>();
        this.assignedSchedules = new ArrayList<>();
    }
    
    // Methods
    public boolean canTeachSubject(String subjectID) {
        return this.qualifiedSubjectIDs.contains(subjectID);
    }

    // Actions
    public void assignSchedule(String schedule) {
        if (schedule != null && !this.assignedSchedules.contains(schedule)) {
            this.assignedSchedules.add(schedule);
        }
    }
    
    public void addQualifiedSubject(String subjectID) {
        if (subjectID != null && !this.qualifiedSubjectIDs.contains(subjectID)) {
            this.qualifiedSubjectIDs.add(subjectID);
        }
    }
    
    // Getters
    public String getTeacherID() { return teacherID; }
    public String getTeacherName() { return teacherName; }
    public ArrayList<String> getQualifiedSubjectIDs() { return qualifiedSubjectIDs; }
    public ArrayList<String> getAssignedSchedules() { return assignedSchedules; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (Prefers: %s)", teacherID, teacherName);
    }
}
