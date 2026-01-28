/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ers.group;

import java.util.*;

/**
 *
 * @author Andrea Ysabela
 */
public class CourseSubject {
    //Identity
    private String courseSubjectID;
    private String courseSubjectName;

    //Academic Constraints
    private int units;
    private int studentCount;
    private boolean isLabRoom;
    private boolean isScheduled = false;

    private ArrayList<CourseSubject> prerequisites;

    //Constructor
    public CourseSubject(String courseSubjectID, String courseSubjectName, 
                        int units, int studentCount, boolean isLabRoom) {
        this.courseSubjectID = courseSubjectID;
        this.courseSubjectName = courseSubjectName;
        this.units = units;
        this.studentCount = studentCount;
        this.isLabRoom = isLabRoom;
        this.prerequisites = new ArrayList<>();
    }

    //Methods
    public boolean canFitIn(int roomCapacity, boolean roomIsLab) {
        if (this.isLabRoom != roomIsLab) {
            return false;
        }
        return roomCapacity >= this.studentCount;
    }

    public void addPrerequisite(CourseSubject prerequisite) {
        if (prerequisite != null && !this.prerequisites.contains(prerequisite)) {
                this.prerequisites.add(prerequisite);
            }
    }

    //Getters and Setters
    public String getCourseSubjectID() { return courseSubjectID; }
    public String getCourseSubjectName() { return courseSubjectName;}
    public int getUnits() { return units;}
    public int getStudentCount() { return studentCount;}
    public boolean isLabRoom() { return isLabRoom;}
    public boolean isScheduled() { return isScheduled;}
    public ArrayList<CourseSubject> getPrerequisites() { return prerequisites; }
    
    public void setCourseSubjectID(String courseSubjectID) { this.courseSubjectID = courseSubjectID; }
    public void setCourseSubjectName(String courseSubjectName) { this.courseSubjectName = courseSubjectName; }
    public void setUnits(int units) { this.units = units; }
    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }
    public void setLabRoom(boolean labRoom) { this.isLabRoom = labRoom; }
    public void setScheduled(boolean scheduled) { this.isScheduled = scheduled; }
    public void setPrerequisites(ArrayList<CourseSubject> prerequisites) { this.prerequisites = prerequisites; }

    @Override
    public String toString() {
        String status = isScheduled ? "[Scheduled]" : "[Pending]";
        return String.format("%s %s (%s) - Units: %d, Capacity: %d", 
                status, courseSubjectID, courseSubjectName, units, studentCount);
}
}

