package ers.group;


import java.util.ArrayList;
import java.util.Map;


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
   
    // Get subject names
    public String getQualifiedSubjectNames(Map<String, CourseSubject> subjectMap) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qualifiedSubjectIDs.size(); i++) {
            String id = qualifiedSubjectIDs.get(i);
            CourseSubject course = subjectMap.get(id);
            if (course != null) {
                sb.append(course.getCourseSubjectID()).append(" ").append(course.getCourseSubjectName());
                if (i < qualifiedSubjectIDs.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }
   
    // Getters
    public String getTeacherID() { return teacherID; }
    public String getTeacherName() { return teacherName; }
    public ArrayList<String> getQualifiedSubjectIDs() { return qualifiedSubjectIDs; }
    public ArrayList<String> getAssignedSchedules() { return assignedSchedules; }
   
    @Override
    public String toString() {
        return String.format("%s - %s", teacherID, teacherName);
    }
}



