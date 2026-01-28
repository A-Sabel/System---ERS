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
    private int yearLevel;
    private int semester;

    private ArrayList<CourseSubject> prerequisites;

    //Constructor
    public CourseSubject(String courseSubjectID, String courseSubjectName, 
                        int units, int studentCount, boolean isLabRoom, int yearLevel, int semester) {
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
    public String getCourseSubjectID() { return this.courseSubjectID; }
    public String getCourseSubjectName() { return this.courseSubjectName;}
    public int getUnits() { return this.units;}
    public int getStudentCount() { return this.studentCount;}
    public boolean isLabRoom() { return this.isLabRoom;}
    public boolean isScheduled() { return this.isScheduled;}
    public int getYearLevel() { return this.yearLevel; }
    public int getSemester() { return this.semester; }
    public ArrayList<CourseSubject> getPrerequisites() { return this.prerequisites; }

    @Override
    public String toString() {
        String status = isScheduled ? "[Scheduled]" : "[Pending]";
        return String.format("%s %s (%s) - Units: %d, Capacity: %d", 
                status, courseSubjectID, courseSubjectName, units, studentCount);
}
}

